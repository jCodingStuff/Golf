package com.group.golf;

import com.group.golf.math.Function;

/**
 * This class represents a course for the Golf Game
 */
public class Course {

    private double g;
    private double mu;
    private double vmax;
    private double[] start;
    private double[] goal;
    private double tolerance;
    private Function function;

    /**
     * Create a new Course for Golf game
     * @param function the 3D function that represents the course physically z = f(x, y)
     * @param g the gravity
     * @param mu the friction coefficient
     * @param vmax the terminal velocity
     * @param start the start coordinates
     * @param goal the goal coordinates
     * @param tolerance the radius of the goal
     */
    public Course(Function function, double g, double mu, double vmax, double[] start, double[] goal, double tolerance) {
        this.g = g;
        this.mu = mu;
        this.vmax = vmax;
        this.start = start;
        this.goal = goal;
        this.tolerance = tolerance;
        this.function = function;
    }

    /**
     * Get access to the gravity
     * @return the gravity
     */
    public double getG() {
        return g;
    }

    /**
     * Set a new value for the gravity
     * @param g the new gravity
     */
    public void setG(double g) {
        this.g = g;
    }

    /**
     * Get access to the friction coefficient
     * @return the friction coefficient
     */
    public double getMu() {
        return mu;
    }

    /**
     * Set a new value for the friction coefficient
     * @param mu the new friction coefficient
     */
    public void setMu(double mu) {
        this.mu = mu;
    }

    /**
     * Get access to the terminal velocity
     * @return the terminal velocity
     */
    public double getVmax() {
        return vmax;
    }

    /**
     * Set a new value for the terminal velocity
     * @param vmax the new terminal velocity
     */
    public void setVmax(double vmax) {
        this.vmax = vmax;
    }

    /**
     * Get access to the start coordinates
     * @return the start coordinates
     */
    public double[] getStart() {
        return start;
    }

    /**
     * Set a new set of start coordinates
     * @param start the new start coordinates
     */
    public void setStart(double[] start) {
        this.start = start;
    }

    /**
     * Get access to the goal coordinates
     * @return the goal coordinates
     */
    public double[] getGoal() {
        return goal;
    }

    /**
     * Set a new set of goal coordinates
     * @param goal the new goal coordinates
     */
    public void setGoal(double[] goal) {
        this.goal = goal;
    }

    /**
     * Get access to the goal tolerance
     * @return the goal tolerance
     */
    public double getTolerance() {
        return tolerance;
    }

    /**
     * Set a new value for the goal tolerance
     * @param tolerance the new goal tolerance
     */
    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    /**
     * Get access to the function
     * @return the function
     */
    public Function getFunction() {
        return function;
    }

    /**
     * Set a new function
     * @param function the formula of the function
     */
    public void setFunction(Function function) {
        this.function = function;
    }

    @Override
    public String toString() {
        String message = this.getClass().getName() + " [" + this.function + ", start=" + arrToStr(this.start) + ", ";
        message += "goal=" + arrToStr(this.goal) + ", tolerance=" + this.tolerance + ", g=" + this.g + ", mu=" + this.mu;
        message += ", vmax=" + this.vmax + "]";
        return message;
    }

    private static String arrToStr(double[] array) {
        String message = "[";
        for (int i = 0; i < array.length; i++) {
            message += array[i];
            if (i == array.length - 1) message += ", ";
        }
        message += "]";
        return message;
    }
}
