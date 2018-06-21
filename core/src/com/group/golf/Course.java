package com.group.golf;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.group.golf.math.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a course for the Golf Game
 */
public class Course {

    private static final double STEP = 0.001;

    private float g;
    private float mu;
    private float vmax;
    private float[] start;
    private float[] goal;
    
    private float[] start2;
    private float[] goal2;

    private float[] offsets;
    private float[] scales;

    private List<Rectangle> walls; // Pixel coordinates, CAREFUL!
    
    private float tolerance;
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
    public Course(Computable[][] functions, float g, float mu, float vmax, float[] start, float[] goal,
                  float tolerance) {
        this.g = g;
        this.mu = mu;
        this.vmax = vmax;
        this.start = start;
        this.goal = goal;
        this.tolerance = tolerance;
        this.functions = functions;

        this.start2 = null;
        this.goal2 = null;

        // Setup walls
        this.walls = new ArrayList<Rectangle>();
    }

    public Course(Computable[][] functions, float g, float mu, float vmax, float[] start, float[] start2, float[] goal, float[] goal2,
            float tolerance) {
        this.g = g;
        this.mu = mu;
        this.vmax = vmax;
        this.start = start;
        this.goal = goal;
  
        this.start2 = start2;
        this.goal2 = goal2;
  
  
        this.tolerance = tolerance;
        this.functions = functions;

        // Setup walls
        this.walls = new ArrayList<Rectangle>();
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
    public Computable getFunctionFor(float x, float y) {
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
    public float getHeight(float x, float y) {
        if (this.isSpline()) {
            return this.getFunctionFor(x, y).getZ(x, y);
        } else {
            return this.functions[0][0].getZ(x, y);
        }
    }

    public double getWallNum(Point3D closest) {
        // Create a clone of the walls
        List<Rectangle> cloneWalls = new ArrayList<Rectangle>();
        for (Rectangle wall : this.walls) cloneWalls.add(new Rectangle(wall));

        Point3D goalPoint = new Point3D(this.goal[0], this.goal[1], 0);
        Line2D line = new Line2D(goalPoint, closest);

        if (goalPoint.getX() <= closest.getX()) {
            for (float x = goalPoint.getX(); x <= closest.getX(); x += STEP) {
                this.helpWallNum(x, line, cloneWalls);
            }
        } else {
            for (float x = closest.getX(); x <= goalPoint.getX(); x += STEP) {
                this.helpWallNum(x, line, cloneWalls);
            }
        }

        return this.walls.size() - cloneWalls.size();
    }

    private void helpWallNum(float x, Line2D line, List<Rectangle> cloneWalls) {
        float y = line.getY(x);
        float[] coord = MathLib.toPixel(new float[]{x, y}, this.offsets, this.scales);
        float realX = coord[0];
        float realY = coord[1];
        for (int i = cloneWalls.size() - 1; i >= 0; i--) {
            if (cloneWalls.get(i).contains(realX, realY)) {
                cloneWalls.remove(i);
            }
        }
    }
    public boolean isWaterBetween(Point3D a, Point3D b) {
        Line2D path = new Line2D(a, b);
        boolean water = false;
        if (b.getX() >= a.getX()) { // B is on the right of A
            for (float x = a.getX(); x <= b.getX() && !water; x += STEP) {
                if (this.getHeight(x, path.getY(x)) < 0) { // Ball in water
                    water = true;
                }
            }
        } else { // B is on the left of A
            for (float x = b.getX(); x <= a.getX() && !water; x += STEP) {
                if (this.getHeight(x, path.getY(x)) < 0) { // Ball in water
                    water = true;
                }
            }
        }
        return water;
    }

    /**
     * Get access to the gravity
     * @return the gravity
     */
    public float getG() {
        return g;
    }

    /**
     * Set a new value for the gravity
     * @param g the new gravity
     */
    public void setG(float g) {
        this.g = g;
    }

    /**
     * Get access to the friction coefficient
     * @return the friction coefficient
     */
    public float getMu() {
        return mu;
    }

    /**
     * Set a new value for the friction coefficient
     * @param mu the new friction coefficient
     */
    public void setMu(float mu) {
        this.mu = mu;
    }

    /**
     * Get access to the terminal velocity
     * @return the terminal velocity
     */
    public float getVmax() {
        return vmax;
    }

    /**
     * Set a new value for the terminal velocity
     * @param vmax the new terminal velocity
     */
    public void setVmax(float vmax) {
        this.vmax = vmax;
    }

    /**
     * Get access to the start coordinates
     * @return the start coordinates
     */
    public float[] getStart() {
        return start;
    }

    /**
     * Set a new set of start coordinates
     * @param start the new start coordinates
     */
    public void setStart(float[] start) {
        this.start = start;
    }

    /**
     * Get access to the goal coordinates
     * @return the goal coordinates
     */
    public float[] getGoal() {
        return goal;
    }

    /**
     * Set a new set of goal coordinates
     * @param goal the new goal coordinates
     */
    public void setGoal(float[] goal) {
        this.goal = goal;
    }
    
    public float[] getStart2() {
        return start2;
    }

    /**
     * Set a new set of start coordinates
     * @param start2 the new start coordinates
     */
    public void setStart2(float[] start2) {
        this.start2 = start2;
    }

    /**
     * Get access to the goal coordinates
     * @return the goal coordinates
     */
    public float[] getGoal2() {
        return goal2;
    }

    /**
     * Set a new set of goal coordinates
     * @param goal2 the new goal coordinates
     */
    public void setGoal2(float[] goal2) {
        this.goal2 = goal2;
    }

    /**
     * Get access to the goal tolerance
     * @return the goal tolerance
     */
    public float getTolerance() {
        return tolerance;
    }

    /**
     * Set a new value for the goal tolerance
     * @param tolerance the new goal tolerance
     */
    public void setTolerance(float tolerance) {
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

    public Rectangle getWall(int i) {
        return this.walls.get(i);
    }

    public void addWall(Rectangle newWall) {
        this.walls.add(newWall);
    }

    public List<Rectangle> getWalls() {
        return walls;
    }

    public void setWalls(List<Rectangle> walls) {
        this.walls = walls;
    }

    public void setOffsets(float[] offsets) {
        this.offsets = offsets;
    }

    public void setScales(float[] scales) {
        this.scales = scales;
    }

    public float[] getOffsets() {
        return offsets;
    }

    public float[] getScales() {
        return scales;
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
    private static String arrToStr(float[] array) {
        String message = "[";
        for (int i = 0; i < array.length; i++) {
            message += array[i];
            if (i == array.length - 1) message += ", ";
        }
        message += "]";
        return message;
    }
}
