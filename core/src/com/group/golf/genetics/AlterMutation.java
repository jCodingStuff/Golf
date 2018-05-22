package com.group.golf.genetics;

import com.group.golf.math.JVector2;

/**
 * Mutation that swaps the components of one movement
 */
public class AlterMutation implements Mutation {

    @Override
    public void mutate(Individual ind, double rate) {
        if (Math.random() < rate) {
            int index = (int) (Math.random()*ind.getGenes().length);
            JVector2 force = ind.getGenes()[index];
            force.swap();
        }
    }
}
