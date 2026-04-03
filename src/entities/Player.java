package entities;

import engine.InputHandler;
import utils.Constants;
import world.MapGrid;

/**
 * Player entity.
 * Movement is driven by InputHandler: one tile per key-press event
 * (not per frame), giving classic roguelike feel.
 */
public class Player extends Entity {

    private int attackDamage;
    private long lastAttackTime = 0;

    public Player(int col, int row) {
        super(col, row, Constants.PLAYER_START_HP);
        this.attackDamage = Constants.PLAYER_ATTACK;
    }

    /**
     * Attempt to move the player based on pressed keys.
     * Returns true if the player actually moved.
     *
     * @param input   current InputHandler state
     * @param map     map used for collision detection
     */
    public boolean handleMovement(InputHandler input, MapGrid map) {
        int dc = 0, dr = 0;

        if      (input.consumeMove("UP"))    dr = -1;
        else if (input.consumeMove("DOWN"))  dr =  1;
        else if (input.consumeMove("LEFT"))  dc = -1;
        else if (input.consumeMove("RIGHT")) dc =  1;

        if (dc == 0 && dr == 0) return false;

        int newCol = col + dc;
        int newRow = row + dr;

        if (map.isWalkable(newCol, newRow)) {
            col = newCol;
            row = newRow;
            return true;
        }
        return false;
    }

    public int getAttackDamage() { return attackDamage; }

    public void setLastAttackTime(long time) { this.lastAttackTime = time; }
    public long getLastAttackTime() { return lastAttackTime; }

    public void upgradeStats(int level) {
        // +10 Max HP every level
        this.maxHp = Constants.PLAYER_START_HP + (level - 1) * 10;
        // +2 Attack every level
        this.attackDamage = Constants.PLAYER_ATTACK + (level - 1) * 2;
        // Fully heal on upgrade (handled in GameLoop usually, but safe here)
        this.hp = this.maxHp;
        this.alive = true;
    }
}
