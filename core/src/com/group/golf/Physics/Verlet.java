package com.group.golf.Physics;

import com.group.golf.Ball;
import com.group.golf.Course;

/**
 * A physics engine that users Verlet ODE solver method
 * @author Alexandros Chimonas
 */
public class Verlet extends Physics {

    /**
     * Create a new Verlet instance
     * @param course the course being played
     */
    public Verlet(Course course) {
        super(course);
        errorBound = 0.07f;
    }

    public void movement(float delta, boolean simulation) {
        Ball ball = super.getBall();
        float[] currentVelocity = new float[]{ball.getVelocityX(),ball.getVelocityY()};
        float[] currentAcceleration = acceleration(new float[]{ball.getX(),ball.getY()}, currentVelocity);

        float[] newCoordinates = new float[]{ball.getX() + delta * (ball.getVelocityX() + 0.5f * delta * currentAcceleration[0]),
                ball.getY() + delta * (ball.getVelocityY() + 0.5f * delta * currentAcceleration[1])};

        float[] nextAcceleration = acceleration(newCoordinates,currentVelocity);

        float[] newVelocities = new float[]{ball.getVelocityX() + 0.5f * delta * (currentAcceleration[0] + nextAcceleration[0]),
                ball.getVelocityY() + 0.5f * delta * (currentAcceleration[1] + nextAcceleration[1])};

        ball.setCoords(newCoordinates);
        ball.setVelocities(newVelocities);

        checkLowVelocity();


        super.checkCollision(simulation);
        ball.limit(super.getCourse().getVmax());
//        System.out.println("Verlet x:     " + newCoords[0] +     " y: " + newCoords[1]);
//        System.out.println("Verlet velocity x: " + newVelocities[0] + "  y: " + newVelocities[1]);
    }
}