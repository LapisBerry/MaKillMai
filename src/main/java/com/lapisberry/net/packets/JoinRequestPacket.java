package com.lapisberry.net.packets;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code JoinRequestPacket} class is the first packet sending from client to server to join the server.
 */
public class JoinRequestPacket extends ClientPacket implements Serializable {
    @Serial
    private static final long serialVersionUID = -3120924882895484001L;
    // Fields
    private final String username;

    // Constructors
    public JoinRequestPacket(String username) {
        this.username = username;
    }

    // Getters
    public String getUsername() {
        return username;
    }
}
