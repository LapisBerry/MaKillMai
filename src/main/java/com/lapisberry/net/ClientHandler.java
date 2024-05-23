package com.lapisberry.net;

import com.lapisberry.net.packets.ClientPacket;
import com.lapisberry.net.packets.JoinResponsePacket;
import com.lapisberry.net.packets.ServerPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    // Fields
    private final Server server;
    private final Socket socket;
    private final int clientId;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    // Constructors
    public ClientHandler(Server server, Socket socket, int clientId) {
        this.server = server;
        this.socket = socket;
        this.clientId = clientId;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("ClientHandler cannot be created.");
        }
    }

    // Methods
    @Override
    public void run() {
        // Send join response packet to client
        sendPacketToClient(new JoinResponsePacket(clientId));
        new Thread(this::startListeningClientPacket, "Listening client packet thread").start();
    }

    private void startListeningClientPacket() {
        while (!socket.isClosed()) {
            try {
                ClientPacket packet = (ClientPacket) inputStream.readObject();
                server.processPacketFromClient(this, packet);
            } catch (IOException e) {
                System.out.println("Client disconnected: " + socket.getInetAddress().getHostAddress());
                break;
            } catch (ClassNotFoundException e) {
                System.out.println("Packet from client cannot be read.");
            }
        }
        close();
    }

    public void sendPacketToClient(ServerPacket packet) {
        try {
            outputStream.writeObject(packet);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Packet cannot be sent to client.");
        }
    }

    public void close() {
        try {
            socket.close();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            System.out.println("ClientHandler cannot be closed.");
        }
        server.removeClientHandler(this);
    }

    // Getters Setters
    public Socket getSocket() {
        return socket;
    }

    public int getClientId() {
        return clientId;
    }
}