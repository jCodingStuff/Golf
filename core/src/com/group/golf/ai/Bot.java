package com.group.golf.ai;

import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.math.Point3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Bot {

    private final Course course;
    private final Ball ball;
    private final Physics engine;
    private final Collision collision;

    private Stack<Point3D> path;

    /**
     * Create a new Bot instance
     * @param course
     * @param ball
     * @param engine
     * @param collision
     */
    public Bot(Course course, Ball ball, Physics engine, Collision collision) {
        this.course = course;
        this.ball = ball;
        this.engine = engine;
        this.collision = collision;

        this.fillStack();
    }

    /**
     * Fill the path that the bot will follow to solve the game
     */
    private void fillStack() {
        Point3D ballPoint = new Point3D(this.ball.getX(), this.ball.getY());
        Point3D tmpGoal = new Point3D(this.course.getGoal()[0], this.course.getGoal()[1]);
        List<Point3D> range = this.getRange(tmpGoal);
        this.path.push(tmpGoal);
        while (this.ballInRange(tmpGoal, range)) {
            tmpGoal = closestPoint(range);
            range = this.getRange(tmpGoal);
            this.path.push(tmpGoal);
        }
        if (this.collision.isWaterBetween(ballPoint, tmpGoal)) {
            // Solve the water conflict
        }
    }

    /**
     * Check if the ball is within the range of a point
     * @param range the limits of the range
     * @return true if the ball is in range, false otherwise
     */
    private boolean ballInRange(Point3D center, List<Point3D> range) {
        return false;
    }

    /**
     * Get the range of a specific coordinate
     * @param coord the coordinate
     * @return the points forming the limit of the range
     */
    private List<Point3D> getRange(Point3D coord) {
        List<Point3D> points = new ArrayList<Point3D>();
        if () { // Seek points for tolerance range

        } else { // Seek points for exact coordinates

        }
        return points;
    }

    /**
     * Perform a move towards the goal
     */
    private void makeMove() {
        // Get the point on the top of the stack and move the ball there
    }

    // GETTER AND SETTER AREA

    /**
     * Get access to the course instance
     * @return the course instance
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Get the ball instance
     * @return the ball instace
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * Get the physics engine
     * @return the physics engine
     */
    public Physics getEngine() {
        return engine;
    }

    /**
     * Get the collision system
     * @return the collision system
     */
    public Collision getCollision() {
        return collision;
    }

}
