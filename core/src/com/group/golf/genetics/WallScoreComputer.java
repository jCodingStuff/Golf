package com.group.golf.genetics;

import com.group.golf.Course;
import com.group.golf.math.JVector2;
import com.group.golf.math.Point3D;

public class WallScoreComputer implements ScoreComputer {

    @Override
    public void compute(Course course, Individual[] population) {
        double[] goal = course.getGoal();
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

            JVector2 closestSpot = landings[closestIndex];
            Point3D closestPoint = new Point3D(closestSpot.getX(), closestSpot.getY(), 0);
            double wallNum = course.getWallNum(closestPoint);

            population[i].setLastMove(closestIndex - 1);
            double score;
            if (closestIndex == 0) {
                score = 0;
            }
            else {
                score = (1/closestIndex) + (1/closestDist) + (1/(1+wallNum));
            }
            population[i].setScore(score);
        }
    }

}
