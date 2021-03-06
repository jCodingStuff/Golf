package com.group.golf.genetics;

import com.group.golf.math.JVector2;

/**
 * Mutation that swaps the order of two movements
 * @author Julian Marrades
 */
public class SwapMutation implements Mutation {

    /**
     * Get to random forces from the individual and swap their order
     * @param ind the individual to mutate
     * @param rate the mutation rate
     */
    @Override
    public void mutate(Individual ind, double rate) {
        if (Math.random() < rate) {
            int indexA = (int) (Math.random()*ind.getGenes().length);
            int indexB = (int) (Math.random()*ind.getGenes().length);
            JVector2[] genes = ind.getGenes();
            JVector2 tmp = genes[indexA];
            genes[indexA] = genes[indexB];
            genes[indexB] = tmp;
        }
    }
}
