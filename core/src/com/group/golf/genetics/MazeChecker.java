package com.group.golf.genetics;

import com.group.golf.Ball;
import com.group.golf.Physics.Collision;
import com.group.golf.ai.GeneticBot;
import com.group.golf.math.JVector2;

/**
 * GoalChecker for mazes
 * @author Julian Marrades
 */
public class MazeChecker implements GoalChecker {

    private GeneticBot bot;

    /**
     * Create a new instance of MazeChecker
     * @param bot the bot that will operate this instance
     */
    public MazeChecker(GeneticBot bot) {
        this.bot = bot;
    }

    /**
     * Check if any of the landings of any individual is inside the tolerance radius of the goal
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
        for (int i = 0; i < population.length && !reached; i++) {
            JVector2[] landings = population[i].getLandings();
            for (int j = 0; j < landings.length && !reached; j++) {
                JVector2 spot = landings[j];

                // Compute distance to goal and register it
                double dist = JVector2.dist(goal[0], goal[1], spot.getX(), spot.getY());
                if (dist < recordDistance) {
                    population[i].setLastMove(j-1);
                    this.bot.setWinner(population[i]);
                    recordDistance = dist;
                }

                // Check if goal has been reached
                virtualBall.setPosition(spot.getX(), spot.getY());
                if (virtualCollision.isGoalAchieved()) {
                    System.out.println("Ending position: " + spot.getX() + " " + spot.getY());
                    System.out.println("Goal: " + goal[0] + " " + goal[1]);
                    reached = true;
                }
            }
        }
        return reached;
    }
    
}
