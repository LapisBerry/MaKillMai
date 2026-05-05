package com.lapisberry.net.packets;

import java.io.Serial;

/**
 * Sent by the target of an ATTACK die during AWAITING_DAMAGE_RESPONSE.
 * Choices: ACCEPT (take damage), TAKE_ROT (Bart — take 1 rot from pool
 * instead), DISCARD_ROT (Pedro — discard 1 own rot to negate).
 */
public class DamageResponsePacket extends ClientPacket {
    @Serial
    private static final long serialVersionUID = 1L;

    public enum Choice { ACCEPT, TAKE_ROT, DISCARD_ROT }

    private final Choice choice;

    public DamageResponsePacket(Choice choice) {
        this.choice = choice;
    }

    public Choice getChoice() {
        return choice;
    }

    @Override
    public String toString() {
        return "DamageResponsePacket{" + choice + "}";
    }
}
