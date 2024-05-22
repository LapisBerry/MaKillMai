package com.lapisberry.net;

import com.lapisberry.utils.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {
    // Fields
    private final ServerSocket serverSocket;
    private final ArrayList<ClientHandler> clientHandlers;

    // Constructors
    public Server() {
        try {
            this.serverSocket = new ServerSocket(Config.PORT);
        } catch (IOException e) {
            throw new RuntimeException("Server cannot be created.");
        }
        clientHandlers = new ArrayList<>();
    }

    // Methods
    @Override
    public void run() {
        new Thread(this::startAcceptingClients, "Accepting clients thread").start();
    }

    private void startAcceptingClients() {
        System.out.println("Start accepting clients...");
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
    }

    public void processPacketFromClient(ClientHandler sender, Object packet) {
        System.out.println("Processing packet from " + sender.getSocket().getInetAddress().getHostAddress() + ": " + packet);
    }

    private void sendPacketToAllClients(Object packet) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendPacketToClient(packet);
        }
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Server cannot be closed.");
        }
    }
}