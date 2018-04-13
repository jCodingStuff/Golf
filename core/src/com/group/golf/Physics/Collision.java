package com.group.golf.Physics;

import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.math.Line2D;

/**
 * @author Julian Marrades
 * @version 0.1, 13-04-2018
 */
public class Collision {

    private static final double STEP = 0.001;

    private final Ball ball;
    private final Course course;

    private double lastX;
    private double lastY;

    /**
     * Create a new instace of Collision
     * @param ball the ball to evaluate
     * @param course the course in which the ball rolls
     */
    public Collision(Ball ball, Course course) {
        this.ball = ball;
        this.course = course;
        this.lastX = this.course.getStart()[0];
        this.lastX = this.course.getStart()[1];
    }

    /**
     * Check if the ball is within the tolerance range from the goal coordinate
     * @return
     */
    public boolean isGoalAchieved() {
        double xToGoal = this.course.getGoal()[0] - this.ball.getX();
        double yToGoal = this.course.getGoal()[1] - this.ball.getY();
        double distToGoal = Math.sqrt(Math.pow(xToGoal, 2) + Math.pow(yToGoal, 2));
        return distToGoal < this.course.getTolerance();
    }

    /**
     * React when the ball hits a wall
     * @param ballX the pixel-x position of the ball
     * @param ballY the pixel-y position of the ball
     */
    public void checkForWalls(double ballX, double ballY) {
        if (ballX <= 0 || ballX >= Golf.VIRTUAL_WIDTH) {
            this.ball.setVelocityX(-this.ball.getVelocityX());

        }
        if (ballY <= 0 || ballY >= Golf.VIRTUAL_HEIGHT) {
            this.ball.setVelocityY(-this.ball.getVelocityY());
        }
    }

    /**
     * Check if the ball has gone to water or is currently in water
     * @return true if it has gone or is in water, false otherwise
     */
    public boolean ballInWater() {
        boolean water = false;

        double ballX = this.ball.getX();
        double ballY = this.ball.getY();
        Line2D path = new Line2D(this.lastX, this.lastY, this.ball.getX(), this.ball.getY());

        // Evaluate the line
        if (ballX >= this.lastX) { // Ball is moving to the right
            for (double x = this.lastX; x <= ballX && !water; x += STEP) {
                if (this.course.getHeight(x, path.getY(x)) < 0) { // Ball in water
                    water = true;
                }
            }
        } else { // Ball is moving to the left
            for (double x = ballX; x <= this.lastX && !water; x += STEP) {
                if (this.course.getHeight(x, path.getY(x)) < 0) { // Ball in water
                    water = true;
                }
            }
        }

        // Make the current position of the ball the last
        this.lastX = ballX;
        this.lastY = ballY;

        return water;
    }

}
