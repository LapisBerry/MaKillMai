package net;

import utils.ApplicationConfig;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    // Fields
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;


    // Constructors
    public Client() {
        try {
            socket = new Socket("localhost", ApplicationConfig.PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            System.out.println("Client cannot be created.");
        }
    }


    // Methods
}
