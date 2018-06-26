package com.group.golf.math;

/**
 * A class to represent a point in R3 (3D space)
 * @author Julian Marrades
 */
public class Point3D {

    private float x;
    private float y;
    private float z;

    /**
     * Create a new instance of Point3D
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     */
    public Point3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Create a new instance of Point3D leaving z=0
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Point3D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get access to the x-coordinate
     * @return the x-coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Set a new x-coordinate
     * @param x the new x-coordinate
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Get access to the y-coordinate
     * @return the y-coordinate
     */
    public float getY() {
        return y;
    }

    /**
     * Set a new y-coordinate
     * @param y the new y-coordinate
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Get access to the z-coordinate
     * @return the z-coordinate
     */
    public float getZ() {
        return z;
    }

    /**
     * Set a new z-coordinate
     * @param z the new z-coordinate
     */
    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        Point3D other = (Point3D) obj;
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[" + this.x + ", " + this.y + ", " + this.z + "]";
    }
}
