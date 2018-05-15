package com.group.golf.ai;

/**
 * Class to generate instances of DumBot
 * @author Kaspar Kallast
 * @version 0.1, 15-05-2018
 */
import com.badlogic.gdx.math.Vector2;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.math.Point3D;

public class DumBot implements Bot {

    private static final double A_SCALAR = 10;
    private static final double F_SCALAR = 10;
    private static final double BIG_SCALAR = 100;

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
    	double[] forces = new double[] {goalCoords[0]-this.ball.getX(), goalCoords[1]-this.ball.getY()};
    	for (int i = 0; i < forces.length; i++) {
    	    forces[i] *= BIG_SCALAR;
        }
        this.scale(forces);
    	this.engine.hit(forces[0], forces[1]);
    }

    private void scale(double[] forces) {
        forces[0] = this.scaleX(forces[0]);
        forces[1] = this.scaleY(forces[1]);
    }

    private double scaleX(double forceX) {

    }

    private double scaleY(double forceY) {
        double scaledForce;
        double dy = this.engine.calculateSlope(new Vector2((float) this.ball.getX(), (float) this.ball.getY()))[1];
        if (forceY > 0 && dy > 0) { // Moving right against the slope
            scaledForce = forceY * Math.abs(dy) * A_SCALAR;
        } else if (forceY > 0 && dy < 0) { // Moving right in favour of the slope
            scaledForce = forceY / (Math.abs(dy) * F_SCALAR);
        } else if (forceY < 0 && dy > 0) { // Moving left in favour of the slope
            scaledForce = forceY / (Math.abs(dy) * F_SCALAR);
        } else if (forceY < 0 && dy < 0) { // Moving left against the slope
            scaledForce = forceY * Math.abs(dy) * A_SCALAR;
        } else {
            scaledForce = forceY;
        }
        return scaledForce;
    }
}
