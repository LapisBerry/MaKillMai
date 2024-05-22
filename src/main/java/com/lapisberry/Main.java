package com.lapisberry;

import com.lapisberry.gui.scenes.JoinScene;
import com.lapisberry.net.Server;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage;
    private static Server server;

    // methods
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("MaKillMai");
        primaryStage.getIcons().add(new Image(ClassLoader.getSystemResource("images/makillmai-icon.png").toString()));
        primaryStage.setScene(new JoinScene());

        primaryStage.show();

        // This line will make the username field unfocused when started.
        primaryStage.getScene().getRoot().requestFocus();
    }

    public static void createServer() {
        server = new Server();
        new Thread(server, "Server thread").start();
    }

    public static void closeServer() {
        server.close();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}