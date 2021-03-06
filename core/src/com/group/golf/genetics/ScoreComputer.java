package com.group.golf.genetics;

import com.group.golf.Course;

/**
 * An interface for fitness computation
 * @author Julian Marrades
 */
public interface ScoreComputer {

    /**
     * Compute score for genetic Algorithm
     * @param course the course where the algorithm is operating
     * @param population the population to evaluate
     */
    void compute(Course course, Individual[] population);

}
