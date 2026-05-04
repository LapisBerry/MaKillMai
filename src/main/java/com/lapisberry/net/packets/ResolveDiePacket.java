package com.lapisberry.net.packets;

import java.io.Serial;

/** Client resolves an attack/heal die by picking the target's clientId. */
public class ResolveDiePacket extends ClientPacket {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int dieIndex;
    private final int targetClientId;

    public ResolveDiePacket(int dieIndex, int targetClientId) {
        this.dieIndex = dieIndex;
        this.targetClientId = targetClientId;
    }

    public int getDieIndex() {
        return dieIndex;
    }

    public int getTargetClientId() {
        return targetClientId;
    }

    @Override
    public String toString() {
        return "ResolveDiePacket{dieIndex=" + dieIndex + ", target=" + targetClientId + "}";
    }
}
