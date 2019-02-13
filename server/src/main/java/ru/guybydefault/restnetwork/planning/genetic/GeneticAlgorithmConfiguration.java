package ru.guybydefault.restnetwork.planning.genetic;

public class GeneticAlgorithmConfiguration {
    public final int populationSize;
    public final double mutationRate;
    public final double crossOverRate;
    public final int tournamentSelectionSize;
    public final int eliteSchedulesNum;


    public GeneticAlgorithmConfiguration(int populationSize, double mutationRate, double crossOverRate, int tournamentSelectionSize, int eliteSchedulesNum) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossOverRate = crossOverRate;
        this.tournamentSelectionSize = tournamentSelectionSize;
        this.eliteSchedulesNum = eliteSchedulesNum;
    }

    public static GeneticAlgorithmConfiguration buildDefaultParametrization() {
        return new GeneticAlgorithmConfiguration(9, 0.1, 0.9, 3, 1);
    }
}
