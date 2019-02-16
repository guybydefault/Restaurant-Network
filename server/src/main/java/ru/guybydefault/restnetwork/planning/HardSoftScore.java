package ru.guybydefault.restnetwork.planning;

public class HardSoftScore implements Comparable<HardSoftScore>, Cloneable {

    private int hardScore;
    private int softScore;

    public HardSoftScore(int hardScore, int softScore) {
        this.hardScore = hardScore;
        this.softScore = softScore;
    }


    public void minusHard(int s) {
        hardScore -= s;
    }


    public void minusSoft(int s) {
        softScore -= s;
    }

    public int getHardScore() {
        return hardScore;
    }

    public int getSoftScore() {
        return softScore;
    }

    @Override
    public Object clone() {
        HardSoftScore copy = null;
        try {
            copy = (HardSoftScore) super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
        return copy;
    }

    @Override
    public int compareTo(HardSoftScore o) {
        if (this.getHardScore() - o.getHardScore() != 0) {
            return o.getHardScore() - this.getHardScore();
        }
        return o.getSoftScore() - this.getSoftScore();
    }

    @Override
    public String toString() {
        return "HardSoftScore{" +
                "hardScore=" + hardScore +
                ", softScore=" + softScore +
                '}';
    }
}
