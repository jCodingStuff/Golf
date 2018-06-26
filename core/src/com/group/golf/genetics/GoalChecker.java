package com.group.golf.genetics;

import com.group.golf.Ball;
import com.group.golf.Physics.Collision;

/**
 * An interface to check if an individual of a population has reached the goal
 * @author Julian Marrades
 */
public interface GoalChecker {

    /**
     * Check if the goal has been reached
     * @param goal the coordinates of the goal
     * @param population the population of individuals
     * @param virtualBall the instance of the virtualBall
     * @param virtualCollision the instance of the virtualCollision
     * @return true if the goal has been reached, false otherwise
     */
    boolean goalReached(float[] goal, Individual[] population, Ball virtualBall, Collision virtualCollision);

}
