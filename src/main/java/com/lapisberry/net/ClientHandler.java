package com.lapisberry.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    // Fields
    private final Server server;
    private final Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;


    // Constructors
    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
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
        new Thread(this::startListeningClientPacket, "Listening client packet thread").start();
    }

    private void startListeningClientPacket() {
        while (!socket.isClosed()) {
            try {
                Object packet = inputStream.readObject();
                server.processPacketFromClient(this, packet);
            } catch (IOException e) {
                System.out.println("Client disconnected: " + socket.getInetAddress().getHostAddress());
                break;
            } catch (ClassNotFoundException e) {
                System.out.println("Packet from client cannot be read.");
            }
        }
    }

    public void sendPacketToClient(Object packet) {
        try {
            outputStream.writeObject(packet);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Packet cannot be sent to client.");
        }
    }

    // Getters Setters
    public Socket getSocket() {
        return socket;
    }
}