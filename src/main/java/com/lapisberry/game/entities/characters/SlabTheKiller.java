package com.lapisberry.game.entities.characters;

import com.lapisberry.game.controllers.GameController;
import com.lapisberry.game.entities.players.Player;

import java.io.Serial;

/**
 * "Once per turn, you may pair a Beer (HEALTH_POTION) die with an ATTACK die
 * to deal 2 damage to one player instead of their normal effects."
 */
public final class SlabTheKiller extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean abilityUsedThisTurn;

    public SlabTheKiller() {
        super("Slab the Killer",
                "Once per turn, pair a HEALTH_POTION + an ATTACK die to deal 2 damage to one target.",
                8);
    }

    @Override
    public void onTurnStart(GameController gc, Player self) {
        abilityUsedThisTurn = false;
    }

    public boolean isAbilityUsedThisTurn() {
        return abilityUsedThisTurn;
    }

    public void markAbilityUsed() {
        abilityUsedThisTurn = true;
    }
}
