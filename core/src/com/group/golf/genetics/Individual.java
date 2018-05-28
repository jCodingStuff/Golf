package com.group.golf.genetics;

import com.group.golf.math.JVector2;

import java.util.Arrays;

public class Individual {

    private JVector2[] genes;
    private double score;
    private int lastMove;
    private JVector2[] landings;

    public Individual(JVector2[] genes, JVector2[] landings) {
        this.genes = genes;
        this.landings = landings;
    }

    public Individual(Individual other) {
        this.genes = duplicateData(other.genes);
        this.landings = duplicateData(other.landings);
        this.score = other.score;
        this.lastMove = other.lastMove;
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

    private static JVector2[] duplicateData(JVector2[] data) {
        JVector2[] result = new JVector2[data.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new JVector2(data[i]);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Individual{" +
                "\n - genes=" + Arrays.toString(genes) +
                "\n - score = " + score +
                "\n - lastMove (index) = " + lastMove +
                "\n - landings=" + Arrays.toString(landings) +
                '}';
    }
}
