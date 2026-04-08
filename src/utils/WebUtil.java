package utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Demonstrates "Web Connectivity" by mocking a fetch from a remote API.
 */
public class WebUtil {

    /**
     * Attempts to fetch a "Daily Dungeon Seed" from a web service.
     */
    public static long fetchDailySeed() {
        System.out.println("Checking web for daily seed...");

        // Using HttpClient (Java 11+)
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com")) // Dummy URL for demo
                .GET()
                .build();

        try {
            // In a real app, this would be async or on a background thread
            // For demo purposes, we catch exceptions if offline
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Successfully connected to web service.");
                return System.currentTimeMillis() % 100000; // Use current time as mock seed
            }
        } catch (Exception e) {
            System.err.println("Web service unavailable (Offline?): " + e.getMessage());
        }

        return -1; // Fallback to procedural local seed
    }
}
