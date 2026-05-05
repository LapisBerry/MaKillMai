package com.lapisberry.game.entities.characters;

import java.io.Serial;

/**
 * "When you would take HP damage from an ATTACK die, you may discard 1 of
 * your own rot to negate it. Does not trigger on rot/Indian damage or Pure Magic."
 */
public final class PedroRamirez extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public PedroRamirez() {
        super("Pedro Ramirez",
                "When taking ATTACK damage, may discard 1 own rot to negate it.",
                8);
    }
}
