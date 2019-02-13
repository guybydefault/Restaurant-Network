package ru.guybydefault.restnetwork.util;

import java.util.ArrayList;
import java.util.Arrays;

public class Util {


    public static ArrayList<int[]> generateSumCombinations(int min, int max, int targetSum) {
        if (min <= 0 || max < min || targetSum < max) {
            throw new IllegalArgumentException("violation of constraint: targetSum >= max >= min > 0");
        }
        ArrayList<int[]> result = new ArrayList<int[]>();
        generateSumCombinations(min, max, targetSum, new ArrayList<Integer>(), result);
        return result;
    }

    /**
     * Return list of all possible sums = <strong>sum</strong>
     *
     * @param targetSum
     * @return
     */
    private static void generateSumCombinations(int min, int max,
                                                int targetSum,
                                                ArrayList<Integer> partial,
                                                ArrayList<int[]> result) {
        final int s = partial.stream().mapToInt(el -> el.intValue()).sum();
        for (int i = min; i <= max; i++) {
            ArrayList<Integer> newPartial = new ArrayList<>(partial);
            newPartial.add(i);
            int newTarget = targetSum - s - i;
            if (newTarget == 0) {
                result.add(newPartial.stream().mapToInt(el -> el.intValue()).toArray());
                return;
            }
            int newMax = max > newTarget ? newTarget : max;
            if (min <= newMax) {
                generateSumCombinations(min, newMax, targetSum, newPartial, result);
            }
        }

    }
}
