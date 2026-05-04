package com.lapisberry.game.entities.characters;

import com.lapisberry.utils.Config;

import java.io.Serial;

/**
 * Phase 1 placeholder used for every character that doesn't yet have its
 * Bang!-style ability implemented. Carries the display name from the
 * {@link CharacterEnum} and the default HP. Future phases will replace it
 * with concrete subclasses that override behaviour per character.
 */
public final class GenericCharacter extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public GenericCharacter(String displayName) {
        super(displayName, "Special ability not yet implemented.", Config.DEFAULT_CHARACTER_HP);
    }
}
