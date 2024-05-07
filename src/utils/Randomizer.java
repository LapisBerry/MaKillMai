package utils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The {@code Randomizer} class
 * <p>
 *     This class provides static methods for randomizing things.
 * <p>
 *     Like shuffling an {@code ArrayList} or getting a random integer.
 */
public final class Randomizer {
    public static int getRandomInt(int bound) {
        return (int)(Math.random() * bound);
    }

    public static void shuffleArrayList(ArrayList<?> list) {
        Collections.shuffle(list);
    }
}
