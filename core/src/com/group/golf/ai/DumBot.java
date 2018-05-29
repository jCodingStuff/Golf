package com.group.golf.ai;

import com.badlogic.gdx.math.Vector2;
/**
 * Class to generate instances of DumBot
 * @author Kaspar Kallast
 * @version 0.2, 29-05-2018
 */
import com.badlogic.gdx.math.Vector2;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.math.Point3D;

public class DumBot implements Bot {

    private static final double A_SCALAR = 15; 
    private static final double F_SCALAR = 2; 
    private static final double BIG_SCALAR = 30; 

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
    	double extraPower = 6.5; 
    	double distanceLimit = 0.75; 
    	double[] goalCoords = this.course.getGoal();
    	double[] distances = new double[] {goalCoords[0]-this.ball.getX(), goalCoords[1]-this.ball.getY()};
    	/*
    	for (int i = 0; i < distances.length; i++) {
    		if (Math.abs(distances[i]) < Math.abs(distanceLimit)) {
    			// distances[i] += extraPower;
    			if (i == 0) {
    				distances[i] *= (extraPower);
    				System.out.println("distanceLimit applied to distances[0].");
    			}
    			else {
    				if (i == 1) distances[i] *= (extraPower);
        			System.out.println("distanceLimit applied to distances[1].");
    			}
    		}
    	    distances[i] *= BIG_SCALAR;
        }
    	*/
    	if ((Math.abs(distances[0]) + Math.abs(distances[1])) / 2 < Math.abs(distanceLimit)) {
    		System.out.println("distanceLimit applied");
    		distances[0] *= extraPower;
        	distances[1] *= extraPower; 
    	}
    	distances[0] *= BIG_SCALAR;
    	distances[1] *= BIG_SCALAR; 
        this.scale(distances);
    	this.engine.hit(distances[0], distances[1]);
    }

    private void scale(double[] forces) {
        double[] derivatives = this.engine.calculateSlope(new double[]{this.ball.getX(), this.ball.getY()});
        for (int i = 0; i < forces.length; i++) {
            forces[i] = this.scaleForce(forces[i], derivatives[i]);
        }
    }

    private double scaleForce(double force, double d) {
    	double scaledForce;
    	if (force > 0 && d > 0) {
    		scaledForce = force * Math.abs(d) * A_SCALAR;
    	}
    	else if (force > 0 && d < 0) {
    		scaledForce = force / (Math.abs(d) * F_SCALAR);
    	}
    	else if (force < 0 && d > 0) {
    		scaledForce = force / (Math.abs(d) * F_SCALAR);
    	}
    	else if (force < 0 && d < 0) {
    		scaledForce = force * Math.abs(d) * A_SCALAR;
    	}
    	else {
    		scaledForce = force;
    	}
    	
    	return scaledForce;
    }
}
