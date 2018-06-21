package com.group.golf;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;


/**
 * A class to save the data of the ball
 */
public class Ball {

    public static final float RADIUS = 10;


    private float mass;
    private float x;
    private float y;
    private float velocityX;
    private float velocityY;

    private Texture texture;
    private Circle collisionCircle;

    /**
     * Construct a new Ball object
     * @param mass the mass of the ball
     * @param x the x coordinate of the ball
     * @param y the y coordinate of the ball
     */
    public Ball(float mass, float x, float y) {
        this.mass = mass;
        this.x = x;
        this.y = y;
        this.velocityX = 0;
        this.velocityY = 0;
        this.collisionCircle = new Circle((float) this.x, (float) this.y, RADIUS);
    }

    /**
     * Create a default ball with position at [0, 0]
     * @param mass the mass of the ball
     */
    public Ball(float mass) {
        this.mass = mass;
        this.x = 0;
        this.y = 0;
        this.velocityX = 0;
        this.velocityY = 0;
        this.collisionCircle = new Circle((float) this.x, (float) this.y, RADIUS);
    }

    /**
     * Create a new Ball using another one as a template
     * @param other the template ball
     */
    public Ball(Ball other) {
        this.mass = other.mass;
        this.x = other.x;
        this.y = other.y;
        this.velocityX = other.velocityX;
        this.velocityY = other.velocityY;
        this.collisionCircle = new Circle((float) this.x, (float) this.y, RADIUS);
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
    public void limit(float max) {
        double velocity = this.calcVelocity();
        if (velocity > max) {
            this.velocityX /= velocity;
            this.velocityY /= velocity;
            this.velocityX *= max;
            this.velocityY *= max;
        }
    }

    public void render(Batch batch, double realX, double realY) {
        batch.begin();
        batch.draw(this.texture, (float) realX - RADIUS,
                (float) realY - RADIUS, RADIUS * 2,RADIUS * 2);
        batch.end();
    }

    public void invertVelocityX() {
        this.velocityX = -this.velocityX;
    }

    public void invertVelocityY() {
        this.velocityY = -this.velocityY;
    }

    /**
     * Update the collsionCircle
     */
    private void updateCollisionCircle() {
        this.collisionCircle.setX((float) this.x);
        this.collisionCircle.setY((float) this.y);
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
    public float getVelocityX() {return velocityX;
    }

    /**
     * Set a new value for the x-component of the velocity
     * @param velocityX the new x-component of the velocity
     */
    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    /**
     * Get access to the y-component of the velocity
     * @return the y-component of the velocity
     */
    public float getVelocityY() {
        return velocityY;
    }

    /**
     * Set a new value for the y-component of the velocity
     * @param velocityY the new y-component of the velocity
     */
    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    /**
     * Get access to the mass
     * @return the mass
     */
    public float getMass() {
        return mass;
    }

    /**
     * Set a new mass
     * @param mass the new mass
     */
    public void setMass(float mass) {
        this.mass = mass;
    }

    /**
     * Get access to the x-position
     * @return the x-coordinate for position
     */
    public float getX() {
//        if (super.tail == null)
            return x;
//        return super.last()[0];
    }




    /**
     * Set a new x-position
     * @param x the new x-position
     */
    public void setX(float x) {
        this.x = x;
        this.updateCollisionCircle();
    }

    /**
     * Get access to the y-position
     * @return the y-coordinate for position
     */
    public float getY() {
//        if (super.tail == null)
            return y;
//        return super.last()[1];
    }

    /**
     * Set a new value for the y-position
     * @param y the new y-position
     */
    public void setY(float y) {
        this.y = y;
        this.updateCollisionCircle();
    }

    /**
     * Set a new position for the ball
     * @param x the new x-position
     * @param y the new y-position
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        this.updateCollisionCircle();
    }

    public void setCoords(float[] coords) {
        setX(coords[0]);
        setY(coords[1]);
    }

    public void setVelocities(float[] velocities) {
        setVelocityX(velocities[0]);
        setVelocityY(velocities[1]);
    }



    public Circle getCollisionCircle() {
        return collisionCircle;
    }
}
