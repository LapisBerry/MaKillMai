package com.lapisberry.net.packets;

import java.io.Serial;
import java.io.Serializable;

public class JoinResponsePacket extends ServerPacket implements Serializable {
    @Serial
    private static final long serialVersionUID = -5576292645379280835L;
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
