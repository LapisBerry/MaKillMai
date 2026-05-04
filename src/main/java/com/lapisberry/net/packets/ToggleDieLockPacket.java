package com.lapisberry.net.packets;

import java.io.Serial;

/** Client toggles the locked-state of a single die during the rolling phase. */
public class ToggleDieLockPacket extends ClientPacket {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int dieIndex;

    public ToggleDieLockPacket(int dieIndex) {
        this.dieIndex = dieIndex;
    }

    public int getDieIndex() {
        return dieIndex;
    }

    @Override
    public String toString() {
        return "ToggleDieLockPacket{dieIndex=" + dieIndex + "}";
    }
}
