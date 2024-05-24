package com.lapisberry.game.entities.characters;

import java.io.Serial;

public final class Dummy extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = -5339093653311951978L;

    public Dummy() {
        super("Dummy", "I have no special ability", 10);
    }
}
