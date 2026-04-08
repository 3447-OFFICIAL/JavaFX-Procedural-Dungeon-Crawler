package utils;

import java.sql.*;

/**
 * Handles communication with a local SQLite database.
 * Demonstrates "Database Connectivity" and "Multithreading".
 */
public class LeaderboardDB {

    private static final String DB_URL = "jdbc:sqlite:leaderboard.db";

    static {
        // Initialize the database table
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS high_scores (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "level INTEGER NOT NULL," +
                         "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("DB Init Error: " + e.getMessage());
        }
    }

    /**
     * Saves a score to the database on a background thread.
     * Demonstrates "Multithreading".
     */
    public static void saveScoreAsync(int level) {
        Thread thread = new Thread(() -> {
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement("INSERT INTO high_scores(level) VALUES(?)")) {
                pstmt.setInt(1, level);
                pstmt.executeUpdate();
                System.out.println("[DB Thread] High score saved: Level " + level);
            } catch (SQLException e) {
                System.err.println("Database Error: " + e.getMessage());
            }
        });
        thread.setDaemon(true); // Don't block app exit
        thread.start();
    }

    /**
     * Gets the top 10 scores from the database.
     * Used by the Web Application Dashboard.
     */
    public static java.util.List<String> getTopScores() {
        java.util.List<String> scores = new java.util.ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT level, timestamp FROM high_scores ORDER BY level DESC LIMIT 10")) {
            while (rs.next()) {
                scores.add("Level " + rs.getInt("level") + " (" + rs.getString("timestamp") + ")");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching top scores: " + e.getMessage());
        }
        return scores;
    }
}
