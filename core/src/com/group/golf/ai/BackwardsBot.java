package com.group.golf.ai;

import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.math.Line2D;
import com.group.golf.math.MathLib;
import com.group.golf.math.Point3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BackwardsBot implements Bot {

    private final Course course;
    private final Ball ball;
    private Physics engine;

    private Stack<Point3D> path;
    private Point3D ballPoint;

    /**
     * Create a new Bot instance
     * @param course
     * @param ball
     */
    public BackwardsBot(Course course, Ball ball) {
        this.course = course;
        this.ball = ball;

        this.ballPoint = new Point3D(this.ball.getX(), this.ball.getY());

        this.fillStack();
    }

    @Override
    public void setPhysics(Physics physics) {
        this.engine = physics;
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
        if (this.engine.getCollision().isWaterBetween(this.ballPoint, tmpGoal)) {
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
        Line2D bounds = this.getBounds(tmpGoalToBall, center, range);
        if (center.getX() <= this.ballPoint.getX()) {
            return bounds.isRight(this.ballPoint);
        } else {
            return bounds.isLeft(this.ballPoint);
        }
    }

    /**
     * Get the closest line to ball that cuts the path which joins goal and ball
     * @param line the line joining ball and goal
     * @param center the tmpGoal (center of range)
     * @param points the list of points forming the range
     * @return
     */
    private Line2D getBounds(Line2D line, Point3D center, List<Point3D> points) {
        Line2D bounds = null;
        double recordDistance = Double.MAX_VALUE;
        if (center.getX() <= this.ballPoint.getX()) {
            /* Obtain the closest point to the ball (under the line), for which the next point is above the line */
            for (int i = 0; i < points.size(); i++) {
                Point3D current = points.get(i);
                Point3D next = points.get(i == points.size() - 1 ? 0 : i+1);
                double distanceX = this.ballPoint.getX() - current.getX();
                if (current.getX() >= center.getX() && line.isUnder(current) && line.isAbove(next)
                        && distanceX < recordDistance) {
                    bounds = new Line2D(current, next);
                    recordDistance = distanceX;
                }
            }
        } else {
            /* Obtain the closest point to the ball (above the line), for which the next point is under the line */
            for (int i = 0; i < points.size(); i++) {
                Point3D current = points.get(i);
                Point3D next = points.get(i == points.size() - 1 ? 0 : i+1);
                double distanceX = current.getX() - this.ballPoint.getX();
                if (current.getX() <= center.getX() && line.isAbove(current) && line.isUnder(next)
                        && distanceX < recordDistance) {
                    bounds = new Line2D(current, next);
                    recordDistance = distanceX;
                }
            }
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
            double distance = MathLib.distanceSquared(point, points.get(i));
            if ((starter > distance) && (!this.engine.getCollision().isWaterBetween(point, points.get(i)))) {
                starter = distance;
                point = points.get(i);
            }
        }
        return point;
    }

    @Override
    public void makeMove() {
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

}
