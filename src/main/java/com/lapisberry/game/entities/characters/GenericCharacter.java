package com.lapisberry.game.entities.characters;

import java.io.Serial;

/**
 * Placeholder used for characters whose ability hasn't been wired up yet.
 * Carries the display name + HP from the {@link CharacterEnum} so the rest
 * of the game still runs.
 */
public final class GenericCharacter extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public GenericCharacter(String displayName, int hp) {
        super(displayName, "Special ability not yet implemented.", hp);
    }
}
