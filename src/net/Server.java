package net;

import utils.ApplicationConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    // Fields
    private final ServerSocket serverSocket;
    private final ArrayList<ClientHandler> clientHandlers;

    public static void main(String[] args) {
        Server server = new Server();
        server.startAcceptingClients();
    }

    // Constructors
    public Server() {
        try {
            this.serverSocket = new ServerSocket(ApplicationConfig.PORT);
        } catch (IOException e) {
            throw new RuntimeException("Server cannot be created.");
        }
        clientHandlers = new ArrayList<>();
    }


    // Methods
    private void startAcceptingClients() {
        System.out.println("Start accepting clients...");
        new Thread(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    Socket socket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(this, socket);
                    clientHandlers.add(clientHandler);

                    System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());
                    new Thread(clientHandler).start();
                } catch (IOException e) {
                    System.out.println("Server cannot accept client anymore. Server is closed.");
                }
            }
        }).start();
    }
}
