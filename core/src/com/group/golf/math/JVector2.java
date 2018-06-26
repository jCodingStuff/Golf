package com.group.golf.math;

/**
 * A class to represent a vector in R2 (2D space)
 * @author Julian Marrades
 */
public class JVector2 {

    private float x;
    private float y;
    private float magnitude;

    /**
     * Create a new instance of JVector2
     * @param x the x-component
     * @param y the y-component
     */
    public JVector2(float x, float y) {
        this.x = x;
        this.y = y;
        this.updateMagnitude();
    }

    /**
     * Create a new instace of JVector2 from another instance
     * @param other the JVector2 template
     */
    public JVector2(JVector2 other) {
        this.x = other.x;
        this.y = other.y;
        this.updateMagnitude();
    }

    /**
     * Update the magnitude using the components
     */
    private void updateMagnitude() {
        this.magnitude = (float)Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    /**
     * Get access to the x-component
     * @return the x-component
     */
    public float getX() {
        return x;
    }

    /**
     * Get access to the x-component truncated as an integer
     * @return the integer value of the x-component
     */
    public int intX() {
        return (int) x;
    }

    /**
     * Get access to the y-component
     * @return the y-component
     */
    public float getY() {
        return y;
    }

    /**
     * Get access to the y-component truncated as an integer
     * @return the integer value of the y-component
     */
    public int intY() {
        return (int) y;
    }

    /**
     * Get the magnitute of the vector
     * @return the magnitude of the vector
     */
    public float getMagnitude() {
        return this.magnitude;
    }

    /**
     * Scale (mulitiply) the vector
     * @param scalar the scalar value to apply
     */
    public void multiply(float scalar) {
        x = x * scalar;
        y = y * scalar;
        magnitude = magnitude * scalar;
    }

    /**
     * Scale (divide) the vector
     * @param scalar the scalar value to apply
     */
    public void divide(float scalar) {
        x = x / scalar;
        y = y / scalar;
        magnitude = magnitude / scalar;
    }

    /**
     * Normalize the vector, set its magnitude to 1
     */
    public void normalize() {
        x = x / magnitude;
        y = y / magnitude;
        magnitude = 1;
    }

    /**
     * Set a new magnitude for the vector
     * @param number the new magnitude
     */
    public void setMagnitude(float number) {
        this.normalize();
        this.multiply(number);
    }

    /**
     * Limit the magnitude of the vector
     * @param value upper limit for the magnitude
     */
    public void limit(float value) {
        if (this.getMagnitude() > value) {
            this.setMagnitude(value);
        }
    }

    /**
     * Swap the components of the vector
     */
    public void swap() {
        float tmp = this.x;
        this.x = this.y;
        this.y = tmp;
    }

    /**
     * Set a new position for the vector
     * @param x the new x-coordinate
     * @param y the new y-coordinate
     */
    public void setPosition(float x, float y) {
        this.x = x; this.y = y;
    }

    /**
     * Invert a vector
     * @param vector the vector to invert
     * @return the a new instance with the swapped components of vector
     */
    public static JVector2 opposite(JVector2 vector) {
        float invX = vector.y;
        float invY = vector.x;
        return new JVector2(invX, invY);
    }

    /**
     * Perform vector sum
     * @param vector the vector to add to the current instance
     */
    public void add(JVector2 vector) {
        x += vector.x;
        y += vector.y;
        this.updateMagnitude();
    }

    /**
     * Perform vector subtraction
     * @param vector the vector to subtract to the current instance
     */
    public void sub(JVector2 vector) {
        x -= vector.x;
        y -= vector.y;
        this.updateMagnitude();
    }

    /**
     * Static vector sum
     * @param vector1 the first vector
     * @param vector2 the second vector
     * @return a vector which is the sum of vector1 and vector2
     */
    public static JVector2 add2Vecs(JVector2 vector1, JVector2 vector2) {
        float sumX = vector1.getX() + vector2.getX();
        float sumY = vector1.getY() + vector2.getY();
        return new JVector2(sumX, sumY);
    }

    /**
     * Static vector subtraction
     * @param vector1 the first vector
     * @param vector2 the second vector
     * @return a vector which is the subtraction of vector2 to vector1
     */
    public static JVector2 sub2Vecs(JVector2 vector1, JVector2 vector2) {
        float subX = vector1.getX() - vector2.getX();
        float subY = vector1.getY() - vector2.getY();
        return new JVector2(subX, subY);
    }

    /**
     * Euclidean distance between the tips of two vectors
     * @param vector1 the first vector
     * @param vector2 the second vector
     * @return euclidean distance between the tips of vector1 and vector2
     */
    public static float dist2Vecs(JVector2 vector1, JVector2 vector2) {
        float intervalX = vector2.getX() - vector1.getX();
        float intervalY = vector2.getY() - vector1.getY();
        float distance = (float)Math.sqrt(Math.pow(intervalX, 2) + Math.pow(intervalY, 2));
        return distance;
    }

    /**
     * Euclidean distance between two points in R2
     * @param x1 first x-coordinate
     * @param y1 first y-coordinate
     * @param x2 second x-coordinate
     * @param y2 second y-coordinate
     * @return euclidean distance between (x1, y1) and (x2, y2)
     */
    public static float dist(float x1, float y1, float x2, float y2) {
        float intervalX = x2 - x1;
        float intervalY = y2 - y1;
        float distance = (float)Math.sqrt(Math.pow(intervalX, 2) + Math.pow(intervalY, 2));
        return distance;
    }

    /**
     * Dot product of two vectors
     * @param vector1 the first vector
     * @param vector2 the second vector
     * @return the dot product of vector1 and vector 2
     */
    public static float dotProduct(JVector2 vector1, JVector2 vector2) {
        float result = vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY();
        return result;
    }

    /**
     * Euclidean squared distance between two points in R2
     * @param x1 first x-coordinate
     * @param y1 first y-coordinate
     * @param x2 second x-coordinate
     * @param y2 second y-coordinate
     * @return euclidean squared distance between (x1, y1) and (x2, y2)
     */
    public static float dist2(float x1, float y1, float x2, float y2) {
        float intervalX = x2 - x1;
        float intervalY = y2 - y1;
        float distance = (float)(Math.pow(intervalX, 2) + Math.pow(intervalY, 2));
        return distance;
    }

    /**
     * Compute the angle (in radians) between two vectors
     * @param vector1 the first vector
     * @param vector2 the second vector
     * @return the angle between vector1 and vector2
     */
    public static float angleBetween(JVector2 vector1, JVector2 vector2) { //Gives the angle in radians
        float topPart = dotProduct(vector1, vector2);
        float botPart = vector1.getMagnitude() * vector2.getMagnitude();
        float cosine = topPart / botPart;
        return (float)Math.acos(cosine);
    }

    @Override
    public String toString() {
        return "JVector2{" +
                "x=" + x +
                ", y=" + y +
                ", magnitude=" + magnitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JVector2 other = (JVector2) o;
        return other.x == this.x &&
                other.y == this.y;
    }

}
