package app;

import app.components.JoinScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    // Scene
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setScene(new JoinScene());

        primaryStage.show();

        // This line will make the username field unfocused when started.
        primaryStage.getScene().getRoot().requestFocus();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
