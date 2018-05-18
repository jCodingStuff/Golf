package com.group.golf.Physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.math.Computable;

import java.util.ArrayList;

/**
 * Created by alex_ on 21-Mar-18.
 * A class to hold the Physics engine for the Crazy Golf game
 */

public class Physics {

    private Course course;
    private Ball ball;
    private double[] hitCoord;

    /**
     * Construct a Physics engine
     *
     * @param course the course to analyze
     * @param ball   the ball that will roll on the course
     */
    public Physics(Course course, Ball ball) {
        this.course = course;
        this.ball = ball;
        this.hitCoord = new double[2];
    }


    //https://www.haroldserrano.com/blog/visualizing-the-runge-kutta-method
    public double[] RK4(double h){

        double[] grav = gravForce(ball, new double[]{ball.getX(),ball.getY()});
        double[] friction =  frictionForce(ball,ball.getVelocityX(),ball.getVelocityY());


        double v1x = ball.getVelocityX();       double v1y = ball.getVelocityY();

        double a1x = grav[0] + friction[0];     double a1y = grav[1] + friction[1];
        double v2x = v1x + a1x * h/2;           double v2y = v1y + a1y * h/2;

        friction =  frictionForce(ball,v2x,v2y);
        double a2x = grav[0] + friction[0];     double a2y = grav[1] + friction[1];
        double v3x = v1x + a2x * h/2;           double v3y = v1y + a2y * h/2;

        friction = frictionForce(ball,v3x,v3y);
        double a3x = grav[0] + friction[0];     double a3y = grav[1] + friction[1];
        double v4x = v1x + h * a3x;             double v4y = v1y + h * a3y;

        friction = frictionForce(ball,v4x,v4y);
        double a4x = grav[0] + friction[0];     double a4y = grav[1] + friction[1];

        double[] resV = {v1x + h/6 * (a1x + 2*a2x + 2*a3x + a4x), v1y + h/6 * (a1y + 2*a2y + 2*a3y + a4y)};
        return resV;

    }



    /**
     * Hit the ball
     * @param forceX the velocityX done to the ball
     * @param forceY the velocityY done to the ball
     */
    public void hit(double forceX, double forceY) {

        hitCoord[0] = ball.getX();
        hitCoord[1] = ball.getY();

        ball.setVelocityX(Gdx.graphics.getDeltaTime() * forceX / ball.getMass());
        ball.setVelocityY(Gdx.graphics.getDeltaTime() * forceY / ball.getMass());
    }

    /**
     * Update the ball state to the following instance of time
     * @param delta delta time
     */
    public void movement(float delta, boolean debug) {

        ball.setX(ball.getX() + delta * ball.getVelocityX());
        ball.setY(ball.getY() + delta * ball.getVelocityY());

        double[] vel = RK4(delta);
        ball.setVelocityX(vel[0]);
        ball.setVelocityY(vel[1]);

        if (debug) System.out.println("VelocityX: " + ball.getVelocityX() + "   VelocityY: " + ball.getVelocityY());
        if (Math.abs(this.ball.getVelocityX()) < 0.0776 && Math.abs(this.ball.getVelocityY()) < 0.0776) {
            this.ball.reset();
        }

    }

    /**
     * Compute the friction force that oposes to the movement of the ball
     * @param ball the ball rolling
     * @param velocityX the x-component of the velocity of the ball
     * @param velocityY the y-component of the velocity of the ball
     * @return a Vector2 instace containig the friction force
     */
    public double[] frictionForce(Ball ball,double velocityX, double velocityY) {
        double multiplier = - this.course.getMu() * this.course.getG()
                / normalLength(velocityX,velocityY);
        return new double[]{(multiplier * velocityX) ,(multiplier * velocityY)};
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
    public double[] gravForce(Ball ball, double[] coord) {
        double multiplier = - this.course.getG();
        double[] slopeMultiplier = calculateSlope(coord);
        return new double[] {multiplier * slopeMultiplier[0],multiplier * slopeMultiplier[1]};
    }

    /**
     * Compute the slope of the course in a specific point
     * @param coord the Vector2 containing the position to analize
     * @return a Vector2 instance containing the slope at the point provided
     */
    public double[] calculateSlope(double[] coord) {
        double[] slope = new double[2];

        double step = 0.001;

        slope[0] = (this.course.getHeight(coord[0]+step,coord[1]) - this.course.getHeight(coord[0]-step,coord[1]))/(2*step);
        slope[1] = ((this.course.getHeight(coord[0],coord[1]+step) - this.course.getHeight(coord[0],coord[1]-step))/(2*step));
//
//        if (this.course.getHeight(coord.x-H,coord.y) != this.course.getHeight(coord.x,coord.y) &&
//                (this.course.getHeight(coord.x+H,coord.y) != this.course.getHeight(coord.x,coord.y))) {
//            if (this.course.getHeight(coord.x-H,coord.y) < this.course.getHeight(coord.x+H,coord.y)) {
//                slope.x = (float) ((this.course.getHeight(coord.x-H,coord.y)-this.course.getHeight(coord.x,coord.y))/((coord.x-H)-coord.x));
//            } else {
//                slope.x = (float) ((this.course.getHeight(coord.x+H,coord.y)-this.course.getHeight(coord.x,coord.y))/((coord.x+H)-coord.x));
//            }
//        } else {
//            slope.x = 0;
//        }
//        if (this.course.getHeight(coord.x,coord.y-H) != this.course.getHeight(coord.x,coord.y) &&
//                (this.course.getHeight(coord.x,coord.y+H) != (this.course.getHeight(coord.x,coord.y)))) {
//            if (this.course.getHeight(coord.x,coord.y-H) < this.course.getHeight(coord.x,coord.y+H)) {
//                slope.y = (float) ((this.course.getHeight(coord.x,coord.y-H)-this.course.getHeight(coord.x,coord.y))/((coord.y-H) - coord.y));
//            } else {
//                slope.y = (float) ((this.course.getHeight(coord.x,coord.y+H)-this.course.getHeight(coord.x,coord.y))/((coord.y+H) - coord.y));
//            }
//        } else {
//            slope.y = 0;
//        }
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
    public double[] getHitCoord() {
        return hitCoord;
    }

    /**
     * Set a new value for the Vector2 instance
     * @param hitCoord the new Vector2 instance
     */
    public void setHitCoord(double[] hitCoord) {
        this.hitCoord = hitCoord;
    }
}
