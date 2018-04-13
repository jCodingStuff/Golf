package com.group.golf.math;

/**
 * @author Julian Marrades
 * @version 0.1, 13-04-218
 * A class to represent a line in the XY plane
 */
public class Line2D {

    private double m;
    private double k;

    /**
     * Create a new instance of line
     * @param m the slope
     * @param k the y-intercept
     */
    public Line2D(double m, double k) {
        this.m = m;
        this.k = k;
    }


    // GETTER AND SETTER AREA

    /**
     * Get the slope of the line
     * @return the slope of the line
     */
    public double getM() {
        return m;
    }

    /**
     * Set a new slope to the line
     * @param m the new slope
     */
    public void setM(double m) {
        this.m = m;
    }

    /**
     * Get the y-intercept of the line
     * @return the y-intercept
     */
    public double getK() {
        return k;
    }

    /**
     * Set a new y-intercept to the line
     * @param k the new y-intercept
     */
    public void setK(double k) {
        this.k = k;
    }
}
