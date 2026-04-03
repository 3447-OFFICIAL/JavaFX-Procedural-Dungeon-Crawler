package ai;

import world.MapGrid;

import java.util.*;

/**
 * A* pathfinding on the tile grid.
 *
 * Uses Manhattan distance as the heuristic (appropriate for 4-directional
 * movement on a grid).  Returns a list of [col, row] steps; the first element
 * is always the next tile to step onto, excluding the start tile.
 */
public class Pathfinding {

    /** 4-directional movement deltas: up, down, left, right. */
    private static final int[][] DIRS = {{0,-1},{0,1},{-1,0},{1,0}};

    /**
     * Finds the shortest walkable path from (startCol,startRow) to (goalCol,goalRow).
     *
     * @return list of [col, row] steps (excluding start), or empty list if no path.
     */
    public static List<int[]> findPath(MapGrid map,
                                       int startCol, int startRow,
                                       int goalCol,  int goalRow) {

        if (!map.isWalkable(goalCol, goalRow)) return Collections.emptyList();

        int cols = map.getCols();
        int rows = map.getRows();

        // Node: [col, row, gCost, hCost, parentIndex]
        // We use a flat index = row*cols+col for O(1) lookup.
        int total = cols * rows;
        int[] gCost   = new int[total];
        int[] parent  = new int[total];
        Arrays.fill(gCost,  Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        int startIdx = startRow * cols + startCol;
        int goalIdx  = goalRow  * cols + goalCol;
        gCost[startIdx] = 0;

        // open set: PriorityQueue ordered by fCost = g + h
        PriorityQueue<int[]> open = new PriorityQueue<>(
                Comparator.comparingInt(n -> n[2])); // n[2] = fCost

        open.offer(new int[]{startCol, startRow, heuristic(startCol, startRow, goalCol, goalRow)});

        boolean[] closed = new boolean[total];

        while (!open.isEmpty()) {
            int[] cur = open.poll();
            int cx = cur[0], cy = cur[1];
            int cIdx = cy * cols + cx;

            if (closed[cIdx]) continue;
            closed[cIdx] = true;

            if (cIdx == goalIdx) return reconstructPath(parent, cols, startIdx, goalIdx);

            for (int[] d : DIRS) {
                int nx = cx + d[0], ny = cy + d[1];
                if (!map.inBounds(nx, ny) || !map.isWalkable(nx, ny)) continue;

                int nIdx = ny * cols + nx;
                if (closed[nIdx]) continue;

                int ng = gCost[cIdx] + 1;
                if (ng < gCost[nIdx]) {
                    gCost[nIdx]  = ng;
                    parent[nIdx] = cIdx;
                    int fCost    = ng + heuristic(nx, ny, goalCol, goalRow);
                    open.offer(new int[]{nx, ny, fCost});
                }
            }
        }

        return Collections.emptyList(); // no path found
    }

    // ------------------------------------------------------------------ //
    // Private helpers
    // ------------------------------------------------------------------ //

    private static int heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private static List<int[]> reconstructPath(int[] parent, int cols,
                                                int startIdx, int goalIdx) {
        List<int[]> path = new ArrayList<>();
        int cur = goalIdx;
        while (cur != startIdx) {
            int c = cur % cols;
            int r = cur / cols;
            path.add(0, new int[]{c, r});
            cur = parent[cur];
            if (cur == -1) return Collections.emptyList(); // broken chain
        }
        return path;
    }
}
