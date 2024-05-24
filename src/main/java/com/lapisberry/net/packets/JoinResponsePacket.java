package com.lapisberry.net.packets;

import java.io.Serial;

/**
 * The {@code JoinResponsePacket} class is the packet sending from server to client to response the {@code JoinRequestPacket}.
 */
public class JoinResponsePacket extends ServerPacket {
    @Serial
    private static final long serialVersionUID = 8792681250096995152L;
    // Fields
    private final int clientId;

    // Constructors
    public JoinResponsePacket(final int clientId) {
        this.clientId = clientId;
    }

    // Methods
    public int getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return "JoinResponsePacket{clientId=" + clientId + "}";
    }
}
