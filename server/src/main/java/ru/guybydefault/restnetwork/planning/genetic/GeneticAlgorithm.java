package ru.guybydefault.restnetwork.planning;

import java.util.ArrayList;

public class GeneticAlgorithm {

    private PlanningData data;

    private GeneticAlgorithmConfiguration configuration;

    public GeneticAlgorithm(PlanningData data, GeneticAlgorithmConfiguration configuration) {
        this.data = data;
        this.configuration = configuration;
    }

    public Population evolve(Population population) {
        // TODO is sortByScore required?
        return mutatePopulation(crossOverPopulation(population).sortByScore());
    }


    private Population selectTournamentPopulation(Population population) {
        Population tournamentPopulation = new Population(configuration.tournamentSelectionSize, data);
        for (int i = 0; i < configuration.tournamentSelectionSize; i++) {
            tournamentPopulation.getRosterSolutionList().add(population.getRosterSolutionList().get((int) (population.getSize() * Math.random())));
        }
        return tournamentPopulation;
    }

    private RosterSolution crossOverRosterSolution(RosterSolution s1, RosterSolution s2) {
        // TODO optimization initialize is not required but we need to make sure that arraylist is initialized for required size
        RosterSolution crossoverSolution = new RosterSolution(data).initialize();
        for (int i = 0; i < crossoverSolution.getShiftDayList().size(); i++) {
//            if (Math.random() > 0.5) {
//                crossoverSolution.getShiftDayList().set(i, s1.getShiftDayList().get(i));
//            } else {
//                crossoverSolution.getShiftDayList().set(i, s2.getShiftDayList().get(i));
//            }
            if (s1.getShiftDayList().get(i).size() == crossoverSolution.getShiftDayList().get(i).size()) {
                if (s2.getShiftDayList().get(i).size() == crossoverSolution.getShiftDayList().get(i).size()) {
                    for (int k = 0; k < crossoverSolution.getShiftDayList().get(i).size(); k++) {
                        if (Math.random() > 0.5) {
                            crossoverSolution.getShiftDayList().get(i).set(k, s1.getShiftDayList().get(i).get(k));
                        } else {
                            crossoverSolution.getShiftDayList().get(i).set(k, s2.getShiftDayList().get(i).get(k));
                        }
                    }
                } else {
                    for (int k = 0; k < crossoverSolution.getShiftDayList().get(i).size(); k++) {
                        crossoverSolution.getShiftDayList().get(i).set(k, s1.getShiftDayList().get(i).get(k));
                    }
                }
            } else if (s2.getShiftDayList().get(i).size() == crossoverSolution.getShiftDayList().get(i).size()) {
                for (int k = 0; k < crossoverSolution.getShiftDayList().get(i).size(); k++) {
                    crossoverSolution.getShiftDayList().get(i).set(k, s2.getShiftDayList().get(i).get(k));
                }
            } else {
                if (Math.random() > 0.5) {
                    crossoverSolution.getShiftDayList().set(i, s1.getShiftDayList().get(i));
                } else {
                    crossoverSolution.getShiftDayList().set(i, s2.getShiftDayList().get(i));
                }
            }
        }
        return crossoverSolution;
    }

    // Assuming population is already sorted by score
    private Population crossOverPopulation(Population population) {
        Population crossOverPopulation = new Population(population.getSize(), data);
        for (int i = 0; i < configuration.eliteSchedulesNum; i++) {
            crossOverPopulation.getRosterSolutionList().set(i, population.getRosterSolutionList().get(i));
        }
        for (int i = configuration.eliteSchedulesNum; i < population.getSize(); i++) {
            if (configuration.crossOverRate > Math.random()) {
                RosterSolution solution1 = selectTournamentPopulation(population).sortByScore().getRosterSolutionList().get(0);
                RosterSolution solution2 = selectTournamentPopulation(population).sortByScore().getRosterSolutionList().get(0);
                RosterSolution crossOverRosterSolution = crossOverRosterSolution(solution1, solution2);
                crossOverPopulation.getRosterSolutionList().set(i, crossOverRosterSolution);
            } else {
                crossOverPopulation.getRosterSolutionList().set(i, population.getRosterSolutionList().get(i));
            }
        }
        return crossOverPopulation;
    }


    private Population mutatePopulation(Population population) {
        Population mutatePopulation = new Population(population.getSize(), data);
        ArrayList<RosterSolution> rosterSolutions = mutatePopulation.getRosterSolutionList();
        for (int i = 0; i < configuration.eliteSchedulesNum; i++) {
            mutatePopulation.getRosterSolutionList().set(i, population.getRosterSolutionList().get(i));
        }
        for (int i = configuration.eliteSchedulesNum; i < population.getSize(); i++) {
            rosterSolutions.set(i, mutateRosterSolution(population.getRosterSolutionList().get(i)));
        }
        return mutatePopulation;
    }

    private RosterSolution mutateRosterSolution(RosterSolution mutateSolution) {
        RosterSolution solution = new RosterSolution(data).initialize();
        for (int i = 0; i < mutateSolution.getShiftDayList().size(); i++) {
            if (configuration.mutationRate > Math.random()) {
                // TODO set changed?
                mutateSolution.getShiftDayList().set(i, solution.getShiftDayList().get(i));
            }
        }
        return mutateSolution;
    }


}
