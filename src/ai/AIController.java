package ai;

import entities.Enemy;
import entities.Player;
import utils.Constants;
import world.MapGrid;

import java.util.List;

/**
 * AIController drives each Enemy's behaviour every game tick.
 *
 * Behaviour states:
 *  - IDLE  : enemy is too far from player; stays put.
 *  - CHASE : enemy uses A* to move towards player one tile per step.
 *  - ATTACK: enemy is adjacent (distance = 1); deals damage and waits.
 */
public class AIController {

    /**
     * Update a single enemy. Called once per game update tick.
     *
     * @param enemy  the enemy to update
     * @param player the player (target)
     * @param map    current map for pathfinding
     * @return true if the enemy attacked the player this tick
     */
    public boolean update(Enemy enemy, Player player, MapGrid map) {
        if (!enemy.isAlive() || !player.isAlive()) return false;
        if (!enemy.isReadyToMove()) return false;

        int dist = enemy.distanceTo(player);

        // --- ATTACK: adjacent ---
        if (dist == 1) {
            player.takeDamage(enemy.getAttackDamage());
            // Reset cooldown so attacks are also rate-limited
            enemy.applyMove(0, 0); // resets timer without moving
            return true;
        }

        // --- IDLE: out of aggro range ---
        if (dist > Constants.ENEMY_AGGRO_RANGE) return false;

        // --- CHASE: use A* to find next step ---
        List<int[]> path = Pathfinding.findPath(
                map,
                enemy.getCol(), enemy.getRow(),
                player.getCol(), player.getRow());

        if (path.isEmpty()) return false;

        int[] next = path.get(0);
        int nc = next[0];
        int nr = next[1];

        // Don't walk onto the player's tile (stop one step away)
        if (nc == player.getCol() && nr == player.getRow()) return false;

        // Confirm tile is still walkable (another enemy may occupy it — basic check)
        if (map.isWalkable(nc, nr)) {
            int dc = nc - enemy.getCol();
            int dr = nr - enemy.getRow();
            enemy.applyMove(dc, dr);
        }

        return false;
    }
}
