package net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {
    // Fields
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    // Constructors


    // Methods
    @Override
    public void run() {
    }
}
