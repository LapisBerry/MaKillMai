package com.lapisberry.net;

import com.lapisberry.game.controllers.GameController;
import com.lapisberry.game.controllers.LobbyController;
import com.lapisberry.net.packets.ClientPacket;
import com.lapisberry.net.packets.JoinRequestPacket;
import com.lapisberry.net.packets.LobbyPacket;
import com.lapisberry.net.packets.ServerPacket;
import com.lapisberry.utils.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {
    // Fields
    private final ServerSocket serverSocket;
    private final ArrayList<ClientHandler> clientHandlers;
    private final LobbyController serverLobby;
    private final GameController serverGame;
    private int clientIdCounter = 0;

    // Constructors
    public Server() {
        try {
            this.serverSocket = new ServerSocket(Config.PORT);
        } catch (IOException e) {
            throw new RuntimeException("Server cannot be created.");
        }
        clientHandlers = new ArrayList<>();
        serverLobby = new LobbyController();
        serverGame = new GameController();
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
                ClientHandler clientHandler = new ClientHandler(this, socket, clientIdCounter++);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();

                System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());
            } catch (IOException e) {
                System.out.println("Server cannot accept client anymore. Server is closed.");
            }
        }
    }

    public void sendPacketToAllClients(ServerPacket packet) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendPacketToClient(packet);
        }
    }

    public void processPacketFromClient(ClientHandler sender, ClientPacket packet) {
        System.out.println("Processing packet from " + sender.getSocket().getInetAddress().getHostAddress() + ": " + packet);
        if (packet instanceof JoinRequestPacket joinRequestPacket) {
            serverLobby.addPlayer(sender.getClientId(), joinRequestPacket.getUsername());
            sendPacketToAllClients(new LobbyPacket(serverLobby));
        }
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Server cannot be closed.");
        }
    }

    public void removeClientHandler(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        serverLobby.removePlayer(clientHandler.getClientId());
        sendPacketToAllClients(new LobbyPacket(serverLobby));
    }
}