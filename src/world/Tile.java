package world;

/**
 * Represents a single cell in the dungeon grid.
 * Tile type determines walkability and rendering color.
 */
public class Tile {

    public enum Type {
        WALL,
        FLOOR,
        CORRIDOR,
        STAIRS
    }

    private Type type;

    public Tile(Type type) {
        this.type = type;
    }

    /** A tile is walkable if it is floor, corridor, or stairs. */
    public boolean isWalkable() {
        return type == Type.FLOOR || type == Type.CORRIDOR || type == Type.STAIRS;
    }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    @Override
    public String toString() {
        return type.name();
    }
}
