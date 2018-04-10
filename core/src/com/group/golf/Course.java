package com.group.golf;

import com.group.golf.math.Function;

import java.util.List;

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
    private List<Function> functions;

    /**
     * Create a new Course for Golf game
     * @param functions the 3D function that represents the course physically z = f(x, y), or the splines that build it
     * @param g the gravity
     * @param mu the friction coefficient
     * @param vmax the terminal velocity
     * @param start the start coordinates
     * @param goal the goal coordinates
     * @param tolerance the radius of the goal
     */
    public Course(List<Function> functions, double g, double mu, double vmax, double[] start, double[] goal,
                  double tolerance) {
        this.g = g;
        this.mu = mu;
        this.vmax = vmax;
        this.start = start;
        this.goal = goal;
        this.tolerance = tolerance;
        this.functions = functions;
    }

    /**
     * Check if the course is a spline
     * @return true if it is spline, false otherwise
     */
    public boolean isSpline() {
        return this.functions.size() > 1;
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
    public List<Function> getFunctions() {
        return functions;
    }

    /**
     * Get access to the function, if the course is not a spline
     * @return the function that defines the entire course
     */
    public Function getFunction() {
        return functions.get(0);
    }

    /**
     * Set a new group of functions
     * @param functions the formula of the function
     */
    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    /**
     * Get the distance between the start and goal
     * @return the euclidean disctance between start and goal
     */
    public double getDistance() {
        double dist = Math.sqrt(Math.pow(this.goal[0] - this.start[0], 2) + Math.pow(this.goal[1] - this.start[0], 2));
        return Math.abs(dist);
    }

    @Override
    public String toString() {
        String message = this.getClass().getName() + " [" + this.functions + ", start=" + arrToStr(this.start) + ", ";
        message += "goal=" + arrToStr(this.goal) + ", tolerance=" + this.tolerance + ", g=" + this.g + ", mu=" + this.mu;
        message += ", vmax=" + this.vmax + "]";
        return message;
    }

    /**
     * Get a string representation of a double array
     * @param array the array to represent
     * @return the string representation
     */
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
