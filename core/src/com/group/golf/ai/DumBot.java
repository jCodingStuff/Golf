package com.group.golf.ai;

/**
 * Class to generate instances of DumBot
 * @author Kaspar Kallast
 * @version 0.1, 15-05-2018
 */
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.math.Point3D;

public class DumBot implements Bot {
	
	private final Course course;
    private final Ball ball;
    private Physics engine;
    private Collision collision;

    /**
     * Create a new instance of DumBot
     * @param course the course
     * @param ball the ball
     */
    public DumBot(Course course, Ball ball) {
		this.course = course;
		this.ball = ball;
	}

    @Override
    public void setPhysics(Physics physics) {
        this.engine = physics;
    }

    @Override
    public void setCollision(Collision collision) {
        this.collision = collision;
    }

    @Override
    public void makeMove() {
    	double[] goalCoords = this.course.getGoal();
    	double[] XnY = new double[] {goalCoords[0]-this.ball.getX(), goalCoords[1]-this.ball.getY()};
    	this.engine.hit(XnY[0], XnY[1]);

    }
}
