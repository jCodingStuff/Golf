package com.group.golf;

public class Ball {

    private double mass;
    private double x;
    private double y;

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
    }

    /**
     * Create a default ball with position at [0, 0]
     * @param mass the mass of the ball
     */
    public Ball(double mass) {
        this.mass = mass;
        this.x = 0;
        this.y = 0;
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
