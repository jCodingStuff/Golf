package com.group.golf.Physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.math.MathLib;
import com.group.golf.math.Point3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A major class for the physics engine for the Golf game
 * @author Alexandros Chimonas
 * @author Martijn Hilders
 */
public class Physics {

    private Course course;
    private Collision collision;
    private boolean water;
    private boolean wall_stop;
    private List<Rectangle> walls;
    static float[][] repeatChecker;
    protected float errorBound;

    protected static float[] hitCoord;
    private static final Sound loseSound = Gdx.audio.newSound(Gdx.files.internal("defeat_2.wav"));

    private Ball movingBall;


    /**
     * Construct a Physics engine
     * @param course the course to analyze
     */
    public Physics(Course course) {
        this.course = course;
        this.hitCoord = new float[2];
        this.collision = new Collision(this.course);
        this.walls = course.getWalls();
    }

    /**
     * Construct a Physics engine from a template
     * @param other another Physics engine acting as a tempalte
     */
    public Physics(Physics other) {
        this.course = other.course;
        this.collision = other.collision;
        this.water = other.water;
        this.walls = course.getWalls();
        this.repeatChecker = other.repeatChecker;
    }


    /**
     * Hit the ball
     * @param ball the ball to hit
     * @param xLength the length that the mouse was dragged horizontally
     * @param yLength the length that the mouse was dragged vertically
     */
    public void hit(Ball ball, float xLength, float yLength) {
        this.movingBall = ball;
        this.collision.setBall(this.movingBall);
        water = false;

        repeatChecker = new float[0][2];

        hitCoord[0] = ball.getX();
        hitCoord[1] = ball.getY();

        this.movingBall.setVelocityX(xLength);
        this.movingBall.setVelocityY(yLength);

//        System.out.println("HIT COORDS ARE SET x:  " + hitCoord[0] + "   y: " + hitCoord[1]);
        this.movingBall.limit(this.course.getVmax());
    }

    /**
     * Manage the movement of the active ball
     * @param delta step-size
     * @param simulation true if the hit is part of a simulation, false if it is a real hit
     */
    public void movement(float delta, boolean simulation) {
        System.out.println("No differential equation created");
    }

    /**
     * Check collisions
     * @param simulation true if a simulation is taking place, false otherwise
     * @return true if no water has hit, false otherwise
     */
    public boolean checkCollision(boolean simulation) {
        float[] ballPixels = MathLib.toPixel(new float[]{this.movingBall.getX(), this.movingBall.getY()}, this.course.getOffsets(),
                this.course.getScales());
        this.collision.checkForWalls(ballPixels[0], ballPixels[1]);
        this.collision.checkForGraphicWalls(ballPixels[0], ballPixels[1], walls);

        if (this.collision.ballInWater()) {
            movingBall.reset();
            this.movingBall.setX(hitCoord[0]);
            this.movingBall.setY(hitCoord[1]);
            if (!simulation) loseSound.play(0.2f);
            water = true;
            return false;
        }
        return true;
    }

    /**
     * Stop the ball if it meets the conditions
     * @return true if the ball was forced to stop, false otherwise
     */
    protected boolean checkLowVelocity() {
        float[] slope = this.calculateSlope(new float[]{this.movingBall.getX(), this.movingBall.getY()});
        float vx = this.movingBall.getVelocityX();
        float vy = this.movingBall.getVelocityY();
        if (vx != 0 && ((vx < 0 && slope[0] >= 0 && vx >= -errorBound) ||
                (vx > 0 && slope[0] <= 0 && vx <= errorBound))) {
//            System.out.println("VelocityX reset!");
            this.movingBall.resetX();
        }
        if (vy != 0 && ((vy < 0 && slope[1] >= 0 && vy >= -errorBound) ||
                (vy > 0 && slope[1] <= 0 && vy <= errorBound))) {
//            System.out.println("VelocityY reset!");
            this.movingBall.resetY();
        }
        return !this.movingBall.isMoving();
    }

    /**
     * Compute the accelation
     * @param coord the actual position of the ball
     * @param velocities the velocities at that position
     * @return the accelation at that position
     */
    public float[] acceleration(float[] coord, float[] velocities) {
        float[] gravForce = gravForce(coord);
        float[] frictionForce = frictionForce(velocities[0],velocities[1]);
        return new float[]{gravForce[0]+frictionForce[0],gravForce[1]+frictionForce[1]};
    }

    /**
     * Compute the friction force that opposes to the movement of the ball
     * @param velocityX the x-component of the velocity of the ball
     * @param velocityY the y-component of the velocity of the ball
     * @return a Vector2 instace containig the friction force
     */
    public float[] frictionForce(float velocityX, float velocityY) {
        float multiplier = - (this.course.getMu() * this.course.getG())
                / (float) normalLength(velocityX,velocityY);
//        System.out.println(multiplier * velocityX);
        return new float[]{(multiplier * velocityX) ,(multiplier * velocityY)};
    }

    /**
     * Compute the modulus of the velocity of the ball
     * @param velocityX the x-component of the velocity
     * @param velocityY the y-component of the velocity
     * @return the modulus of the vector formed by X and Y components
     */
    public double normalLength(float velocityX, float velocityY) {
        return (Math.sqrt(Math.pow(velocityX,2) + Math.pow(velocityY,2)));
    }

    /**
     * Compute the gravitational force that the ball suffers
     * @param coord the Vector2 containing the actual position of the ball
     * @return a Vector2 instance containing the gravity force
     */
    public float[] gravForce(float[] coord) {
        float multiplier = - (float)this.course.getG();
        float[] slopeMultiplier = calculateSlope(coord);
        return new float[] {multiplier * slopeMultiplier[0],multiplier * slopeMultiplier[1]};
    }

    /**
     * Compute the slope of the course in a specific point
     * @param coord the Vector2 containing the position to analize
     * @return a Vector2 instance containing the slope at the point provided
     */
    public float[] calculateSlope(float[] coord) {
        float[] slope = new float[2];

        float step = 1e-4f;

        slope[0] = ((this.course.getHeight(coord[0]+step,coord[1]) - this.course.getHeight(coord[0]-step,coord[1]))/(2*step));
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
     * Get access to the Vector2 instance
     * @return the Vector2 instance
     */
    public float[] getHitCoord() {
        return hitCoord;
    }

    /**
     * Check if a ball has achieved the goal
     * @param ball the ball to check
     * @return true if the ball has achieved the goal, false otherwise
     */
    public boolean isGoalAchieved(Ball ball) {
        collision.setBall(ball);
        return collision.isGoalAchieved();
    }

    /**
     * Check if a ball has achieved the goal2
     * @param ball the ball to check
     * @return true if the ball has achieved the goal2, false otherwise
     */
    public boolean isGoalAchieved2(Ball ball) {
        collision.setBall(ball);
        return collision.isGoalAchieved2();
    }

    /**
     * Set a new value for the Vector2 instance
     * @param hitCoord the new Vector2 instance
     */
    public void setHitCoord(float[] hitCoord) {
        this.hitCoord = hitCoord;
    }

    /**
     * Check and manage water attribute
     * @return true if water is true, false otherwise
     */
    public boolean isWater() {
        if (water)  {
            water = false;
            return true;
        }
        return false;
    }

    /**
     * Set a new value for the water attribute
     * @param water the new value
     */
    public void setWater(boolean water) {
        this.water = water;
    }

    /**
     * Set a new set of walls to the physics engine
     * @param walls the new set of walls
     */
    public void setWalls(List<Rectangle> walls) {
    	this.walls = walls;
    }

    /**
     * Get access to the Collision instance managed by the physics engine
     * @return the collision instance
     */
    public Collision getCollision() {
        return collision;
    }

    /**
     * Set a new instance of Collision to the physics engine
     * @param collision the new Collision instance
     */
    public void setCollision(Collision collision) {
        this.collision = collision;
    }

    /**
     * Get access to the ball being manipulated by the physics engine
     * @return the active ball
     */
    public Ball getBall() {
        return movingBall;
    }

    /**
     * Set a new ball to the physics engine
     * @param ball the new ball
     */
    public void setBall(Ball ball) {
        this.movingBall = ball;
    }
}
