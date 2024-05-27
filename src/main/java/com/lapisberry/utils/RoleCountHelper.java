package com.lapisberry.utils;

public final class RoleCountHelper {
    public static int getEmperors(int players) {
        if (4 <= players && players <= 8) return 1;
        return 0;
    }

    public static int getRoyalists(int players) {
        if (4 <= players && players <= 8) return TABLE[players - 4][2];
        return 0;
    }

    public static int getRebels(int players) {
        if (4 <= players && players <= 8) return TABLE[players - 4][1];
        return 0;
    }

    public static int getSpies(int players) {
        if (4 <= players && players <= 8) return TABLE[players - 4][0];
        return 0;
    }

    // Array of role count
    private static final int[][] TABLE = {// sp,rb,ry -> spy, rebel, royalist
            {1, 2, 0},// 4 players
            {1, 2, 1},// 5 players
            {1, 3, 1},// 6 players
            {1, 3, 2},// 7 players
            {2, 3, 2},// 8 players
            {2, 4, 2},// 9 players
            {2, 4, 3} // 10 players
    };
}
