package com.lapisberry.net.packets;

import java.io.Serial;

/** Client asks the server to roll all unlocked dice. */
public class RollDicePacket extends ClientPacket {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "RollDicePacket{}";
    }
}
