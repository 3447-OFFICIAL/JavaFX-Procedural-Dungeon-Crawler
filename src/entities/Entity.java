package entities;

/**
 * Abstract base class for all game entities (Player, Enemy).
 * Implements Combatant interface to demonstrate Interface usage.
 */
public abstract class Entity implements Combatant {

    protected int col;   // grid column
    protected int row;   // grid row
    protected int hp;
    protected int maxHp;
    protected boolean alive;

    public Entity(int col, int row, int maxHp) {
        this.col   = col;
        this.row   = row;
        this.maxHp = maxHp;
        this.hp    = maxHp;
        this.alive = true;
    }

    /** Apply damage. Entity dies when hp reaches 0. */
    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
        if (hp == 0) alive = false;
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    // ------------------------------------------------------------------ //
    // Getters / setters
    // ------------------------------------------------------------------ //
    public int  getCol()   { return col; }
    public int  getRow()   { return row; }
    public void setCol(int col) { this.col = col; }
    public void setRow(int row) { this.row = row; }
    public int  getHp()    { return hp; }
    public int  getMaxHp() { return maxHp; }
    public boolean isAlive() { return alive; }

    /** Manhattan distance in tiles to another entity. */
    public int distanceTo(Entity other) {
        return Math.abs(col - other.col) + Math.abs(row - other.row);
    }
}
