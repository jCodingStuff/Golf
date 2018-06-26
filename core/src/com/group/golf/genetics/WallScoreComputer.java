package com.group.golf.genetics;

import com.group.golf.Course;
import com.group.golf.math.JVector2;
import com.group.golf.math.Point3D;

/**
 * Class WallScoreComputer computes fitness for maze-like courses
 * @author Julian Marrades
 */
public class WallScoreComputer implements ScoreComputer {

    /**
     * Penalize the distance from the last spot (s) to the goal (g), the number of walls between s and g, and the presence of water between s and g
     * @param course the course where the algorithm is operating
     * @param population the population to evaluate
     */
    @Override
    public void compute(Course course, Individual[] population) {
        float[] goal = course.getGoal();
        for (int i = 0; i < population.length; i++) {
            JVector2[] landings = population[i].getLandings();
            int index = landings.length - 1;
            JVector2 spot = landings[index];
            double dist = JVector2.dist(spot.getX(), spot.getY(), goal[0], goal[1]);

            Point3D point = new Point3D(spot.getX(), spot.getY(), 0);
            double wallNum = course.getWallNum(point);
//            System.out.println("WALLS: " + wallNum);

            population[i].setLastMove(index - 1);
            float water;
            if (course.isWaterBetween(new Point3D(goal[0], goal[1], 0), new Point3D(spot.getX(), spot.getY(), 0))) {
                water = 1;
            } else {
                water = 0;
            }
            double score = (1/dist) + (1/(1+wallNum)) + (1/(1+water));
            population[i].setScore(score);
        }
    }

}
