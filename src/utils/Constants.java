package utils;

/**
 * Central constants for the dungeon crawler game.
 * Modify values here to scale or tune the game globally.
 */
public final class Constants {

    private Constants() {} // utility class, no instantiation

    // --- Grid ---
    public static final int MAP_COLS       = 40;
    public static final int MAP_ROWS       = 30;
    public static final int TILE_SIZE      = 24;  // pixels per tile

    // --- Window ---
    public static final int WINDOW_WIDTH   = MAP_COLS * TILE_SIZE;
    public static final int WINDOW_HEIGHT  = MAP_ROWS * TILE_SIZE + 60; // +60 for HUD

    // --- Dungeon Generation ---
    public static final int MIN_ROOMS      = 6;
    public static final int MAX_ROOMS      = 12;
    public static final int MIN_ROOM_SIZE  = 4;
    public static final int MAX_ROOM_SIZE  = 8;

    // --- Player ---
    public static final int PLAYER_START_HP  = 100;
    public static final int PLAYER_ATTACK    = 20;

    // --- Enemy ---
    public static final int ENEMY_START_HP   = 60;
    public static final int ENEMY_ATTACK     = 10;
    public static final int ENEMY_MOVE_DELAY_MS = 400;  // ms between enemy moves
    public static final int ENEMY_AGGRO_RANGE   = 15;   // tiles; beyond this enemy idles
    public static final int ENEMY_COUNT         = 4;    // enemies to spawn per dungeon

    // --- Colors (hex strings for Canvas context) ---
    public static final String COLOR_WALL       = "#1a1a2e";
    public static final String COLOR_FLOOR      = "#16213e";
    public static final String COLOR_CORRIDOR   = "#0f3460";
    public static final String COLOR_PLAYER     = "#4fc3f7";
    public static final String COLOR_ENEMY      = "#ef5350";
    public static final String COLOR_HUD_BG     = "#0d0d0d";
    public static final String COLOR_HUD_TEXT   = "#e0e0e0";
    public static final String COLOR_HP_BAR     = "#43a047";
    public static final String COLOR_HP_LOW     = "#e53935";
    public static final String COLOR_OVERLAY_BG = "rgba(0,0,0,0.7)";
}
