package com.group.golf.Physics;

import com.group.golf.Ball;
import com.group.golf.Course;

public class Euler extends Physics {

    public Euler(Course course) {
        super(course);
        errorBound = 0.046f;
    }

    @Override
    public void movement(float delta, boolean simulation) {
        Ball ball = super.getBall();

        float[] accelerations = acceleration(new float[]{ball.getX(),ball.getY()},
                new float[]{ball.getVelocityX(),ball.getVelocityY()});

        float[] newVelocities = new float[]{ball.getVelocityX() + delta * accelerations[0],
                                        ball.getVelocityY() + delta * accelerations[1]};

        float[] newCoords = new float[]{ball.getX() + delta * ball.getVelocityX(),
                                    ball.getY() + delta * ball.getVelocityY()};

        ball.setCoords(newCoords);
        ball.setVelocities(newVelocities);

        if (isRepeting(ball,newCoords)){
            ball.reset();
            repeatChecker = new float[0][2];
        }
        super.checkCollision(simulation);

        System.out.println("Euler Velocity x:     " + newVelocities[0] + "   Velocity y:     " + newVelocities[1]);

        ball.limit(super.getCourse().getVmax());
//        System.out.println("Euler Coords x:     " + newCoords[0] + "   Velocity y:     " + newCoords[1]);
    }
}
