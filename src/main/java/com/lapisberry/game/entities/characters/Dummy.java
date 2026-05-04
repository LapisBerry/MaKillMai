package com.lapisberry.game.entities.characters;

import com.lapisberry.utils.Config;

import java.io.Serial;

public final class Dummy extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public Dummy() {
        super("Dummy", "I have no special ability", Config.DEFAULT_CHARACTER_HP);
    }
}
