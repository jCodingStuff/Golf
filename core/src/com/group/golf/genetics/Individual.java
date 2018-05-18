package com.group.golf.genetics;

import com.group.golf.math.JVector2;

public class Individual {

    private JVector2[] genes;
    private double score;
    private int lastMove;
    private JVector2[] landings;

    public Individual(JVector2[] genes, JVector2[] landings) {
        this.genes = genes;
        this.landings = landings;
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

    public int getLastMove() {
        return lastMove;
    }

    public void setLastMove(int lastMove) {
        this.lastMove = lastMove;
    }

    public JVector2[] getLandings() {
        return landings;
    }

    public void setLandings(JVector2[] landings) {
        this.landings = landings;
    }
}
