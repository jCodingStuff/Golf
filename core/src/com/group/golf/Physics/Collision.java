package com.group.golf.Physics;

import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.math.Line2D;
import com.group.golf.math.MathLib;
import com.group.golf.math.Point3D;

/**
 * @author Julian Marrades
 * @version 0.1, 13-04-2018
 */
public class Collision {

    private static final double STEP = 0.001;

    private Ball ball;
    private final Course course;

    private double[] offsets;
    private double[] scales;

    private double lastX;
    private double lastY;

    /**
     * Create a new instace of Collision
     * @param ball the ball to evaluate
     * @param course the course in which the ball rolls
     * @param offsets the offsets of the course
     * @param scales the scales of the course
     */
    public Collision(Ball ball, Course course, double[] offsets, double[] scales) {
        this.ball = ball;
        this.course = course;
        this.offsets = offsets;
        this.scales = scales;
        this.lastX = this.course.getStart()[0];
        this.lastX = this.course.getStart()[1];
    }

    /**
     * Create a new instance of Collision from a template
     * @param other the template
     */
    public Collision(Collision other) {
        this.ball = other.ball;
        this.course = other.course;
        this.offsets = other.offsets;
        this.scales = other.scales;
        this.lastX = other.lastX;
        this.lastX = other.lastY;
    }

    /**
     * Check if the ball is within the tolerance range from the goal coordinate
     * @return
     */
    public boolean isGoalAchieved() {
        double xToGoal = this.course.getGoal()[0] - this.ball.getX();
        double yToGoal = this.course.getGoal()[1] - this.ball.getY();
        double distToGoal = Math.sqrt(xToGoal * xToGoal + yToGoal * yToGoal);
        return distToGoal <= this.course.getTolerance();
    }

    /**
     * React when the ball hits a wall
     */
    public void checkForWalls() {
        double[] real = MathLib.toPixel(new double[]{this.ball.getX(), this.ball.getY()}, this.offsets, this.scales);
        if (real[0] < Ball.RADIUS || real[0] > Golf.VIRTUAL_WIDTH - Ball.RADIUS) {
            this.ball.setVelocityX(-this.ball.getVelocityX());

        }
        if (real[1] < Ball.RADIUS || real[1] > Golf.VIRTUAL_HEIGHT - Ball.RADIUS) {
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
        Line2D path = new Line2D(this.lastX, this.lastY, ballX, ballY);

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

    /**
     * Check if there is water in the straight line that joins two points
     * @param a a Point3D instance
     * @param b a Point3D instance
     * @return
     */
    public boolean isWaterBetween(Point3D a, Point3D b) {
        Line2D path = new Line2D(a, b);
        boolean water = false;
        if (b.getX() >= a.getX()) { // B is on the right of A
            for (double x = a.getX(); x <= b.getX() && !water; x += STEP) {
                if (this.course.getHeight(x, path.getY(x)) < 0) { // Ball in water
                    water = true;
                }
            }
        } else { // B is on the left of A
            for (double x = b.getX(); x <= a.getX() && !water; x += STEP) {
                if (this.course.getHeight(x, path.getY(x)) < 0) { // Ball in water
                    water = true;
                }
            }
        }
        return water;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public Course getCourse() {
        return course;
    }

    public double[] getOffsets() {
        return offsets;
    }

    public void setOffsets(double[] offsets) {
        this.offsets = offsets;
    }

    public double[] getScales() {
        return scales;
    }

    public void setScales(double[] scales) {
        this.scales = scales;
    }
}
