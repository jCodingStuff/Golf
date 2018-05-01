package com.group.golf.Physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.math.Computable;

/**
 * Created by alex_ on 21-Mar-18.
 * A class to hold the Physics engine for the Crazy Golf game
 */

public class Physics {
    private Course course;
    private Ball ball;
    private Vector2 hitCoord;
    private Computable function;
    private double[] solutions;
    private final double STEP_SIZE = 0.25;


    private static final double H = 10e-7;

    /**
     * Construct a Physics engine
     *
     * @param course the course to analyze
     * @param ball   the ball that will roll on the course
     */
    public Physics(Course course, Ball ball) {
        this.course = course;
        this.ball = ball;
        this.hitCoord = new Vector2();
    }


    /**
     * differential equation solver, using the fourth and third order Runge-Kutta method to solve the double variable
     * ODEs.
     * @param x     the x variable, containing a double.
     * @param y     the y variable, containing a double. 
     */

    public void ODEsolver(double x, double y) {
        //Runge-Kutta method third. no init
        for (int i = 0; i <= 4; i += STEP_SIZE) {
            //      this.solutions[i] = RK3(x,y,STEP_SIZE);
                    this.solutions[i] = RK4(x,y,STEP_SIZE);
        }
        System.out.println(this.solutions);
    }

    public double RK3(double x, double y, double h) {
        double k1 = h * function.getZ(x,y);
        double k2 = h * function.getZ(x + (h / 3), y + (k1 / 3));
        double k3 = h * function.getZ(x + h * (2 / 2), y + k2 * (2 / 3));
        y = y + (k1 + 3 * k3) / 4;
        return y;
    }

    public double RK4(double x, double y, double h){
        double k1 = h * function.getZ(x,y);
        double k2 = h * function.getZ(x + (h/2), y + (k1 / 2));
        double k3 = h * function.getZ(x + (h/2), y + (k2 / 2));
        double k4 = h * function.getZ(x  + h, y + k3);
        y = y + (k1/6) + (k2/3) + (k3/3) + (k4/6);
        return y;
    }


    //angle between force and y - basis

    /**
     * Hit the ball
     * @param forceX the velocityX done to the ball
     * @param forceY the velocityY done to the ball
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
     * @param delta delta time
     */
    public void movement(float delta) {
        double[] grav = gravForce(ball, new Vector2((float) ball.getX(),(float) ball.getY()));
        double[] friction =  frictionForce(ball,ball.getVelocityX(),ball.getVelocityY());

        ball.setX((float) (ball.getX() + delta * ball.getVelocityX()));
        ball.setY((float) (ball.getY() + delta * ball.getVelocityY()));

        ball.setVelocityX(ball.getVelocityX() + delta * (grav[0] + friction[0]));
        ball.setVelocityY(ball.getVelocityY() + delta * (grav[1] + friction[1]));

        if (Math.abs(this.ball.getVelocityX()) < 0.01 && Math.abs(this.ball.getVelocityY()) < 0.01) {
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
    public double[] gravForce(Ball ball, Vector2 coord) {
        double multiplier = - this.course.getG();
        double[] slopeMultiplier = calculateSlope(coord);
        return new double[] {multiplier * slopeMultiplier[0],multiplier * slopeMultiplier[1]};
    }

    /**
     * Compute the slope of the course in a specific point
     * @param coord the Vector2 containing the position to analize
     * @return a Vector2 instance containing the slope at the point provided
     */
    public double[] calculateSlope(Vector2 coord) {
        double[] slope = new double[2];

        double step = 0.001;

        slope[0] = (this.course.getHeight(coord.x-step,coord.y) - this.course.getHeight(coord.x+step,coord.y))/(2*step);
        slope[1] = ((this.course.getHeight(coord.x,coord.y-step) - this.course.getHeight(coord.x,coord.y+step))/(2*step));
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
