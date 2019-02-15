package ru.guybydefault.restnetwork.planning;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Configuration
public class PlanningConfiguration {
    private double startingReproductionRate = 0.01;
    private int eliteSolutionsNumber = 3;
    private int reproductionResetEliteInterval = 7;
    private int maxSolutionSize = 1000;

    public PlanningConfiguration() {
    }

    public PlanningConfiguration(double startingReproductionRate, int eliteSolutionsNumber, int reproductionResetEliteInterval, int maxSolutionSize) {
        this.startingReproductionRate = startingReproductionRate;
        this.eliteSolutionsNumber = eliteSolutionsNumber;
        this.reproductionResetEliteInterval = reproductionResetEliteInterval;
        this.maxSolutionSize = maxSolutionSize;
    }

    @Bean(value = "defaultPlanningConfiguration")
    public static PlanningConfiguration buildDefaultConfiguration() {
        return new PlanningConfiguration();
    }

    public double getStartingReproductionRate() {
        return startingReproductionRate;
    }

    public int getEliteSolutionsNumber() {
        return eliteSolutionsNumber;
    }

    public int getReproductionResetEliteInterval() {
        return reproductionResetEliteInterval;
    }

    public int getMaxSolutionSize() {
        return maxSolutionSize;
    }
}
