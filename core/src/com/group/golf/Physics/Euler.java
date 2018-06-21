package com.group.golf.Physics;

import com.group.golf.Ball;
import com.group.golf.Course;

public class Euler extends Physics {

    public Euler(Course course) {
        super(course);
    }

    public void movement(float delta) {
        Ball ball = super.getBall();
        float[] newCoords = new float[2];
        float[] newVelocities = new float[2];

        newCoords[0] = ball.getX() + delta * ball.getVelocityX();
        newCoords[1] = ball.getY() + delta * ball.getVelocityY();

        float[] accelerations = acceleration(new float[]{ball.getX(),ball.getY()},
                new float[]{ball.getVelocityX(),ball.getVelocityY()});

        newVelocities[0] = ball.getVelocityX() + delta * accelerations[0];
        newVelocities[1] = ball.getVelocityY() + delta * accelerations[1];

        ball.setCoords(newCoords);

        ball.setVelocities(newVelocities);

        super.checkCollision();

        System.out.println("Euler Velocity x:     " + newVelocities[0] + "   Velocity y:     " + newVelocities[1]);

    }
}
