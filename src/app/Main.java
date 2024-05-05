package app;

import app.components.JoinPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        JoinPane root = new JoinPane();
        Scene scene = new Scene(root, 1280, 800);

        scene.setOnKeyPressed(e -> {
            if (Objects.requireNonNull(e.getCode()) == KeyCode.F11) {
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }
        });

        primaryStage.fullScreenExitHintProperty().setValue("Press F11 to exit full screen mode.");
        primaryStage.setScene(scene);
        primaryStage.show();

        // This line will make the username field unfocused when started.
        root.requestFocus();
    }
}
