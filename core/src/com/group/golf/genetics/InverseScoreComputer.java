package com.group.golf.genetics;

import com.group.golf.Course;
import com.group.golf.math.JVector2;

/**
 * Class InverseScoreComputer computes fitness for simple courses
 * @auther Julian Marrades
 */
public class InverseScoreComputer implements ScoreComputer {

    /**
     * Penalize the distance from the closestPoint to the goal, and the number of shots to achieve closestPoint
     * @param course the course where the algorithm is operating
     * @param population the population to evaluate
     */
    @Override
    public void compute(Course course, Individual[] population) {
        float[] goal = course.getGoal();
        for (int i = 0; i < population.length; i++) {
            JVector2[] landings = population[i].getLandings();
            double closestDist = Double.MAX_VALUE;
            int closestIndex = 0;
            for (int j = 1; j < landings.length; j++) {
                double dist = JVector2.dist(landings[j].getX(), landings[j].getY(), goal[0], goal[1]);
                if (dist < closestDist) {
                    closestDist = dist;
                    closestIndex = j;
                }
            }
            population[i].setLastMove(closestIndex - 1);
            double score;
            if (closestIndex == 0) {
                score = 0;
            }
            else {
                score = (1/closestIndex) + (1/closestDist);
            }
            population[i].setScore(score);
        }
    }
}
