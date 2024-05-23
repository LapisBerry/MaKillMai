package com.lapisberry.net.packets;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code ClientPacket} class is a packet sending from client to server.
 */
public abstract class ClientPacket implements Serializable {
    @Serial
    private static final long serialVersionUID = -2245864016631194615L;
}
