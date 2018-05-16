package com.group.golf.genetics;

import com.group.golf.math.JVector2;

public class Individual {

    private JVector2[] genes;
    private double score;

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
}
