package com.lapisberry.net.packets;

import java.io.Serial;

/** Client's accept/skip decision when 3+ PURE_MAGIC dice are pending. */
public class UsePureMagicPacket extends ClientPacket {
    @Serial
    private static final long serialVersionUID = 1L;

    private final boolean accept;

    public UsePureMagicPacket(boolean accept) {
        this.accept = accept;
    }

    public boolean isAccept() {
        return accept;
    }

    @Override
    public String toString() {
        return "UsePureMagicPacket{accept=" + accept + "}";
    }
}
