package com.lapisberry.net.packets;

import java.io.Serial;

/** Sid Ketchum picks a player to heal +1 HP at the start of their turn, or skips. */
public class SidHealPacket extends ClientPacket {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int targetClientId;
    private final boolean skip;

    public SidHealPacket(int targetClientId, boolean skip) {
        this.targetClientId = targetClientId;
        this.skip = skip;
    }

    public int getTargetClientId() {
        return targetClientId;
    }

    public boolean isSkip() {
        return skip;
    }

    @Override
    public String toString() {
        return "SidHealPacket{target=" + targetClientId + ", skip=" + skip + "}";
    }
}
