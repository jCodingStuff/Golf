package com.group.golf.genetics;

import com.group.golf.math.JVector2;

public class Individual {

    private JVector2[] genes;
    private double score;
    private JVector2 lastSpot;
    private JVector2[] landings;

    public Individual(JVector2[] genes) {
        this.genes = genes;
    }

    public JVector2[] getGenes() {
        return genes;
    }

    public void setGenes(JVector2[] genes) {
        this.genes = genes;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public JVector2 getLastSpot() {
        return lastSpot;
    }

    public void setLastSpot(JVector2 lastSpot) {
        this.lastSpot = lastSpot;
    }

    public JVector2[] getLandings() {
        return landings;
    }

    public void setLandings(JVector2[] landings) {
        this.landings = landings;
    }
}
