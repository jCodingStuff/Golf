package com.group.golf.genetics;

import java.util.List;

public class Individual {

    private List<double[]> genes;

    public Individual(List<double[]> genes) {
        this.genes = genes;
    }

    public List<double[]> getGenes() {
        return genes;
    }

    public void setGenes(List<double[]> genes) {
        this.genes = genes;
    }
}
