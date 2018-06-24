package com.group.golf.Physics;

import com.group.golf.Ball;
import com.group.golf.Course;

public class Verlet extends Physics {

    public Verlet(Course course) {
        super(course);
        errorBound = 0.035f;
    }

    public void movement(float delta, boolean simulation) {
        Ball ball = super.getBall();
        float[] currentVelocity = new float[]{ball.getVelocityX(),ball.getVelocityY()};
        float[] currentAcceleration = acceleration(new float[]{ball.getX(),ball.getY()}, currentVelocity);

        float[] newCoordinates = new float[]{ball.getX() + delta * (ball.getVelocityX() + 0.5f * delta * currentAcceleration[0]),
                ball.getY() + delta * (ball.getVelocityY() + 0.5f * delta * currentAcceleration[1])};

        float[] nextAcceleration = acceleration(newCoordinates,currentVelocity);

        float[] newVelocities = new float[]{ball.getVelocityX() + 0.5f * delta * (currentAcceleration[0] + nextAcceleration[0]),
                ball.getVelocityY() + 0.5f * (currentAcceleration[1] + nextAcceleration[1]) * delta};

        ball.setCoords(newCoordinates);
        ball.setVelocities(newVelocities);

        if (isRepeting(ball,newCoordinates)){
            ball.reset();
            repeatChecker = new float[0][2];
        }

        super.checkCollision(simulation);
        ball.limit(super.getCourse().getVmax());
//        System.out.println("Verlet x:     " + newCoords[0] +     " y: " + newCoords[1]);
//        System.out.println("Verlet velocity x: " + newVelocities[0] + "  y: " + newVelocities[1]);
    }
}