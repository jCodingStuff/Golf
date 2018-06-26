package com.group.golf.genetics;

import com.group.golf.Ball;
import com.group.golf.Physics.Collision;
import com.group.golf.ai.GeneticBot;
import com.group.golf.math.JVector2;

/**
 * GoalChecker for simple courses
 * @author Julian Marrades
 */
public class SimpleChecker implements GoalChecker {

    private GeneticBot bot;

    /**
     * Create a new instance of SimpleChecker
     * @param bot the bot that will operate on this instance
     */
    public SimpleChecker(GeneticBot bot) {
        this.bot = bot;
    }

    /**
     * Check if the closest landing of any individual is inside the tolerance radius of the goal
     * @param goal the coordinates of the goal
     * @param population the population of individuals
     * @param virtualBall the instance of the virtualBall
     * @param virtualCollision the instance of the virtualCollision
     * @return true if the goal has been reached, false otherwise
     */
    @Override
    public boolean goalReached(float[] goal, Individual[] population, Ball virtualBall, Collision virtualCollision) {
        boolean reached = false;
        double recordDistance = Double.MAX_VALUE;
        Individual winner = population[0];
        for (int i = 0; i < population.length && !reached; i++) {

            // Get closest position to the goal for a given individual
            int index = population[i].getLastMove() + 1;
            JVector2 lastSpot = population[i].getLandings()[index];

            // Compute distance to goal and register it
            double dist = JVector2.dist(goal[0], goal[1], lastSpot.getX(), lastSpot.getY());
            if (dist < recordDistance) {
                this.bot.setWinner(population[i]);
                recordDistance = dist;
            }

            // Check if goal has been reached
            virtualBall.setPosition(lastSpot.getX(), lastSpot.getY());
            if (virtualCollision.isGoalAchieved()) {
                System.out.println("Ending position: " + lastSpot.getX() + " " + lastSpot.getY());
                System.out.println("Goal: " + goal[0] + " " + goal[1]);
                reached = true;
            }

        }
        return reached;
    }
}
