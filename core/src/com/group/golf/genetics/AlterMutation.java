package com.group.golf.genetics;

import com.group.golf.math.JVector2;

/**
 * Mutation that swaps the components of one movement
 * @author Julian Marrades
 */
public class AlterMutation implements Mutation {

    /**
     * Get a random force from the individual and swap its (x, y) components
     * @param ind the individual to mutate
     * @param rate the mutation rate
     */
    @Override
    public void mutate(Individual ind, double rate) {
        if (Math.random() < rate) {
            int index = (int) (Math.random()*ind.getGenes().length);
            JVector2 force = ind.getGenes()[index];
            force.swap();
        }
    }
}
