package ru.guybydefault.restnetwork.util;

import ru.guybydefault.restnetwork.entity.Cook;

import java.util.ArrayList;
import java.util.List;

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

    public static <T> List<List<T>> generateCombinations(List<T> elements, int combinationSize) {
        if (combinationSize <= 0 || combinationSize > elements.size()) {
            throw new IllegalArgumentException("combinationSize out of bounds");
        }
        List<List<T>> result = new ArrayList<>();
        generateCombinations(elements, 0, combinationSize, new ArrayList<>(), result);
        return result;
    }

    private static <T> void generateCombinations(List<T> elements, int index, int combinationSize, List<T> combination, List<List<T>> result) {
        if (combination.size() == combinationSize) {
            result.add(combination);
        } else {
            for (int i = index; i < elements.size(); i++) {
                List<T> newList = new ArrayList<T>(combination);
                newList.add(elements.get(i));
                generateCombinations(elements, i + 1, combinationSize, newList, result);
            }
        }
    }

    public static <T> List<List<T>> generatePermutations(List<T> elements, int permutationSize) {
        if (permutationSize <= 0 || permutationSize > elements.size()) {
            throw new IllegalArgumentException("permutationSize out of bounds");
        }
        List<List<T>> result = new ArrayList<>();
        generatePermutations(elements, permutationSize, new ArrayList<T>(), result);
        return result;
    }

    private static <T> void generatePermutations(List<T> elements, int permutationSize, List<T> permutation, List<List<T>> result) {
        if (permutation.size() == permutationSize) {
            result.add(permutation);
        } else {
            for (T t : elements) {
                if (permutation.contains(t)) {
                    continue;
                }
                List<T> newList = new ArrayList<>(permutation);
                newList.add(t);
                generatePermutations(elements, permutationSize, newList, result);
            }
        }
    }
}
