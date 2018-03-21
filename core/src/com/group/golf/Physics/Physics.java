package com.group.golf.Physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.math.Function;

/**
 * Created by alex_ on 21-Mar-18.
 */

public class Physics {
    private Course course;
    private Ball ball;

    public Physics(Course course, Ball ball) {
        this.course = course;
        this.ball = ball;
    }


    //angle between force and y - basis vector
    public void hit(double force, double angle) {
        double forceX = force * Math.cos(angle);
        double forceY = force * Math.sin(angle);

        ball.setVelocityX(Gdx.graphics.getDeltaTime() * forceX / ball.getMass());
        ball.setVelocityY(Gdx.graphics.getDeltaTime() * forceY / ball.getMass());
    }

    public void movement() {
        Vector2 grav = gravForce(ball, new Vector2((float) ball.getX(),(float) ball.getY()));
        Vector2 friction =  frictionForce(ball,ball.getVelocityX(),ball.getVelocityY());

        ball.setX(ball.getX() + Gdx.graphics.getDeltaTime() * ball.getVelocityX());
        ball.setY(ball.getY() + Gdx.graphics.getDeltaTime() * ball.getVelocityY());

        ball.setVelocityX(ball.getVelocityX() + Gdx.graphics.getDeltaTime() * (grav.x + friction.x));
        ball.setVelocityY(ball.getVelocityY() + Gdx.graphics.getDeltaTime() * (grav.y + friction.y));
    }
    
    public Vector2 frictionForce(Ball ball,double velocityX, double velocityY) {
        double multiplier = - this.course.getMu() * this.course.getG() * ball.getMass() / normalLength(velocityX,velocityY);
        return new Vector2((float) (multiplier * velocityX) , (float) (multiplier * velocityY));
    }

    public double normalLength(double velocityX, double velocityY) {
        return (Math.sqrt(Math.pow(velocityX,2) + Math.pow(velocityY,2)));
    }

    public Vector2 gravForce(Ball ball, Vector2 coord) {
        double multiplier = ball.getMass()* this.course.getG();
        Vector2 slopeMultiplier = calculateSlope(coord);
        return new Vector2((float) - multiplier * slopeMultiplier.x,(float)- multiplier * slopeMultiplier.y);
    }

    public Vector2 calculateSlope(Vector2 coord) {
        Vector2 slope = new Vector2();

        if (this.course.getFunction().getZ(coord.x-1,coord.y) != this.course.getFunction().getZ(coord.x,coord.y) &&
                (this.course.getFunction().getZ(coord.x+1,coord.y) != this.course.getFunction().getZ(coord.x,coord.y))) {
            if (this.course.getFunction().getZ(coord.x-1,coord.y) < this.course.getFunction().getZ(coord.x+1,coord.y)) {
                slope.x = (float) (this.course.getFunction().getZ(coord.x-1,coord.y)-this.course.getFunction().getZ(coord.x,coord.y))/((coord.x-1)-coord.x);
            } else {
                slope.x = (float) (this.course.getFunction().getZ(coord.x+1,coord.y)-this.course.getFunction().getZ(coord.x,coord.y))/((coord.x+1)-coord.x);
            }
        } else {
            slope.x = 0;
        }
        if (this.course.getFunction().getZ(coord.x,coord.y-1) != this.course.getFunction().getZ(coord.x,coord.y) &&
                (this.course.getFunction().getZ(coord.x,coord.y+1) != (this.course.getFunction().getZ(coord.x,coord.y)))) {
            if (this.course.getFunction().getZ(coord.x,coord.y-1) < this.course.getFunction().getZ(coord.x,coord.y+1)) {
                slope.y = (float) (this.course.getFunction().getZ(coord.x,coord.y-1)-this.course.getFunction().getZ(coord.x,coord.y))/((coord.y-1) - coord.y);
            } else {
                slope.y = (float) (this.course.getFunction().getZ(coord.x,coord.y+1)-this.course.getFunction().getZ(coord.x,coord.y))/((coord.y+1) - coord.y);
            }
        } else {
            slope.y = 0;
        }
        return slope;
    }

}
