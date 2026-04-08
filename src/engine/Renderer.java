package engine;

import entities.Enemy;
import entities.Entity;
import entities.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import utils.Constants;
import world.MapGrid;
import world.Tile;

import java.util.List;

/**
 * Renderer draws everything onto the JavaFX Canvas each frame.
 *
 * Drawing order:
 *  1. Map tiles (walls / floors / corridors)
 *  2. Enemies
 *  3. Player
 *  4. HUD (HP bar, stats)
 *  5. Overlays (game over)
 */
public class Renderer {

    private final Canvas       canvas;
    private final GraphicsContext gc;

    // Cached colours (parsed once)
    private static final Color C_WALL     = Color.web(Constants.COLOR_WALL);
    private static final Color C_FLOOR    = Color.web(Constants.COLOR_FLOOR);
    private static final Color C_CORRIDOR = Color.web(Constants.COLOR_CORRIDOR);
    private static final Color C_PLAYER   = Color.web(Constants.COLOR_PLAYER);
    private static final Color C_ENEMY    = Color.web(Constants.COLOR_ENEMY);
    private static final Color C_HUD_BG   = Color.web(Constants.COLOR_HUD_BG);
    private static final Color C_HUD_TEXT = Color.web(Constants.COLOR_HUD_TEXT);
    private static final Color C_HP_BAR   = Color.web(Constants.COLOR_HP_BAR);
    private static final Color C_HP_LOW   = Color.web(Constants.COLOR_HP_LOW);
    private static final Color C_STAIRS   = Color.web("#9c27b0"); // Purple stairs

    private static final int T = Constants.TILE_SIZE;

    public Renderer(Canvas canvas) {
        this.canvas = canvas;
        this.gc     = canvas.getGraphicsContext2D();
        gc.setFont(Font.font("Monospace", FontWeight.BOLD, 13));
    }

    // ------------------------------------------------------------------ //
    // Main render entry
    // ------------------------------------------------------------------ //

    public void render(MapGrid map, Player player, List<Enemy> enemies,
                       GameStateManager gsm, boolean paused) {
        clearCanvas();
        drawMap(map);
        drawEnemies(enemies);
        drawPlayer(player);
        drawHUD(player, enemies, gsm);

        drawAttackEffect(player);

        if (gsm.getState() == GameStateManager.State.GAME_OVER) {
            drawGameOverOverlay();
        } else if (paused) {
            drawPauseOverlay();
        }
    }

    // ------------------------------------------------------------------ //
    // Private draw methods
    // ------------------------------------------------------------------ //

    private void clearCanvas() {
        gc.setFill(C_WALL);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawMap(MapGrid map) {
        Tile[][] raw = map.getRaw();
        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                Tile t = raw[r][c];
                gc.setFill(tileColor(t.getType()));
                gc.fillRect(c * T, r * T, T, T);

                // Subtle 1-px border for floor tiles to show grid
                if (t.isWalkable()) {
                    gc.setStroke(Color.rgb(0, 0, 0, 0.20));
                    gc.setLineWidth(0.5);
                    gc.strokeRect(c * T, r * T, T, T);
                }
            }
        }
    }

    private Color tileColor(Tile.Type type) {
        return switch (type) {
            case WALL     -> C_WALL;
            case FLOOR    -> C_FLOOR;
            case CORRIDOR -> C_CORRIDOR;
            case STAIRS   -> C_STAIRS;
        };
    }

    private void drawPlayer(Player player) {
        int px = player.getCol() * T;
        int py = player.getRow() * T;
        int margin = 3;

        // Glow effect
        gc.setFill(Color.rgb(79, 195, 247, 0.25));
        gc.fillOval(px - 2, py - 2, T + 4, T + 4);

        // Body
        gc.setFill(C_PLAYER);
        gc.fillOval(px + margin, py + margin, T - margin * 2, T - margin * 2);

        // Highlight dot
        gc.setFill(Color.WHITE.deriveColor(0, 1, 1, 0.5));
        gc.fillOval(px + margin + 2, py + margin + 2, 4, 4);
    }

    private void drawEnemies(List<Enemy> enemies) {
        for (Enemy e : enemies) {
            if (!e.isAlive()) continue;
            int ex = e.getCol() * T;
            int ey = e.getRow() * T;
            int margin = 4;

            // Glow
            gc.setFill(Color.rgb(239, 83, 80, 0.20));
            gc.fillOval(ex - 2, ey - 2, T + 4, T + 4);

            // Body (diamond shape via rotation)
            gc.setFill(C_ENEMY);
            double cx_ = ex + T / 2.0;
            double cy_ = ey + T / 2.0;
            double r   = (T / 2.0) - margin;
            double[] xp = {cx_, cx_ + r, cx_, cx_ - r};
            double[] yp = {cy_ - r, cy_, cy_ + r, cy_};
            gc.fillPolygon(xp, yp, 4);

            // Small HP bar above enemy
            drawMiniHpBar(ex, ey - 5, T, e.getHp(), e.getMaxHp());
        }
    }

    private void drawMiniHpBar(int x, int y, int width, int hp, int maxHp) {
        double ratio = (double) hp / maxHp;
        gc.setFill(Color.rgb(30, 30, 30));
        gc.fillRect(x, y, width, 3);
        gc.setFill(ratio > 0.4 ? C_HP_BAR : C_HP_LOW);
        gc.fillRect(x, y, width * ratio, 3);
    }

    private void drawHUD(Player player, List<Enemy> enemies, GameStateManager gsm) {
        double hudY = Constants.MAP_ROWS * T;

        // Background
        gc.setFill(C_HUD_BG);
        gc.fillRect(0, hudY, canvas.getWidth(), 60);

        // HP Label
        gc.setFill(C_HUD_TEXT);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("HP", 10, hudY + 20);

        // HP bar
        double barW = 200;
        double barH = 14;
        double ratio = (double) player.getHp() / player.getMaxHp();
        gc.setFill(Color.rgb(50, 50, 50));
        gc.fillRect(40, hudY + 7, barW, barH);
        gc.setFill(ratio > 0.35 ? C_HP_BAR : C_HP_LOW);
        gc.fillRect(40, hudY + 7, barW * ratio, barH);
        gc.setStroke(Color.gray(0.5));
        gc.setLineWidth(1);
        gc.strokeRect(40, hudY + 7, barW, barH);

        // HP numbers
        gc.setFill(C_HUD_TEXT);
        gc.fillText(player.getHp() + " / " + player.getMaxHp(), 250, hudY + 20);

        // ATK Display
        gc.setFill(Color.web("#ffa726")); // Orange
        gc.fillText("ATK: " + player.getAttackDamage(), 330, hudY + 20);

        // Enemy count
        long aliveCount = enemies.stream().filter(Entity::isAlive).count();
        gc.setFill(C_HUD_TEXT);
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText("Enemies: " + aliveCount, canvas.getWidth() - 10, hudY + 20);

        // Level display
        gc.setFill(Color.GOLD);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("LEVEL " + gsm.getLevel(), canvas.getWidth() / 2.0, hudY + 20);

        // Controls hint
        gc.setFill(Color.gray(0.5));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("WASD/Arrows Move | Space Attack | P Pause | R Restart", canvas.getWidth() / 2.0, hudY + 50);
    }

    private void drawGameOverOverlay() {
        // Dim overlay
        gc.setFill(Color.rgb(0, 0, 0, 0.72));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Title
        gc.setFont(Font.font("Monospace", FontWeight.BOLD, 42));
        gc.setFill(C_ENEMY);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("GAME OVER", canvas.getWidth() / 2.0, canvas.getHeight() / 2.0 - 20);

        // Subtitle
        gc.setFont(Font.font("Monospace", FontWeight.NORMAL, 16));
        gc.setFill(C_HUD_TEXT);
        gc.fillText("You were defeated by the dungeon", canvas.getWidth() / 2.0,
                canvas.getHeight() / 2.0 + 20);

        gc.setFill(Color.gray(0.6));
        gc.fillText("Press R to restart", canvas.getWidth() / 2.0,
                canvas.getHeight() / 2.0 + 50);

        // Reset font
        gc.setFont(Font.font("Monospace", FontWeight.BOLD, 13));
    }

    private void drawPauseOverlay() {
        // Semi-transparent dark overlay
        gc.setFill(Color.rgb(0, 0, 0, 0.50));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // "PAUSED" Text
        gc.setFont(Font.font("Monospace", FontWeight.BOLD, 48));
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("PAUSED", canvas.getWidth() / 2.0, canvas.getHeight() / 2.0);

        gc.setFont(Font.font("Monospace", FontWeight.NORMAL, 16));
        gc.fillText("Press P to Resume", canvas.getWidth() / 2.0, canvas.getHeight() / 2.0 + 40);

        // Reset font
        gc.setFont(Font.font("Monospace", FontWeight.BOLD, 13));
    }

    private void drawAttackEffect(Player player) {
        long now = System.currentTimeMillis();
        long elapsed = now - player.getLastAttackTime();
        long duration = 150; // ms

        if (elapsed < duration) {
            double alpha = 1.0 - (double) elapsed / duration;
            int px = player.getCol() * T + T / 2;
            int py = player.getRow() * T + T / 2;

            // Simple white circle expanding outward
            gc.setStroke(Color.rgb(255, 255, 255, alpha));
            gc.setLineWidth(3);
            double r = 10 + (elapsed / 10.0);
            gc.strokeOval(px - r, py - r, r * 2, r * 2);

            // X marks the spot
            gc.setStroke(Color.rgb(229, 57, 53, alpha));
            double xSize = 15;
            gc.strokeLine(px - xSize, py - xSize, px + xSize, py + xSize);
            gc.strokeLine(px + xSize, py - xSize, px - xSize, py + xSize);
        }
    }
}
