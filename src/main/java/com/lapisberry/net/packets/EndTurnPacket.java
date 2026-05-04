package com.lapisberry.net.packets;

import java.io.Serial;

/** Client ends their turn after all dice have been resolved. */
public class EndTurnPacket extends ClientPacket {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "EndTurnPacket{}";
    }
}
