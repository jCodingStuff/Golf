package com.group.golf;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.group.golf.math.MathLib;
import com.group.golf.screens.CourseScreen;

/**
 * A class to save the data of the ball
 */
public class Ball {

    public static final float RADIUS = 10;

    private double mass;
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;

    private Texture texture;
    private CourseScreen courseScreen;

    /**
     * Construct a new Ball object
     * @param mass the mass of the ball
     * @param x the x coordinate of the ball
     * @param y the y coordinate of the ball
     */
    public Ball(double mass, double x, double y) {
        this.mass = mass;
        this.x = x;
        this.y = y;
        this.velocityX = 0;
        this.velocityY = 0;
    }

    /**
     * Create a default ball with position at [0, 0]
     * @param mass the mass of the ball
     */
    public Ball(double mass) {
        this.mass = mass;
        this.x = 0;
        this.y = 0;
        this.velocityX = 0;
        this.velocityY = 0;
    }

    /**
     * Reset the velocity of the ball to 0
     */
    public void reset() {
        this.velocityX = 0;
        this.velocityY = 0;
    }

    /**
     * Limit the velocity to a maximum
     * @param max the limit
     */
    public void limit(double max) {
        double velocity = this.calcVelocity();
        if (velocity > max) {
            this.velocityX /= velocity;
            this.velocityY /= velocity;
            this.velocityX *= max;
            this.velocityY *= max;
        }
    }

    public void render() {
        this.courseScreen.getGame().batch.begin();
        double[] offsets = new double[]{this.courseScreen.getXoffset(), this.courseScreen.getYoffset()};
        double[] real = MathLib.toPixel(new double[]{this.x, this.y}, offsets, this.courseScreen.getScale());
        this.courseScreen.getGame().batch.draw(this.texture, (float) real[0] - RADIUS,
                (float) real[1] - RADIUS, RADIUS * 2,RADIUS * 2);
        this.courseScreen.getGame().batch.end();
    }

    /**
     * Set a course screen for the ball
     * @param courseScreen the new courseScreen
     */
    public void setCourseScreen(CourseScreen courseScreen) {
        this.courseScreen = courseScreen;
    }

    /**
     * Set a texture for the ball
     * @param texture the new texture of the ball
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Guess if the ball is moving
     * @return true if it is moving, false otherwise
     */
    public boolean isMoving() {
        return this.velocityX != 0 || this.velocityY != 0;
    }

    /**
     * Compute the velocity modulus
     * @return the value for the velocity modulus
     */
    public double calcVelocity() {
        return Math.sqrt(Math.pow(this.velocityX, 2) + Math.pow(this.velocityY, 2));
    }

    /**
     * Get access to the x-component of the velocity
     * @return the x-component of the velocity
     */
    public double getVelocityX() {
        return velocityX;
    }

    /**
     * Set a new value for the x-component of the velocity
     * @param velocityX the new x-component of the velocity
     */
    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    /**
     * Get access to the y-component of the velocity
     * @return the y-component of the velocity
     */
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * Set a new value for the y-component of the velocity
     * @param velocityY the new y-component of the velocity
     */
    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    /**
     * Get access to the mass
     * @return the mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * Set a new mass
     * @param mass the new mass
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * Get access to the x-position
     * @return the x-coordinate for position
     */
    public double getX() {
        return x;
    }


    /**
     * Set a new x-position
     * @param x the new x-position
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Get access to the y-position
     * @return the y-coordinate for position
     */
    public double getY() {
        return y;
    }

    /**
     * Set a new value for the y-position
     * @param y the new y-position
     */
    public void setY(double y) {
        this.y = y;
    }
}
