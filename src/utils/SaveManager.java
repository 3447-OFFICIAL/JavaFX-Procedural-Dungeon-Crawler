package utils;

import entities.Player;
import java.io.*;

/**
 * Handles saving and loading the game state to/from a file.
 * Demonstrates "File Handling" and "Exception Handling".
 */
public class SaveManager {

    private static final String SAVE_FILE = "dungeon_save.txt";

    /**
     * Saves the current game state.
     * Uses try-with-resources and catch blocks (Exception Handling).
     */
    public static void saveGame(int level, Player player) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE))) {
            writer.println("Level:" + level);
            writer.println("HP:" + player.getHp());
            writer.println("MaxHP:" + player.getMaxHp());
            System.out.println("Game saved successfully to " + SAVE_FILE);
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    /**
     * Loads the last saved level. Returns 1 if file doesn't exist.
     */
    public static int loadLevel() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null && line.startsWith("Level:")) {
                return Integer.parseInt(line.split(":")[1]);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading level: " + e.getMessage());
        }
        return 1;
    }
}
