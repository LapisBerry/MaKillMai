package com.lapisberry.game.entities.characters;

import java.util.function.Supplier;

/**
 * Each entry maps to its display name, base HP, and a factory that produces
 * the runtime {@link BaseCharacter} instance.
 */
public enum CharacterEnum {
    Bart_Cassidy(8, BartCassidy::new),
    Black_Jack(8, BlackJack::new),
    Calamity_Janet(8, CalamityJanet::new),
    El_Gringo(7, ElGringo::new),
    Jesse_Jones(9, JesseJones::new),
    Jourdonnais(7, Jourdonnais::new),
    Kit_Carlson(7, KitCarlson::new),
    Lucky_Duke(8, LuckyDuke::new),
    Paul_Regret(9, PaulRegret::new),
    Pedro_Ramirez(8, PedroRamirez::new),
    Rose_Doolan(9, RoseDoolan::new),
    Sid_KetChum(8, SidKetchum::new),
    Slab_The_Killer(8, SlabTheKiller::new),
    Suzy_Lafayette(8, SuzyLafayette::new),
    Vulture_Sam(9, VultureSam::new),
    Willy_The_Kid(8, WillyTheKid::new);

    private final int baseHp;
    private final Supplier<BaseCharacter> factory;

    CharacterEnum(int baseHp, Supplier<BaseCharacter> factory) {
        this.baseHp = baseHp;
        this.factory = factory;
    }

    public String getDisplayName() {
        return name().replace('_', ' ');
    }

    public int getBaseHp() {
        return baseHp;
    }

    public BaseCharacter createCharacter() {
        return factory.get();
    }
}
