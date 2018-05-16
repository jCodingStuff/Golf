package com.group.golf.genetics;

import java.util.List;

public class Individual {

    private double[][] genes;

    public Individual(double[][] genes) {
        this.genes = genes;
    }

    public double[][] getGenes() {
        return genes;
    }

    public void setGenes(double[][] genes) {
        this.genes = genes;
    }

    public double[] getMovement(int index) {return this.genes[index];}

    public void setMovement(int index, double[] forces) {this.genes[index] = forces;}
}
