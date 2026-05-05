package com.lapisberry.game.entities.characters;

import java.io.Serial;

/**
 * "At the start of your turn, you may heal 1 HP for any one player (including yourself).
 * The server enters a dedicated sub-phase so Sid can pick the target before rolling."
 */
public final class SidKetchum extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public SidKetchum() {
        super("Sid Ketchum",
                "At start of turn, choose any player to heal 1 HP (or skip).",
                8);
    }
}
