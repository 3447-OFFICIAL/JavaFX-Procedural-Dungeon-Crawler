package engine;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.EnumMap;
import java.util.Map;

/**
 * Tracks keyboard state for the game.
 *
 * Key-held flags are used for continuous firing states.
 * "Move queuing" uses a single-consume flag per direction so the
 * player moves exactly one tile per key-press, avoiding the
 * 60-fps continuous-sliding issue.
 */
public class InputHandler {

    // Raw held state (for future abilities / running)
    private final Map<KeyCode, Boolean> held = new EnumMap<>(KeyCode.class);

    // Queued single-step move flags (consumed once per update)
    private boolean upQueued;
    private boolean downQueued;
    private boolean leftQueued;
    private boolean rightQueued;
    private boolean attackQueued;
    private boolean pauseQueued;

    // ------------------------------------------------------------------ //
    // JavaFX event entry points (wire these to Scene)
    // ------------------------------------------------------------------ //

    public void onKeyPressed(KeyEvent e) {
        KeyCode code = e.getCode();
        // Only queue if not already held (prevent key-repeat from queuing multiples)
        if (!isHeld(code)) {
            switch (code) {
                case W, UP    -> upQueued    = true;
                case S, DOWN  -> downQueued  = true;
                case A, LEFT  -> leftQueued  = true;
                case D, RIGHT -> rightQueued = true;
                case SPACE    -> attackQueued = true;
                case P        -> pauseQueued = true;
                default -> {}
            }
        }
        held.put(code, true);
    }

    public void onKeyReleased(KeyEvent e) {
        held.put(e.getCode(), false);
    }

    // ------------------------------------------------------------------ //
    // Consumed move queries — called by Player.handleMovement()
    // ------------------------------------------------------------------ //

    public boolean consumePause() {
        if (pauseQueued) {
            pauseQueued = false;
            return true;
        }
        return false;
    }

    /**
     * Returns true and clears the flag if the given direction was queued.
     * direction: "UP" | "DOWN" | "LEFT" | "RIGHT"
     */
    public boolean consumeMove(String direction) {
        return switch (direction) {
            case "UP"    -> consume("UP");
            case "DOWN"  -> consume("DOWN");
            case "LEFT"  -> consume("LEFT");
            case "RIGHT" -> consume("RIGHT");
            default -> false;
        };
    }

    private boolean consume(String dir) {
        switch (dir) {
            case "UP":    if (upQueued)    { upQueued    = false; return true; } break;
            case "DOWN":  if (downQueued)  { downQueued  = false; return true; } break;
            case "LEFT":  if (leftQueued)  { leftQueued  = false; return true; } break;
            case "RIGHT": if (rightQueued) { rightQueued = false; return true; } break;
        }
        return false;
    }

    public boolean consumeAttack() {
        if (attackQueued) {
            attackQueued = false;
            return true;
        }
        return false;
    }

    public boolean isHeld(KeyCode code) {
        return held.getOrDefault(code, false);
    }

    /** Clear all queued moves (e.g. on game-over / pause). */
    public void clearAll() {
        held.clear();
        upQueued = downQueued = leftQueued = rightQueued = attackQueued = false;
    }
}
