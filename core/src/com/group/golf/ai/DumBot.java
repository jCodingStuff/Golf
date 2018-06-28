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

/**
 * A very simple bot to solve simple courses
 * @author Kaspar Kallast
 */
public class DumBot implements Bot {

    private static final float A_SCALAR = 15;
    private static final float F_SCALAR = 2;
    private static final float BIG_SCALAR = (float) 0.935;

	private final Course course;
    private final Ball ball;
    private Physics engine;


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


	/**
	 * Compute distance between the ball and the goal and make a shot in that direction, using a force proportional to that distance
	 */
	@Override
    public void makeMove() {
    	double extraPower = 2; 
    	double distanceLimit = 0.75; 
    	float[] goalCoords = this.course.getGoal();
    	float[] distances = new float[] {goalCoords[0]-this.ball.getX(), goalCoords[1]-this.ball.getY()};
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
    	this.engine.hit(ball,distances[0], distances[1]);
    }

	/**
	 * Scale the forces to apply using the slope
	 * @param forces
	 */
	private void scale(float[] forces) {
        float[] derivatives = this.engine.calculateSlope(new float[]{this.ball.getX(), this.ball.getY()});
        for (int i = 0; i < forces.length; i++) {
            forces[i] = this.scaleForce(forces[i], derivatives[i]);
        }
    }

	/**
	 * Scale a force
	 * @param force the force
	 * @param d the scales
	 * @return the scaled force
	 */
    private float scaleForce(float force, float d) {
    	float scaledForce;
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
