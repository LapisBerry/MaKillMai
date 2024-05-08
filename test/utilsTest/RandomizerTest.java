package utilsTest;

import org.junit.jupiter.api.Test;
import utils.Randomizer;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomizerTest {
    @Test
    void getRandomInt() {
        for (int i = 1; i <= 100; i++) {
            int randomInt = Randomizer.getRandomInt(i);
            assertTrue(0 <= randomInt && randomInt < i);
        }
    }

    @Test
    void shuffleArrayList() {
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
        ArrayList<String> originalArrayList = new ArrayList<>(arrayList);
        Randomizer.shuffleArrayList(arrayList);
        assertEquals(4, arrayList.size());
        for (String s : originalArrayList) {
            assertTrue(arrayList.contains(s));
        }
    }
}
