package world;

/**
 * Wraps the 2D array of Tiles and exposes safe accessors.
 * All coordinate validation is centralised here.
 */
public class MapGrid {

    private final int cols;
    private final int rows;
    private final Tile[][] grid;

    public MapGrid(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.grid = new Tile[rows][cols];
        fill(Tile.Type.WALL);
    }

    /** Fill every cell with the given type. */
    public void fill(Tile.Type type) {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c] = new Tile(type);
    }

    /** Returns the tile at (col, row), or null if out of bounds. */
    public Tile get(int col, int row) {
        if (!inBounds(col, row)) return null;
        return grid[row][col];
    }

    /** Sets the tile type at (col, row). No-op if out of bounds. */
    public void set(int col, int row, Tile.Type type) {
        if (!inBounds(col, row)) return;
        grid[row][col].setType(type);
    }

    public boolean isWalkable(int col, int row) {
        Tile t = get(col, row);
        return t != null && t.isWalkable();
    }

    public boolean inBounds(int col, int row) {
        return col >= 0 && col < cols && row >= 0 && row < rows;
    }

    public int getCols() { return cols; }
    public int getRows() { return rows; }

    /** Expose raw grid for renderer (read-only intent). */
    public Tile[][] getRaw() { return grid; }
}
