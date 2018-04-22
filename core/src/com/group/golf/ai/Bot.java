package com.group.golf.ai;

import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.math.Line2D;
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
    private Point3D ballPoint;

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

        this.ballPoint = new Point3D(this.ball.getX(), this.ball.getY());

        this.fillStack();
    }

    /**
     * Fill the path that the bot will follow to solve the game
     */
    private void fillStack() {
        Point3D tmpGoal = new Point3D(this.course.getGoal()[0], this.course.getGoal()[1]);
        List<Point3D> range = this.getRange(tmpGoal);
        this.path.push(tmpGoal);
        while (this.ballInRange(tmpGoal, range)) {
            tmpGoal = closestPoint(range);
            range = this.getRange(tmpGoal);
            this.path.push(tmpGoal);
        }
        if (this.collision.isWaterBetween(this.ballPoint, tmpGoal)) {
            // Solve the water conflict
        }
    }

    /**
     * Check if the ball is within the range of a point
     * @param center the center of the range
     * @param range the limits of the range
     * @return true if the ball is in range, false otherwise
     */
    private boolean ballInRange(Point3D center, List<Point3D> range) {
        Line2D tmpGoalToBall = new Line2D(this.ballPoint, center);
        Point3D[] bounds = this.getBounds(tmpGoalToBall, center, range);

    }

    private Point3D[] getBounds(Line2D line, Point3D center, List<Point3D> points) {
        Point3D[] bounds = new Point3D[2];
        if (center.getX() < this.ballPoint.getX()) {

        } else {

        }
        return bounds;
    }

    /**
     * Get the range of a specific coordinate
     * @param coord the coordinate
     * @return the points forming the limit of the range
     */
    private List<Point3D> getRange(Point3D coord) {
        List<Point3D> points = new ArrayList<Point3D>();
        Point3D goalPoint = new Point3D(this.course.getGoal()[0], this.course.getGoal()[1]);
        if (coord.equals(goalPoint)) { // Seek points for tolerance range

        } else { // Seek points for exact coordinates

        }
        return points;
    }
    
    /**
     * Find the closest point in the list of points to the ball's location.
     * @return the found point.
     */
    private Point3D closestPoint(List<Point3D> points) {
    	double starter = Double.MAX_VALUE;
    	Point3D point = this.ballPoint;
    	
    	for (int i = 0; i < points.size(); i++) {
    		double distance = Math.pow(points.get(i).getX(), 2) + Math.pow(points.get(i).getY(),2);
    		if ((starter > distance) && (!this.collision.isWaterBetween(point, points.get(i)))) {
    			starter = distance;
    			point = points.get(i);
    		}
    	}
    	return point;
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
