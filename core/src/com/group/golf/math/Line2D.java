package com.group.golf.math;

/**
 * @author Julian Marrades
 * @version 0.1, 13-04-2018
 * A class to represent a line in the XY plane
 */
public class Line2D {

    private float m;
    private float k;

    /**
     * Create a new instance of Line2D given the slope and y-intercept
     * @param m the slope
     * @param k the y-intercept
     */
    public Line2D(float m, float k) {
        this.m = m;
        this.k = k;
    }

    /**
     * Create new instance of Line2D given the slope and a point
     * @param m the slope
     * @param x a x-coordinate
     * @param y a y-coordinate
     */
    public Line2D(float m, float x, float y) {
        this.m = m;
        this.k = y - this.m * x;
    }

    /**
     * Create a new instance of Line2D given two points
     * @param x1 first x-coordinate
     * @param y1 first y-coordinate
     * @param x2 second x-coordinate
     * @param y2 second y-coordinate
     */
    public Line2D(float x1, float y1, float x2, float y2) {
        this.m = (y2 - y1) / (x2 - x1);
        this.k = y1 - this.m * x1;
    }

    /**
     * Create a new instance of Line2D given two points
     * @param a instance of Point3D
     * @param b instance of Point3D
     */
    public Line2D(Point3D a, Point3D b) {
        this.m = (b.getY() - a.getY()) / (b.getX() - a.getX());
        this.k = a.getY() - this.m * a.getX();
    }

    /**
     * Check if a point is above a line
     * @param point the point to check
     * @return true if the point is above the line, false otherwise
     */
    public boolean isAbove(Point3D point) {
        return point.getY() >= this.getY(point.getX());
    }

    /**
     * Check if a point is under a line
     * @param point the point to check
     * @return true if the point is under the line, false otherwise
     */
    public boolean isUnder(Point3D point) {
        return point.getY() <= this.getY(point.getX());
    }

    /**
     * Check if a point is on the right of a line
     * @param point the point to check
     * @return true if the point on the right of the line the line, false otherwise
     */
    public boolean isRight(Point3D point) {
        return point.getX() >= this.getX(point.getY());
    }

    /**
     * Check if a point is on the left of a line
     * @param point the point to check
     * @return true if the point on the left of the line the line, false otherwise
     */
    public boolean isLeft(Point3D point) {
        return point.getX() <= this.getX(point.getY());
    }

    // GETTER AND SETTER AREA
    /**
     * Get the y-value of the line at a given x
     * @param x the x-coordinate in which the line will be evaluated
     * @return the y-value for the specified x
     */
    public float getY(float x) {
        return this.m * x + this.k;
    }

    /**
     * Get the x-value of the line at a given y
     * @param y the y-coordinate in which the line will be evaluated
     * @return the x-value for the specified y
     */
    public float getX(float y) {
        return (y - this.k) / this.m;
    }

    /**
     * Get the slope of the line
     * @return the slope of the line
     */
    public float getM() {
        return m;
    }

    /**
     * Set a new slope to the line
     * @param m the new slope
     */
    public void setM(float m) {
        this.m = m;
    }

    /**
     * Get the y-intercept of the line
     * @return the y-intercept
     */
    public float getK() {
        return k;
    }

    /**
     * Set a new y-intercept to the line
     * @param k the new y-intercept
     */
    public void setK(float k) {
        this.k = k;
    }
}
