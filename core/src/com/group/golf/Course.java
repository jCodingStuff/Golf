package com.group.golf;

import com.badlogic.gdx.math.MathUtils;
import com.group.golf.math.BicubicInterpolator;
import com.group.golf.math.Computable;
import com.group.golf.math.Point3D;

/**
 * This class represents a course for the Golf Game
 */
public class Course {

    private double g;
    private double mu;
    private double vmax;
    private double[] start;
    private double[] goal;
    
    private double[] start2;
    private double[] goal2;
    
    private double tolerance;
    private Computable[][] functions;

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
    public Course(Computable[][] functions, double g, double mu, double vmax, double[] start, double[] goal,
                  double tolerance) {
        this.g = g;
        this.mu = mu;
        this.vmax = vmax;
        this.start = start;
        this.goal = goal;
        this.tolerance = tolerance;
        this.functions = functions;
        
        this.start2 = null;
        this.goal2 = null;
    }

    public Course(Computable[][] functions, double g, double mu, double vmax, double[] start, double[] start2, double[] goal, double[] goal2,
            double tolerance) {
  this.g = g;
  this.mu = mu;
  this.vmax = vmax;
  this.start = start;
  this.goal = goal;
  
  this.start2 = start2;
  this.goal2 = goal2;
  
  
  this.tolerance = tolerance;
  this.functions = functions;
}
    /**
     * Check if the course is a spline
     * @return true if it is spline, false otherwise
     */
    public boolean isSpline() {
        return this.functions[0][0].getClass() == BicubicInterpolator.class;
    }

    /**
     * Get the computable that covers x and y
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the computable whose domain includes the point (x, y)
     */
    public Computable getFunctionFor(double x, double y) {
        //System.out.println("Coordinate to compute: " + x + ", " + y);
        BicubicInterpolator botLeftSquare = (BicubicInterpolator) this.functions[0][0];
        Point3D botLeftPoint = botLeftSquare.getPoints()[0][0];
        int i = (int)(x - botLeftPoint.getX());
        int j = (int)(y - botLeftPoint.getY());
        //System.out.println("Indices:" + i + ", " + j);
        if (i >= this.functions.length) i--;
        if (j >= this.functions[i].length) j--;
        return this.functions[i][j];
    }

    /**
     * Get the height of the course on a given coordinate
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the height at (x, y)
     */
    public double getHeight(double x, double y) {
        if (this.isSpline()) {
            return this.getFunctionFor(x, y).getZ(x, y);
        } else {
            return this.functions[0][0].getZ(x, y);
        }
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
    
    public double[] getStart2() {
        return start2;
    }

    /**
     * Set a new set of start coordinates
     * @param start the new start coordinates
     */
    public void setStart2(double[] start2) {
        this.start2 = start2;
    }

    /**
     * Get access to the goal coordinates
     * @return the goal coordinates
     */
    public double[] getGoal2() {
        return goal2;
    }

    /**
     * Set a new set of goal coordinates
     * @param goal the new goal coordinates
     */
    public void setGoal2(double[] goal2) {
        this.goal2 = goal2;
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
     * Get access to the computables
     * @return the computables
     */
    public Computable[][] getFunctions() {
        return functions;
    }

    /**
     * Get access to the function, if the course is not a spline
     * @return the function that defines the entire course
     */
    public Computable getFunction() {
        return functions[0][0];
    }

    /**
     * Set a new group of computables
     * @param functions the formula of the computables
     */
    public void setFunctions(Computable[][] functions) {
        this.functions = functions;
    }

    /**
     * Get the distance between the start and goal
     * @return the euclidean disctance between start and goal
     */
    public double getDistance() {
        double dist = Math.sqrt(Math.pow(this.goal[0] - this.start[0], 2) + Math.pow(this.goal[1] - this.start[1], 2));
        return Math.abs(dist);
    }
    
    public double getDistance2() {
        double dist2 = Math.sqrt(Math.pow(this.goal2[0] - this.start2[0], 2) + Math.pow(this.goal2[1] - this.start2[1], 2));
        return Math.abs(dist2);
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
