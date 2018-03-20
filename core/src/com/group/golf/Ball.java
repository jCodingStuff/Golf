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

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
