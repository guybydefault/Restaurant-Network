package ru.guybydefault.restnetwork.planning;

public class HardSoftScore implements Comparable<HardSoftScore> {

    private int hardScore;
    private int softScore;

    public HardSoftScore() {
    }

    public HardSoftScore(int hardScore, int softScore) {
        this.hardScore = hardScore;
        this.softScore = softScore;
    }

    public int getHardScore() {
        return hardScore;
    }

    public void setHardScore(int hardScore) {
        this.hardScore = hardScore;
    }

    public int getSoftScore() {
        return softScore;
    }

    public void setSoftScore(int softScore) {
        this.softScore = softScore;
    }

    public void addHard(int s) {
        this.hardScore += s;
    }

    public void minusHard(int s) {
        this.hardScore -= s;
    }

    public void addSoft(int s) {
        this.softScore += s;
    }

    public void minusSoft(int s) {
        this.softScore -= s;
    }

    public void reset() {
        this.hardScore = 0;
        this.softScore = 0;
    }

    @Override
    public int compareTo(HardSoftScore o) {
        if (this.getHardScore() - o.getHardScore() != 0) {
            return this.getHardScore() - o.getHardScore();
        }
        return this.getSoftScore() - o.getSoftScore();
    }

    @Override
    public String toString() {
        return "HardSoftScore{" +
                "hardScore=" + hardScore +
                ", softScore=" + softScore +
                '}';
    }
}
