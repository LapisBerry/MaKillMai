package com.lapisberry;

import com.lapisberry.gui.scenes.JoinScene;
import com.lapisberry.gui.scenes.LobbyScene;
import com.lapisberry.net.Client;
import com.lapisberry.net.Server;
import com.lapisberry.utils.exceptions.ConnectionRefusedException;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage;
    private static Server server;
    private static Client client;

    // methods
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("MaKillMai");
        primaryStage.getIcons().add(new Image(ClassLoader.getSystemResource("images/makillmai-icon.png").toString()));
        goToJoinScene();
        primaryStage.setOnCloseRequest(event -> {
            closeServer();
            closeClient();
            System.exit(0);
        });

        primaryStage.show();

        // This line will make the username field unfocused when started.
        primaryStage.getScene().getRoot().requestFocus();
    }

    public static void goToJoinScene() {
        primaryStage.setScene(new JoinScene());
    }

    public static void goToLobbyScene() {
        primaryStage.setScene(new LobbyScene());
    }

    public static void createServer() {
        server = new Server();
        new Thread(server, "Server thread").start();
    }

    public static void closeServer() {
        if (server != null) server.close();
    }

    public static void createClient(String host) {
        client = new Client(host);
        new Thread(client, "Client thread").start();
    }

    public static void closeClient() {
        if (client != null) client.close();
    }

    // Getters Setters
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Client getClient() {
        return client;
    }
}