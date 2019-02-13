package ru.guybydefault.restnetwork.planning.genetic;

public class GeneticAlgorithm {

//    private PlanningData data;
//
//    private GeneticAlgorithmConfiguration configuration;
//
//    public GeneticAlgorithm(PlanningData data, GeneticAlgorithmConfiguration configuration) {
//        this.data = data;
//        this.configuration = configuration;
//    }
//
//    public Population evolve(Population population) {
//        return mutatePopulation(crossOverPopulation(population).sortByScore());
//    }
//
//
//    private Population selectTournamentPopulation(Population population) {
//        Population tournamentPopulation = new Population(configuration.tournamentSelectionSize, data);
//        for (int i = 0; i < configuration.tournamentSelectionSize; i++) {
//            tournamentPopulation.getRosterIncrementSolutionList().add(population.getRosterIncrementSolutionList().get((int) (population.getSize() * Math.random())));
//        }
//        return tournamentPopulation;
//    }
//
//    private RosterIncrementSolution crossOverRosterSolution(RosterIncrementSolution s1, RosterIncrementSolution s2) {
//        RosterIncrementSolution crossoverSolution = new RosterIncrementSolution(data).initialize();
//        for (int i = 0; i < crossoverSolution.getShiftDayList().size(); i++) {
//            if (s1.getShiftDayList().get(i).size() == crossoverSolution.getShiftDayList().get(i).size()) {
//                if (s2.getShiftDayList().get(i).size() == crossoverSolution.getShiftDayList().get(i).size()) {
//                    for (int k = 0; k < crossoverSolution.getShiftDayList().get(i).size(); k++) {
//                        if (Math.random() > 0.5) {
//                            crossoverSolution.getShiftDayList().get(i).set(k, s1.getShiftDayList().get(i).get(k));
//                        } else {
//                            crossoverSolution.getShiftDayList().get(i).set(k, s2.getShiftDayList().get(i).get(k));
//                        }
//                    }
//                } else {
//                    for (int k = 0; k < crossoverSolution.getShiftDayList().get(i).size(); k++) {
//                        crossoverSolution.getShiftDayList().get(i).set(k, s1.getShiftDayList().get(i).get(k));
//                    }
//                }
//            } else if (s2.getShiftDayList().get(i).size() == crossoverSolution.getShiftDayList().get(i).size()) {
//                for (int k = 0; k < crossoverSolution.getShiftDayList().get(i).size(); k++) {
//                    crossoverSolution.getShiftDayList().get(i).set(k, s2.getShiftDayList().get(i).get(k));
//                }
//            } else {
//                if (Math.random() > 0.5) {
//                    crossoverSolution.getShiftDayList().set(i, s1.getShiftDayList().get(i));
//                } else {
//                    crossoverSolution.getShiftDayList().set(i, s2.getShiftDayList().get(i));
//                }
//            }
//        }
//        return crossoverSolution;
//    }
//
//    private Population crossOverPopulation(Population population) {
//        Population crossOverPopulation = new Population(population.getSize(), data);
//        for (int i = 0; i < configuration.eliteSchedulesNum; i++) {
//            crossOverPopulation.getRosterIncrementSolutionList().set(i, population.getRosterIncrementSolutionList().get(i));
//        }
//        for (int i = configuration.eliteSchedulesNum; i < population.getSize(); i++) {
//            if (configuration.crossOverRate > Math.random()) {
//                RosterIncrementSolution solution1 = selectTournamentPopulation(population).sortByScore().getRosterIncrementSolutionList().get(0);
//                RosterIncrementSolution solution2 = selectTournamentPopulation(population).sortByScore().getRosterIncrementSolutionList().get(0);
//                RosterIncrementSolution crossOverRosterIncrementSolution = crossOverRosterSolution(solution1, solution2);
//                crossOverPopulation.getRosterIncrementSolutionList().set(i, crossOverRosterIncrementSolution);
//            } else {
//                crossOverPopulation.getRosterIncrementSolutionList().set(i, population.getRosterIncrementSolutionList().get(i));
//            }
//        }
//        return crossOverPopulation;
//    }
//
//
//    private Population mutatePopulation(Population population) {
//        Population mutatePopulation = new Population(population.getSize(), data);
//        ArrayList<RosterIncrementSolution> rosterIncrementSolutions = mutatePopulation.getRosterIncrementSolutionList();
//        for (int i = 0; i < configuration.eliteSchedulesNum; i++) {
//            mutatePopulation.getRosterIncrementSolutionList().set(i, population.getRosterIncrementSolutionList().get(i));
//        }
//        for (int i = configuration.eliteSchedulesNum; i < population.getSize(); i++) {
//            rosterIncrementSolutions.set(i, mutateRosterSolution(population.getRosterIncrementSolutionList().get(i)));
//        }
//        return mutatePopulation;
//    }
//
//    private RosterIncrementSolution mutateRosterSolution(RosterIncrementSolution mutateSolution) {
//        RosterIncrementSolution solution = new RosterIncrementSolution(data).initialize();
//        for (int i = 0; i < mutateSolution.getShiftDayList().size(); i++) {
//            if (configuration.mutationRate > Math.random()) {
//                mutateSolution.getShiftDayList().set(i, solution.getShiftDayList().get(i));
//            }
//        }
//        return mutateSolution;
//    }


}
