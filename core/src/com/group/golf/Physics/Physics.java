package com.group.golf.Physics;

import com.badlogic.gdx.Gdx;
import com.group.golf.Ball;
import com.group.golf.Course;

import java.util.ArrayList;

/**
 * Created by alex_ on 21-Mar-18.
 * A class to hold the Physics engine for the Crazy Golf game
 */

public class Physics {
    private Course course;
    private Ball ball;
    private double[] hitCoord;
    private final int fps = 30;

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
    public void RK4(double h){



        double[][] accel = new double[4][2];
        accel[0] = new double[]{ball.getAccelerationX(),ball.getAccelerationY()};

        double[][] velo = new double[4][2];
        velo[0] = new double[]{ball.getVelocityX(),ball.getVelocityY()};

        for (int i = 1; i < 4; i++) {
            System.out.println(i);
           double[] v = updateRKStep(velo[0],accel[i-1],h,i);
           velo[i] = v;

           double[] p = updateRKStep(new double[]{ball.getX(),ball.getY()},v,h,i);

           double[] grav = gravForce(p);
           double[] friction = frictionForce(v[0],v[1]);

           accel[i] = new double[]{grav[0] + friction[0], grav[1] + friction[1]};
        }

        double accelX = (accel[0][0] + 2 * accel[1][0] + 2 * accel[2][0] + accel[3][0])/6;
        double accelY = (accel[0][1] + 2 * accel[1][1] + 2 * accel[2][1] + accel[3][1])/6;
        ball.setAccelerationX(accelX);
        ball.setAccelerationY(accelY);

        ball.setVelocityX(velo[0][0] + h * accelX);
        ball.setVelocityY(velo[0][1] + h * accelY);

//        System.out.println("VelocityX: " + ball.getVelocityX() + "   VelocityY: " + ball.getVelocityY());
        if (Math.abs(this.ball.getVelocityX()) < 0.0776 && Math.abs(this.ball.getVelocityY()) < 0.0776) {
            this.ball.reset();
        }

        double[] coord = new double[]{ball.getX() + h/6 * (velo[0][0] + 2 * velo[1][0] + 2 * velo[2][0] + velo[3][0]),
                                      ball.getY() + h/6 * (velo[0][1] + 2 * velo[1][1] + 2 * velo[2][1] + velo[3][1])};
        ball.addCoord(coord);

//        double[] grav = gravForce(new double[]{ball.getX(),ball.getY()});
//        double[] friction =  frictionForce(ball.getVelocityX(),ball.getVelocityY());

//        ball.setAccelerationX(accelX);
//        ball.setAccelerationY(accelY);

        //this is going to be removed
//        ball.setX(ball.getX() + h/6 * (velo[0][0] + 2 * velo[1][0] + 2 * velo[2][0] + velo[3][0]));
//        ball.setY(ball.getY() + h/6 * (velo[0][1] + 2 * velo[1][1] + 2 * velo[2][1] + velo[3][1]));
    }

    private double[] updateRKStep(double[] startPos, double[] prev, double h, double k) {
        if (k < 3) {
            return new double[]{startPos[0] + prev[0] * h/2,startPos[1] + prev[1] * h/2};
        } else {
            return new double[]{startPos[0] + prev[0] * h, startPos[1] + prev[1] * h};
        }
    }

    /**
     * Hit the ball
     * @param xLength the length that the mouse was dragged horizontally
     * @param yLength the length that the mouse was dragged vertically
     */
    public void hit(double xLength, double yLength) {

        hitCoord[0] = ball.getX();
        hitCoord[1] = ball.getY();

        double frameRate = 0.04;

        xLength *= 90;
        yLength *= 90;

        ball.setAccelerationX(xLength/ball.getMass());
        ball.setAccelerationY(yLength/ball.getMass());
//        ball.setVelocityX(frameRate * forceX / ball.getMass());
//        ball.setVelocityY(frameRate * forceY / ball.getMass());

        RK4(frameRate);
        while (this.ball.isMoving()) {
            RK4(frameRate);
        }

    }

    /**
     * Update the ball state to the following instance of time
     * @param delta delta time
     */
    public void movement(float delta) {

        RK4(delta);

//        System.out.println("VelocityX: " + ball.getVelocityX() + "   VelocityY: " + ball.getVelocityY());
//        if (Math.abs(this.ball.getVelocityX()) < 0.0776 && Math.abs(this.ball.getVelocityY()) < 0.0776) {
//            this.ball.reset();
//        }

    }

    /**
     * Compute the friction force that oposes to the movement of the ball
     * @param velocityX the x-component of the velocity of the ball
     * @param velocityY the y-component of the velocity of the ball
     * @return a Vector2 instace containig the friction force
     */
    public double[] frictionForce(double velocityX, double velocityY) {
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
     * @param coord the Vector2 containing the actual position of the ball
     * @return a Vector2 instance containing the gravity force
     */
    public double[] gravForce(double[] coord) {
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
