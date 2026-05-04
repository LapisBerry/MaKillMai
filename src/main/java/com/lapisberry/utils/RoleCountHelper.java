package com.lapisberry.utils;

public final class RoleCountHelper {
    // Index by (players - MIN_PLAYERS). Columns: spies, rebels, royalists.
    private static final int[][] TABLE = {
            {1, 2, 0}, // 4 players
            {1, 2, 1}, // 5 players
            {1, 3, 1}, // 6 players
            {1, 3, 2}, // 7 players
            {2, 3, 2}, // 8 players
    };

    public static boolean isValidPlayerCount(int players) {
        return Config.MIN_PLAYERS <= players && players <= Config.MAX_PLAYERS;
    }

    public static int getEmperors(int players) {
        return isValidPlayerCount(players) ? 1 : 0;
    }

    public static int getRoyalists(int players) {
        return isValidPlayerCount(players) ? TABLE[players - Config.MIN_PLAYERS][2] : 0;
    }

    public static int getRebels(int players) {
        return isValidPlayerCount(players) ? TABLE[players - Config.MIN_PLAYERS][1] : 0;
    }

    public static int getSpies(int players) {
        return isValidPlayerCount(players) ? TABLE[players - Config.MIN_PLAYERS][0] : 0;
    }
}
