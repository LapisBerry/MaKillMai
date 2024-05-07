package utils;

import java.util.ArrayList;
import java.util.Collections;

public final class Randomizer {
    public static int getRandomInt(int bound) {
        return (int)(Math.random() * bound);
    }

    public static void shuffleArrayList(ArrayList<?> list) {
        Collections.shuffle(list);
    }
}
