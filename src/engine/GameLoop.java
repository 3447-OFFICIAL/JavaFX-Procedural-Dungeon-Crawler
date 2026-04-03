package engine;

import ai.AIController;
import entities.Enemy;
import entities.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import utils.Constants;
import world.DungeonGenerator;
import world.MapGrid;
import world.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Core game loop using JavaFX AnimationTimer (~60 fps).
 *
 * Responsibilities:
 *  - Maintain update / render separation
 *  - Own all game objects (map, player, enemies)
 *  - Delegate AI updates to AIController
 *  - Trigger restart on 'R' key
 */
public class GameLoop {

    // Core systems
    private final InputHandler    input;
    private final Renderer        renderer;
    private final GameStateManager gsm;
    private final AIController    aiController;

    // Game world (re-created on restart)
    private MapGrid    map;
    private Player     player;
    private List<Enemy> enemies;

    // Timing
    private final AnimationTimer timer;

    public GameLoop(Canvas canvas, InputHandler input) {
        this.input        = input;
        this.renderer     = new Renderer(canvas);
        this.gsm          = new GameStateManager();
        this.aiController = new AIController();

        initGame();

        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                renderer.render(map, player, enemies, gsm);
            }
        };
    }

    public void start() { timer.start(); }
    public void stop()  { timer.stop();  }

    // ------------------------------------------------------------------ //
    // Game initialisation / restart
    // ------------------------------------------------------------------ //

    private void initGame() {
        int level = gsm.getLevel();
        map     = new MapGrid(Constants.MAP_COLS, Constants.MAP_ROWS);
        enemies = new ArrayList<>();
        input.clearAll();

        // --- Dungeon Director AI ---
        // Scale rooms: more rooms as we go deeper
        int minRooms = Constants.MIN_ROOMS + (level / 2);
        int maxRooms = Constants.MAX_ROOMS + level;

        // Harden map: level 5+ makes rooms smaller/cramped
        int minSize = Constants.MIN_ROOM_SIZE;
        int maxSize = Constants.MAX_ROOM_SIZE;
        if (level >= 5) {
            maxSize = Math.max(minSize + 2, Constants.MAX_ROOM_SIZE - (level / 5));
        }

        DungeonGenerator gen = new DungeonGenerator();
        List<int[]> centres = gen.generate(map, minRooms, maxRooms, minSize, maxSize);

        if (centres.isEmpty()) {
            player = new Player(1, 1);
            player.upgradeStats(level);
            return;
        }

        // Spawn/Update Player
        int[] pSpawn = centres.get(0);
        if (player == null) {
            player = new Player(pSpawn[0], pSpawn[1]);
        } else {
            player.setCol(pSpawn[0]);
            player.setRow(pSpawn[1]);
        }
        player.upgradeStats(level);

        // --- Enemy Scaling ---
        int targetEnemyCount = Constants.ENEMY_COUNT + (level - 1);
        int spawned = 0;

        // Try to spawn far from player
        for (int i = 1; i < centres.size() && spawned < targetEnemyCount; i++) {
            int[] ec = centres.get(i);
            if (Math.abs(ec[0] - pSpawn[0]) + Math.abs(ec[1] - pSpawn[1]) >= 8) {
                enemies.add(new Enemy(ec[0], ec[1], level));
                spawned++;
            }
        }

        // Fill remaining enemy slots
        for (int i = 1; i < centres.size() && spawned < targetEnemyCount; i++) {
            int[] ec = centres.get(i);
            boolean alreadyUsed = enemies.stream().anyMatch(e -> e.getCol() == ec[0] && e.getRow() == ec[1]);
            if (!alreadyUsed) {
                enemies.add(new Enemy(ec[0], ec[1], level));
                spawned++;
            }
        }
    }

    // ------------------------------------------------------------------ //
    // Per-frame update
    // ------------------------------------------------------------------ //

    private void update() {
        // Allow restart even during game-over
        if (input.isHeld(javafx.scene.input.KeyCode.R) && !gsm.isRunning()) {
            gsm.reset();
            player = null; // Force fresh player spawn in initGame
            initGame();
            return;
        }

        if (!gsm.isRunning()) return;

        // --- Progression check: Stairs ---
        Tile currentTile = map.get(player.getCol(), player.getRow());
        if (currentTile != null && currentTile.getType() == Tile.Type.STAIRS) {
            advanceLevel();
            return;
        }

        // --- Player movement & attack ---
        player.handleMovement(input, map);
        if (input.consumeAttack()) {
            handlePlayerAttack();
        }

        // --- Enemy AI ---
        for (Enemy enemy : enemies) {
            aiController.update(enemy, player, map);
        }

        // --- Game over check ---
        if (!player.isAlive()) {
            gsm.setGameOver();
            input.clearAll();
        }
    }

    private void advanceLevel() {
        gsm.nextLevel();
        // initGame handles player upgrades
        initGame();
    }

    private void handlePlayerAttack() {
        player.setLastAttackTime(System.currentTimeMillis());

        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) continue;

            // Attack hits all ordinal adjacent tiles (distance = 1)
            int dist = Math.abs(enemy.getCol() - player.getCol()) +
                       Math.abs(enemy.getRow() - player.getRow());

            if (dist == 1) {
                enemy.takeDamage(player.getAttackDamage());
            }
        }
    }
}
