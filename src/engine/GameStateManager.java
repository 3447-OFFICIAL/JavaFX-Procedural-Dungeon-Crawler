package engine;

/**
 * Holds the top-level game state.
 * Extend with additional states (PAUSED, VICTORY, MENU) as needed.
 */
public class GameStateManager {

    public enum State {
        RUNNING,
        GAME_OVER
    }

    private State current;
    private int   level = 1;

    public GameStateManager() {
        current = State.RUNNING;
    }

    public State getState()           { return current; }
    public boolean isRunning()        { return current == State.RUNNING; }

    public void setGameOver()         { current = State.GAME_OVER; }
    public void setRunning()          { current = State.RUNNING; }

    public int getLevel()             { return level; }
    public void nextLevel()           { level++; }

    /** Convenience reset. */
    public void reset() {
        current = State.RUNNING;
        level = 1;
    }
}
