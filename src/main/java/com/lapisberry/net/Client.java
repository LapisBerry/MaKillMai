package com.lapisberry.net;

import com.lapisberry.net.packets.ClientPacket;
import com.lapisberry.net.packets.JoinRequestPacket;
import com.lapisberry.net.packets.ServerPacket;
import com.lapisberry.utils.Config;
import com.lapisberry.utils.exceptions.ConnectionRefusedException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {
    // Fields
    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

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
                System.out.println("Packet received from server: " + packet);
            } catch (IOException e) {
                System.out.println("Server disconnected.");
            } catch (ClassNotFoundException | ClassCastException e) {
                System.out.println("Packet from server cannot be read.");
            }
        }
    }

    private void sendPacketToServer(ClientPacket packet) {
        try {
            outputStream.writeObject(packet);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Packet cannot be sent to server.");
        }
    }

    public void sendJoinRequestPacket(String username) {
        sendPacketToServer(new JoinRequestPacket(username));
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
}
