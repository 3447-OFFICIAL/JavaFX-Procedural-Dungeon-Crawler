package utils;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * An embedded HTTP server that serves a high score dashboard.
 * Demonstrates "Web Application", "Multithreading", and "Database Connectivity".
 */
public class GameWebServer {

    private static final int PORT = 8080;

    public static void startServerAsync() {
        Thread serverThread = new Thread(() -> {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
                server.createContext("/", new DashboardHandler());
                server.setExecutor(null); // Use default executor
                server.start();
                System.out.println("[Web Server] Dashboard started at http://localhost:" + PORT);
            } catch (IOException e) {
                System.err.println("Could not start web server: " + e.getMessage());
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
    }

    static class DashboardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            List<String> scores = LeaderboardDB.getTopScores();
            
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html><html><head>");
            html.append("<title>Dungeon Crawler Dashboard</title>");
            html.append("<style>");
            html.append("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #1a1a1a; color: #eee; text-align: center; padding: 50px; }");
            html.append("h1 { color: #ffcc00; text-shadow: 2px 2px #000; }");
            html.append(".score-list { background: #333; padding: 20px; border-radius: 10px; display: inline-block; min-width: 300px; box-shadow: 0 0 20px rgba(0,0,0,0.5); }");
            html.append(".score-item { padding: 10px; border-bottom: 1px solid #444; font-size: 1.2em; }");
            html.append(".score-item:last-child { border-bottom: none; }");
            html.append("footer { margin-top: 30px; font-size: 0.8em; color: #888; }");
            html.append("</style></head><body>");
            
            html.append("<h1>🏰 Dungeon Leaderboard</h1>");
            html.append("<div class='score-list'>");
            
            if (scores.isEmpty()) {
                html.append("<div class='score-item'>No scores yet. Go play!</div>");
            } else {
                for (String score : scores) {
                    html.append("<div class='score-item'>").append(score).append("</div>");
                }
            }
            
            html.append("</div>");
            html.append("<footer>Dashboard provided by Embedded GameWebServer</footer>");
            html.append("</body></html>");

            byte[] response = html.toString().getBytes();
            t.sendResponseHeaders(200, response.length);
            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }
}
