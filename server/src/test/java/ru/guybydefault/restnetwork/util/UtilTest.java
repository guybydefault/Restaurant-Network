package ru.guybydefault.restnetwork.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.guybydefault.restnetwork.util.Util;

import java.util.ArrayList;

public class UtilTest {

    int[][] from4To10Sum14Combinations = new int[][]{
            new int[]{4, 4, 6},
            new int[]{4, 5, 5},
            new int[]{4, 6, 4},
            new int[]{4, 10},
            new int[]{5, 4, 5},
            new int[]{5, 5, 4},
            new int[]{5, 9},
            new int[]{6, 4, 4},
            new int[]{6, 8},
            new int[]{7, 7},
            new int[]{8, 6},
            new int[]{9, 5},
            new int[]{10, 4}
    };

    @Test
    public void generateSumCombinations() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> {
                    ArrayList<int[]> res = Util.generateSumCombinations(0, 1, 1);
                });
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> {
                    ArrayList<int[]> res = Util.generateSumCombinations(1, 0, 1);
                });
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> {
                    ArrayList<int[]> res = Util.generateSumCombinations(0, 1, 0);
                });

        Assertions.assertEquals(Util.generateSumCombinations(1, 1, 1).size(), 1);
        Assertions.assertArrayEquals(Util.generateSumCombinations(1, 1, 1).get(0), new int[]{1});
        Assertions.assertNotEquals(Util.generateSumCombinations(4, 10, 14).size(), 0);

        ArrayList<int[]> res = Util.generateSumCombinations(4, 10, 14);
        Assertions.assertEquals(res.size(), from4To10Sum14Combinations.length);
        for (int i = 0; i < from4To10Sum14Combinations.length; i++) {
            Assertions.assertArrayEquals(from4To10Sum14Combinations[i], res.get(i));
        }
    }
}