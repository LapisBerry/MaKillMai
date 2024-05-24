package com.lapisberry.net;

import com.lapisberry.Main;
import com.lapisberry.game.controllers.GameController;
import com.lapisberry.game.controllers.LobbyController;
import com.lapisberry.gui.scenes.LobbyScene;
import com.lapisberry.net.packets.ClientPacket;
import com.lapisberry.net.packets.JoinResponsePacket;
import com.lapisberry.net.packets.LobbyPacket;
import com.lapisberry.net.packets.ServerPacket;
import com.lapisberry.utils.Config;
import com.lapisberry.utils.exceptions.ConnectionRefusedException;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {
    // Fields
    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final LobbyController clientLobby;
    private final GameController clientGame;
    private int clientId;

    // Constructors
    public Client(final String host) {
        try {
            this.socket = new Socket(host, Config.PORT);
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Client cannot be created.");
            throw new ConnectionRefusedException("Connection refused.");
        }
        clientLobby = new LobbyController();
        clientGame = new GameController();
    }

    // Methods
    @Override
    public void run() {
        new Thread(this::startListeningServerPacket, "Listening server packet thread").start();
    }

    private void startListeningServerPacket() {
        while (!socket.isClosed()) {
            try {
                ServerPacket packet = (ServerPacket) inputStream.readObject();
                processPacketFromServer(packet);
            } catch (IOException e) {
                System.out.println("Server disconnected.");
                break;
            } catch (ClassNotFoundException | ClassCastException e) {
                System.out.println("Packet from server cannot be read.");
            }
        }
        close();
        Platform.runLater(Main::goToServerDisconnectScene);
    }

    private void processPacketFromServer(ServerPacket packet) {
        System.out.println("Processing packet from server: " + packet);
        if (packet instanceof JoinResponsePacket joinResponsePacket) {
            setClientId(joinResponsePacket.getClientId());
        } else if (packet instanceof LobbyPacket lobbyPacket) {
            clientLobby.setPlayers(lobbyPacket.getPlayers());
            LobbyScene.updatePlayerList(clientLobby);
        }
    }

    public void sendPacketToServer(ClientPacket packet) {
        try {
            outputStream.writeObject(packet);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Packet cannot be sent to server.");
        }
    }

    public void close() {
        try {
            socket.close();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            System.out.println("Client cannot be closed.");
        }
    }

    // Getters Setters
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
