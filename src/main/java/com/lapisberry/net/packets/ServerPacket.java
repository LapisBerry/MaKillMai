package com.lapisberry.net.packets;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code ServerPacket} class is a packet sending from server to client.
 */
public abstract class ServerPacket implements Serializable {
    @Serial
    private static final long serialVersionUID = -9199352633046687573L;
}
