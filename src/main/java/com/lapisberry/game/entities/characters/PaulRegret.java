package com.lapisberry.game.entities.characters;

import java.io.Serial;

/** "You never lose life points from Gatling Gun results." */
public final class PaulRegret extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public PaulRegret() {
        super("Paul Regret",
                "You never lose life points from Pure Magic.",
                9);
    }

    @Override
    public int onPureMagicDamage(int amount) {
        return 0;
    }
}
