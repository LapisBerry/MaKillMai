package com.lapisberry.net.packets;

import java.io.Serial;

/** Client voluntarily stops rolling early; rolling phase ends and resolution begins. */
public class EndRollingPacket extends ClientPacket {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "EndRollingPacket{}";
    }
}
