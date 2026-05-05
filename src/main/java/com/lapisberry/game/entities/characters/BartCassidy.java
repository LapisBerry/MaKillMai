package com.lapisberry.game.entities.characters;

import java.io.Serial;

/**
 * "When you would take HP damage from an ATTACK die, you may instead take 1 rot.
 * Does not trigger on rot/Indian damage or Pure Magic."
 */
public final class BartCassidy extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public BartCassidy() {
        super("Bart Cassidy",
                "When taking ATTACK damage, may take 1 rot instead.",
                8);
    }
}
