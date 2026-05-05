package com.lapisberry.net.packets;

import java.io.Serial;

/** Kit Carlson optional rot discard after resolving a HEALTH_POTION die. */
public class KitDiscardPacket extends ClientPacket {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int targetClientId;
    private final boolean skip;

    public KitDiscardPacket(int targetClientId, boolean skip) {
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
        return "KitDiscardPacket{target=" + targetClientId + ", skip=" + skip + "}";
    }
}
