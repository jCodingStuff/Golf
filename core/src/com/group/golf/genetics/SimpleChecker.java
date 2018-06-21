package com.group.golf.genetics;

import com.group.golf.Ball;
import com.group.golf.Physics.Collision;
import com.group.golf.ai.GeneticBot;
import com.group.golf.math.JVector2;

public class SimpleChecker implements GoalChecker {

    private GeneticBot bot;

    public SimpleChecker(GeneticBot bot) {
        this.bot = bot;
    }

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
