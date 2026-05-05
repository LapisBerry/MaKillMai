package com.lapisberry.game.entities.characters;

import java.io.Serial;

/**
 * "When you resolve a HEALTH_POTION die you always heal as normal AND may
 * additionally remove 1 rot from any player (yourself included), or skip."
 */
public final class KitCarlson extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public KitCarlson() {
        super("Kit Carlson",
                "After resolving HEALTH_POTION, optionally remove 1 rot from any player.",
                7);
    }
}
