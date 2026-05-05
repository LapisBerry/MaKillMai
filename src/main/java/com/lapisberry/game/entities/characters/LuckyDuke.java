package com.lapisberry.game.entities.characters;

import java.io.Serial;

/** "You may make one extra re-roll (total of 3 re-rolls instead of 2)." */
public final class LuckyDuke extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public LuckyDuke() {
        super("Lucky Duke",
                "One extra re-roll (4 total rolls instead of 3).",
                8);
    }

    @Override
    public int getRollPerTurn() {
        return 4;
    }
}
