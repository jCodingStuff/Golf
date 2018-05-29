package com.group.golf.Physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.math.MathLib;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by alex_ on 21-Mar-18.
 * A class to hold the Physics engine for the Crazy Golf game
 */

public class Physics {

    private Course course;
    private Ball ball;
    private Collision collision;
    private double[] offsets;
    private double[] scales;
    private boolean water;
    private Queue<double[]> tester = new Queue<double[]>();

    public static double[] hitCoord;
    private FileHandle velocities;
    private FileHandle time_steps;
    private FileHandle accelerations;


    /**
     * Construct a Physics engine
     * @param course the course to analyze
     * @param ball the ball that will roll on the course
     */
    public Physics(Course course, Ball ball) {
        this.course = course;
        this.ball = ball;
        this.hitCoord = new double[2];
        this.collision = new Collision(this.ball, this.course);

        velocities = Gdx.files.local("velocitiesRK");
        time_steps = Gdx.files.local("time_stepsRK");
        accelerations = Gdx.files.local("accelerationsRK");
    }

    public Physics(Physics other) {
        this.course = other.course;
        this.ball = other.ball;
        this.collision = other.collision;
        this.offsets = other.offsets;
        this.scales = other.scales;
        this.water = other.water;
    }

    public void hit(double xLength, double yLength) {
        water = false;

        System.out.println("xLength:  " + xLength + "  yLength:  " + yLength);

        hitCoord[0] = ball.getX();
        hitCoord[1] = ball.getY();

        ball.addCoord(hitCoord);

        double frameRate = Gdx.graphics.getDeltaTime();

        System.out.println(course.getVmax());

        this.ball.setVelocityX(xLength/this.ball.getMass());
        this.ball.setVelocityY(yLength/this.ball.getMass());
        this.ball.limit(course.getVmax());
        System.out.println("MOTHERFUCKING: " + ball.getVelocityX());
        double[] velocities = new double[]{this.ball.getVelocityX(),this.ball.getVelocityY()};


        double timer = 0;
        integrate(hitCoord,velocities,frameRate);
        timer += frameRate;

        while (this.ball.isMoving() && timer < 10) {
            velocities = new double[]{this.ball.getVelocityX(),this.ball.getVelocityY()};
            integrate(this.ball.last(),velocities,frameRate);
            timer += frameRate;
        }
    }

    //https://www.haroldserrano.com/blog/visualizing-the-runge-kutta-method
    public void integrate(double[] coord,double[] velocities,  double dt) {

        Derivative k1,k2,k3,k4;
        k1 = evaluate(coord,velocities,dt,new Derivative(0,0,0,0));
        k2 = evaluate(coord,velocities,dt*0.5,new Derivative(k1));
        k3 = evaluate(coord,velocities,dt*0.5,new Derivative(k2));
        k4 = evaluate(coord,velocities,dt,k3);

        double dxdt = 1.0/6.0 * (k1.dx + 2.0*(k2.dx + k3.dx) + k4.dx);
        double dydt = 1.0/6.0 * (k1.dy + 2.0*(k2.dy + k3.dy) + k4.dy);

        double dvxdt = 1.0/6.0 * (k1.dvx + 2.0*(k2.dvx + k3.dvx) + k4.dvx);
        double dvydt = 1.0/6.0 * (k1.dvy + 2.0*(k2.dvy + k3.dvy) + k4.dvy);

        this.ball.limit(this.course.getVmax());

        double[] newCoord = new double[]{coord[0]+dxdt*dt,
                                         coord[1]+dydt*dt};

        ball.addCoord(newCoord);

        double[] newVelocities = new double[]{velocities[0]+dvxdt*dt,
                                              velocities[1]+dvydt*dt};

        ball.setVelocityX(newVelocities[0]);
        ball.setVelocityY(newVelocities[1]);


        double[] ballCoords = MathLib.toPixel(newCoord,offsets,scales);
        this.collision.checkForWalls(ballCoords[0], ballCoords[1]);

        if (this.collision.ballInWater()) {
            ball.reset();
            ball.addCoord(hitCoord);
            water = true;
        }

        System.out.println("x  " + newVelocities[0] + "   y   " + newVelocities[1]);

//        if (Math.abs(this.ball.getVelocityX()) < 0.05 && Math.abs(this.ball.getVelocityY()) < 0.05) {
//            this.ball.reset();
//        }

        if (checkRepetion(velocities)) {
            ball.reset();
        }
    }


    class Derivative {
        double dx;  // dx/dt = velocity
        double dy;
        double dvx;  // dv/dt = acceleration
        double dvy;

        Derivative() {
        }
        Derivative(double dx,double dy,double dvx,double dvy) {
            this.dx = dx;
            this.dy = dy;
            this.dvx = dvx;
            this.dvy = dvy;
        }
        Derivative(Derivative d) {
            dx = d.dx * 0.5;
            dy = d.dy * 0.5;
            dvx = d.dvx * 0.5;
            dvy = d.dvy * 0.5;
        }
    }

    public Derivative evaluate(double[] coord,double[] velocities, double dt, Derivative d) {

        coord[0] = coord[0] + d.dx*dt;
        coord[1] = coord[1] + d.dy*dt;

        Derivative output = new Derivative();
        output.dx = velocities[0] + d.dvx*dt;
        output.dy = velocities[1] + d.dvy*dt;

        double[] helper = acceleration(coord,velocities);
        output.dvx = helper[0];
        output.dvy = helper[1];
        return output;
    }

    public double[] acceleration(double[] coord, double[] velocities) {
        double[] gravForce = gravForce(coord);
        double[] frictionForce = frictionForce(velocities);
        return new double[]{gravForce[0]+frictionForce[0],gravForce[1]+frictionForce[1]};
    }

    public boolean checkRepetion(double[] velocities) {
        velocities[0] = Math.abs(velocities[0]);
        velocities[1] = Math.abs(velocities[1]);
        if (velocities[0] < 1.5 && velocities[1] < 1.5) {
            tester.enqueue(velocities);
            double error = 1e-5;
            if (tester.getSize() == 6) {

                double[][] arrayVel = new double[6][2];
                for (int i = 0; i < 6; i++) {
                    arrayVel[i] = tester.dequeue();
                }
                for (int i = 0; i < 6; i++) {
                    tester.enqueue(arrayVel[i]);
                }

                if (arrayVel[0][0] - arrayVel[2][0] < error && arrayVel[2][0] - arrayVel[4][0] < error &&
                        arrayVel[0][1] - arrayVel[2][1] < error && arrayVel[2][1] - arrayVel[4][1] < error) {
                    System.out.println("forcing stop");
                    tester.clear();
                    for (int i = 0; i < 4; i++)
                        this.ball.dequeue();

                    return true;
                }
                tester.dequeue();
            }
        }
        return false;
    }


    /**
     * Compute the friction force that oposes to the movement of the ball
     * @param velocities an array containing the x - velocities and y - velocities
     * @return a double[] instace containig the friction force
     */
    public double[] frictionForce(double[] velocities) {
        double multiplier = - this.course.getMu() * this.course.getG()
                / normalLength(velocities[0],velocities[1]);
        return new double[]{(multiplier * velocities[0]) ,(multiplier * velocities[1])};
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

        double step = 1e-10;

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


    public void setOffsets(double[] offsets) {
        this.offsets = offsets;
    }

    public void setScales(double[] scales) {
        this.scales = scales;
    }

    public boolean isWater() {
        return water;
    }

    public void setWater(boolean water) {
        this.water = water;
    }

    public void setCollision(Collision collision) {
        this.collision = collision;
    }
}
