package com.group.golf.ai;

import java.util.List;

import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.math.Point3D;

public class Bot {

    private Course course;
    private Ball ball;
    private final Physics engine;
    private final Collision collision;

    /**
     * Create a new Bot instace 
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
    }
    
    /**
     * Find the closest point in the list of points to the ball's location.
     * @return the found point.
     */
    private Point3D closestPoint(List<Point3D> points) {
    	double starter = Double.MAX_VALUE;
    	Point3D point = new Point3D(this.ball.getX(), this.ball.getY());
    	
    	for (int i = 0; i < points.size(); i++) {
    		double distance = Math.pow(points.get(i).getX(), 2) + Math.pow(points.get(i).getY(),2);
    		if ((starter > distance) && (!this.collision.isWaterBetween(point, points.get(i)))) {
    			starter = distance;
    			point = points.get(i);
    		}
    	}
    	return point;
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
     * Set a course instance
     * @param course the new course instance
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Get the ball instance
     * @return the ball instace
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * Set a new ball instace
     * @param ball the new ball instance
     */
    public void setBall(Ball ball) {
        this.ball = ball;
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
