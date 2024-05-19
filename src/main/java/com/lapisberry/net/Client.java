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
    }
}
