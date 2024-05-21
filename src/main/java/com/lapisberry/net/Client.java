package com.lapisberry.net;

import com.lapisberry.utils.Config;

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
            throw new RuntimeException("Client cannot be created.");
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
                Object packet = inputStream.readObject();
                System.out.println("Packet received from server: " + packet);
            } catch (IOException e) {
                System.out.println("Server disconnected.");
                break;
            } catch (ClassNotFoundException e) {
                System.out.println("Packet from server cannot be read.");
            }
        }
    }

    private void sendPacketToServer(Object packet) {
        try {
            outputStream.writeObject(packet);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Packet cannot be sent to server.");
        }
    }
}
