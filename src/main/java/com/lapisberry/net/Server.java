package com.lapisberry.net;

import com.lapisberry.game.controllers.GameController;
import com.lapisberry.game.controllers.LobbyController;
import com.lapisberry.net.packets.*;
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

    /** Sends each connected client a {@link GameStatePacket} personalised to hide opponents' roles. */
    public void broadcastGameState() {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendPacketToClient(GameStatePacket.from(serverGame, clientHandler.getClientId()));
        }
    }

    public void processPacketFromClient(ClientHandler sender, ClientPacket packet) {
        System.out.println("Processing packet from " + sender.getSocket().getInetAddress().getHostAddress() + ": " + packet);

        if (packet instanceof JoinRequestPacket joinRequestPacket) {
            serverLobby.addPlayer(sender.getClientId(), joinRequestPacket.getUsername());
            sendPacketToAllClients(new LobbyPacket(serverLobby.getPlayers()));
            return;
        }
        if (packet instanceof PartyLeaderStartGamePacket) {
            serverLobby.setupShuffledPlayersAndShuffledCharacters();
            serverGame.startGame(serverLobby.getShuffledPlayers(), serverLobby.getShuffledCharacters());
            broadcastGameState();
            return;
        }

        // In-game actions
        boolean changed = false;
        if (packet instanceof RollDicePacket) {
            changed = serverGame.handleRoll(sender.getClientId());
        } else if (packet instanceof ToggleDieLockPacket toggle) {
            changed = serverGame.handleToggleLock(sender.getClientId(), toggle.getDieIndex());
        } else if (packet instanceof EndRollingPacket) {
            changed = serverGame.handleEndRolling(sender.getClientId());
        } else if (packet instanceof ResolveDiePacket resolve) {
            changed = serverGame.handleResolveDie(sender.getClientId(), resolve.getDieIndex(), resolve.getTargetClientId());
        } else if (packet instanceof UsePureMagicPacket pureMagic) {
            changed = serverGame.handleUsePureMagic(sender.getClientId(), pureMagic.isAccept());
        } else if (packet instanceof EndTurnPacket) {
            changed = serverGame.handleEndTurn(sender.getClientId());
        }

        if (changed) broadcastGameState();
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
        sendPacketToAllClients(new LobbyPacket(serverLobby.getPlayers()));
    }
}
