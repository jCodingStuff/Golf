package com.group.golf.genetics;

/**
 * An interface for mutation algorithms
 * @auther Julian Marrades
 */
public interface Mutation {

    /**
     * Mutate an individual
     * @param ind the individual to mutate
     * @param rate the mutation rate
     */
    void mutate(Individual ind, double rate);

}
