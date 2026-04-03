package entities;

import utils.Constants;

/**
 * Enemy entity.
 * Movement is delegated to AIController; this class only
 * stores state and provides movement application.
 */
public class Enemy extends Entity {

    private int attackDamage;
    private long lastMoveTime; // nanoseconds, for timed movement

    public Enemy(int col, int row, int level) {
        super(col, row, Constants.ENEMY_START_HP + (level - 1) * 15);
        this.attackDamage = Constants.ENEMY_ATTACK + (level - 1) * 3;
        this.lastMoveTime = System.nanoTime();
    }

    /** Apply a move delta if collision allows (checked by caller). */
    public void applyMove(int dc, int dr) {
        col += dc;
        row += dr;
        lastMoveTime = System.nanoTime();
    }

    /** Returns true if the enemy's move cooldown has elapsed. */
    public boolean isReadyToMove() {
        long elapsed = (System.nanoTime() - lastMoveTime) / 1_000_000L; // ms
        return elapsed >= Constants.ENEMY_MOVE_DELAY_MS;
    }

    public int getAttackDamage() { return attackDamage; }

    public long getLastMoveTime() { return lastMoveTime; }
}
