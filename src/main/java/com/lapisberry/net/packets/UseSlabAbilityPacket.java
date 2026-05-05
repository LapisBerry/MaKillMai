package com.lapisberry.net.packets;

import java.io.Serial;

/** Slab the Killer combo: pair HEALTH_POTION + ATTACK die into a single 2-damage hit. */
public class UseSlabAbilityPacket extends ClientPacket {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int beerDieIndex;
    private final int attackDieIndex;
    private final int targetClientId;

    public UseSlabAbilityPacket(int beerDieIndex, int attackDieIndex, int targetClientId) {
        this.beerDieIndex = beerDieIndex;
        this.attackDieIndex = attackDieIndex;
        this.targetClientId = targetClientId;
    }

    public int getBeerDieIndex() {
        return beerDieIndex;
    }

    public int getAttackDieIndex() {
        return attackDieIndex;
    }

    public int getTargetClientId() {
        return targetClientId;
    }

    @Override
    public String toString() {
        return "UseSlabAbilityPacket{beer=" + beerDieIndex + ", attack=" + attackDieIndex
                + ", target=" + targetClientId + "}";
    }
}
