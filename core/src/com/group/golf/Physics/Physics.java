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
import java.util.List;

/**
 * Created by alex_ on 21-Mar-18.
 * A class to hold the Physics engine for the Crazy Golf game
 */

public class Physics {

    private Course course;
    private Collision collision;
    private boolean water;
    private boolean wall_stop;
    private List<Rectangle> walls;

    private static final Sound loseSound = Gdx.audio.newSound(Gdx.files.internal("defeat_2.wav"));

    private Ball ball;

    public static float[] hitCoord;

    /**
     * Construct a Physics engine
     *
     * @param course the course to analyze
     */
    public Physics(Course course) {
        this.course = course;
        this.hitCoord = new float[2];
        this.collision = new Collision(this.course);
        this.walls = course.getWalls();

    }

    public Physics(Physics other) {
        this.course = other.course;
        this.collision = other.collision;
        this.water = other.water;
        this.walls = course.getWalls();
    }


    /**
     * Hit the ball
     * @param xLength the length that the mouse was dragged horizontally
     * @param yLength the length that the mouse was dragged vertically
     */
    public void hit(Ball ball, float xLength, float yLength) {
        this.ball = ball;
        this.collision.setBall(this.ball);
        water = false;

        hitCoord[0] = ball.getX();
        hitCoord[1] = ball.getY();

        this.ball.setVelocityX(xLength);
        this.ball.setVelocityY(yLength);

        this.ball.limit(this.course.getVmax());

    }

    public void movement(float delta, boolean simulation) {
        System.out.println("No differential equation created");
    }

    public void checkCollision(boolean simulation) {
        float[] ballPixels = MathLib.toPixel(new float[]{this.ball.getX(), this.ball.getY()}, this.course.getOffsets(),
                this.course.getScales());
        this.collision.checkForWalls(ballPixels[0], ballPixels[1]);
        if (!this.collision.checkForGraphicWalls(ballPixels[0], ballPixels[1], walls)) this.wall_stop = true;

        if (this.collision.ballInWater()) {
            ball.reset();
            this.ball.setX(hitCoord[0]);
            this.ball.setY(hitCoord[1]);
            if (!simulation) loseSound.play(0.2f);
            water = true;
        }
    }

    public float[] acceleration(float[] coord, float[] velocities) {
        float[] gravForce = gravForce(coord);
        float[] frictionForce = frictionForce(velocities[0],velocities[1]);
        return new float[]{gravForce[0]+frictionForce[0],gravForce[1]+frictionForce[1]};
    }


    /**
     * Compute the friction force that oposes to the movement of the ball
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

        float step = 0.001f;

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

    public boolean isGoalAchieved(Ball ball) {
        collision.setBall(ball);
        return collision.isGoalAchieved();
    }

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


    public boolean isWater() {
        if (water)  {
            water = false;
            return true;
        }
        return false;
    }

    public void setWater(boolean water) {
        this.water = water;
    }
    
    public void setWalls(List<Rectangle> walls) {
    	this.walls = walls;
    }

    public Collision getCollision() {
        return collision;
    }

    public void setCollision(Collision collision) {
        this.collision = collision;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }
}
