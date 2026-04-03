package world;

import utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Procedurally generates a dungeon using random room placement
 * followed by L-shaped corridor connections between room centres.
 *
 * Algorithm:
 *  1. Fill map with walls.
 *  2. Place random rectangular rooms (min/max size from Constants).
 *  3. Connect each room to the previous one via two L-shaped corridors.
 *  4. Expose the list of room centres for entity spawning.
 */
public class DungeonGenerator {

    private final Random rng;
    private final List<int[]> roomCentres = new ArrayList<>(); // [col, row]

    public DungeonGenerator() {
        this.rng = new Random();
    }

    public DungeonGenerator(long seed) {
        this.rng = new Random(seed);
    }

    /**
     * Generates and populates the given MapGrid.
     * Returns list of room centres as [col, row] arrays.
     */
    public List<int[]> generate(MapGrid map, int minRooms, int maxRooms, int minRoomSize, int maxRoomSize) {
        roomCentres.clear();

        int cols = map.getCols();
        int rows = map.getRows();
        int roomCount = minRooms + rng.nextInt(maxRooms - minRooms + 1);

        List<int[]> rooms = new ArrayList<>(); // stored as [x, y, w, h] (col, row, width, height)

        for (int attempt = 0; attempt < roomCount * 5; attempt++) {
            if (rooms.size() >= roomCount) break;

            int w = minRoomSize + rng.nextInt(maxRoomSize - minRoomSize + 1);
            int h = minRoomSize + rng.nextInt(maxRoomSize - minRoomSize + 1);
            int x = 1 + rng.nextInt(cols - w - 2);
            int y = 1 + rng.nextInt(rows - h - 2);

            if (!overlapsAny(rooms, x, y, w, h)) {
                carveRoom(map, x, y, w, h);
                rooms.add(new int[]{x, y, w, h});

                int cx = x + w / 2;
                int cy = y + h / 2;
                roomCentres.add(new int[]{cx, cy});

                if (rooms.size() > 1) {
                    int[] prev = roomCentres.get(roomCentres.size() - 2);
                    carveCorridor(map, prev[0], prev[1], cx, cy);
                }
            }
        }

        if (roomCentres.size() > 1) {
            int[] exit = roomCentres.get(roomCentres.size() - 1);
            map.set(exit[0], exit[1], Tile.Type.STAIRS);
        }

        return new ArrayList<>(roomCentres);
    }

    // ------------------------------------------------------------------ //
    // Private helpers
    // ------------------------------------------------------------------ //

    private void carveRoom(MapGrid map, int x, int y, int w, int h) {
        for (int r = y; r < y + h; r++)
            for (int c = x; c < x + w; c++)
                map.set(c, r, Tile.Type.FLOOR);
    }

    /**
     * Carve an L-shaped corridor from (x1,y1) to (x2,y2).
     * Direction is randomised to avoid predictable layouts.
     */
    private void carveCorridor(MapGrid map, int x1, int y1, int x2, int y2) {
        if (rng.nextBoolean()) {
            carveHLine(map, x1, x2, y1);
            carveVLine(map, y1, y2, x2);
        } else {
            carveVLine(map, y1, y2, x1);
            carveHLine(map, x1, x2, y2);
        }
    }

    private void carveHLine(MapGrid map, int x1, int x2, int y) {
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        for (int c = minX; c <= maxX; c++) {
            Tile t = map.get(c, y);
            if (t != null && !t.isWalkable())
                map.set(c, y, Tile.Type.CORRIDOR);
        }
    }

    private void carveVLine(MapGrid map, int y1, int y2, int x) {
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        for (int r = minY; r <= maxY; r++) {
            Tile t = map.get(x, r);
            if (t != null && !t.isWalkable())
                map.set(x, r, Tile.Type.CORRIDOR);
        }
    }

    /** AABB overlap check with 1-tile padding. */
    private boolean overlapsAny(List<int[]> rooms, int x, int y, int w, int h) {
        for (int[] r : rooms) {
            if (x <= r[0] + r[2] + 1 && x + w + 1 >= r[0] &&
                y <= r[1] + r[3] + 1 && y + h + 1 >= r[1])
                return true;
        }
        return false;
    }
}
