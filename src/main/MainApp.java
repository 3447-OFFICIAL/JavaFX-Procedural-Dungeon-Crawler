package main;

import engine.GameLoop;
import engine.InputHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import utils.Constants;

/**
 * JavaFX application entry point.
 *
 * Sets up:
 *  - The primary Stage and Scene
 *  - A full-window Canvas for rendering
 *  - InputHandler wired to the Scene's key events
 *  - GameLoop (owns all game logic)
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Start the web dashboard (Another Web Application!)
        utils.GameWebServer.startServerAsync();

        // --- Canvas ---
        Canvas canvas = new Canvas(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        // --- Input ---
        InputHandler input = new InputHandler();

        // --- Scene ---
        StackPane root = new StackPane(canvas);
        root.setStyle("-fx-background-color: #0d0d0d;");

        Scene scene = new Scene(root, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        scene.setOnKeyPressed(input::onKeyPressed);
        scene.setOnKeyReleased(input::onKeyReleased);

        // --- Game loop ---
        GameLoop loop = new GameLoop(canvas, input);

        // --- Stage ---
        primaryStage.setTitle("Dungeon Crawler — JavaFX");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // Request focus so key events are delivered immediately
        canvas.requestFocus();
        scene.getRoot().requestFocus();

        loop.start();

        // Clean shutdown
        primaryStage.setOnCloseRequest(e -> loop.stop());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
