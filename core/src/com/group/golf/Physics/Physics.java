package com.group.golf.Physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.math.Function;

/**
 * Created by alex_ on 21-Mar-18.
 * A class to hold the Physics engine for the Crazy Golf game
 */

public class Physics {
    private Course course;
    private Ball ball;
    private Vector2 hitCoord;

    /**
     * Construct a Physics engine
     * @param course the course to analyze
     * @param ball the ball that will roll on the course
     */
    public Physics(Course course, Ball ball) {
        this.course = course;
        this.ball = ball;
        this.hitCoord = new Vector2();
    }


    //angle between force and y - basis

    /**
     * Hit the ball
     * @param velocityX the velocityX done to the ball
     * @param velocityY the velocityY done to the ball
     */
    public void hit(double forceX, double forceY) {
//        double velocityX = forceX * Math.cos(angle);
//        double velocityY = forceY * Math.sin(angle);

        hitCoord.x = (float) ball.getX();
        hitCoord.y = (float) ball.getY();

        ball.setVelocityX(Gdx.graphics.getDeltaTime() * forceX / ball.getMass());
        ball.setVelocityY(Gdx.graphics.getDeltaTime() * forceY / ball.getMass());
    }

    /**
     * Update the ball state to the following instance of time
     */
    public void movement() {
        Vector2 grav = gravForce(ball, new Vector2((float) ball.getX(),(float) ball.getY()));
        Vector2 friction =  frictionForce(ball,ball.getVelocityX(),ball.getVelocityY());

        ball.setX(ball.getX() + Gdx.graphics.getDeltaTime() * ball.getVelocityX());
        ball.setY(ball.getY() + Gdx.graphics.getDeltaTime() * ball.getVelocityY());

        ball.setVelocityX(ball.getVelocityX() + Gdx.graphics.getDeltaTime() * (grav.x + friction.x));
        ball.setVelocityY(ball.getVelocityY() + Gdx.graphics.getDeltaTime() * (grav.y + friction.y));

        if (Math.abs(this.ball.getVelocityX()) < 0.02) this.ball.setVelocityX(0);
        if (Math.abs(this.ball.getVelocityY()) < 0.02) this.ball.setVelocityY(0);

    }

    /**
     * Compute the friction force that oposes to the movement of the ball
     * @param ball the ball rolling
     * @param velocityX the x-component of the velocity of the ball
     * @param velocityY the y-component of the velocity of the ball
     * @return a Vector2 instace containig the friction force
     */
    public Vector2 frictionForce(Ball ball,double velocityX, double velocityY) {
        double multiplier = - this.course.getMu() * this.course.getG()
                / normalLength(velocityX,velocityY);
        return new Vector2((float) (multiplier * velocityX) , (float) (multiplier * velocityY));
    }

    /**
     * Compute the modulus of the velocity of the ball
     * @param velocityX the x-component of the velocity
     * @param velocityY the y-component of the velocity
     * @return the modulus of the vector formed by X and Y components
     */
    public double normalLength(double velocityX, double velocityY) {
        return (Math.sqrt(Math.pow(velocityX,2) + Math.pow(velocityY,2)));
    }

    /**
     * Compute the gravitational force that the ball suffers
     * @param ball the ball rolling
     * @param coord the Vector2 containing the actual position of the ball
     * @return a Vector2 instance containing the gravity force
     */
    public Vector2 gravForce(Ball ball, Vector2 coord) {
        double multiplier = - this.course.getG();
        Vector2 slopeMultiplier = calculateSlope(coord);
        return new Vector2((float) multiplier * slopeMultiplier.x,(float)multiplier * slopeMultiplier.y);
    }

    /**
     * Compute the slope of the course in a specific point
     * @param coord the Vector2 containing the position to analize
     * @return a Vector2 instance containing the slope at the point provided
     */
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

    /**
     * Get access to the Course instance
     * @return the Course instance
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Set a new value for the Course instance
     * @param course the new Course instance
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Get access to the Ball instance
     * @return the Ball instance
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * Set a new value for the Ball instance
     * @param ball the new Ball instance
     */
    public void setBall(Ball ball) {
        this.ball = ball;
    }

    /**
     * Get access to the Vector2 instance
     * @return the Vector2 instance
     */
    public Vector2 getHitCoord() {
        return hitCoord;
    }

    /**
     * Set a new value for the Vector2 instance
     * @param hitCoord the new Vector2 instance
     */
    public void setHitCoord(Vector2 hitCoord) {
        this.hitCoord = hitCoord;
    }
}
