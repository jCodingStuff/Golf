package com.group.golf.math;

public class JVector2 {

    private float x;
    private float y;
    private float magnitude;

    public JVector2(float x, float y) {
        this.x = x;
        this.y = y;
        this.updateMagnitude();
    }

    public JVector2(JVector2 other) {
        this.x = other.x;
        this.y = other.y;
        this.updateMagnitude();
    }

    private void updateMagnitude() {
        this.magnitude = (float)Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public float getX() {
        return x;
    }

    public float floatX() {
        return (float) this.x;
    }

    public int intX() {
        return (int) x;
    }

    public float getY() {
        return y;
    }

    public float floatY() {
        return (float) this.y;
    }

    public int intY() {
        return (int) y;
    }

    public float getMagnitude() {
        return this.magnitude;
    }

    public void multiply(float scalar) {
        x = x * scalar;
        y = y * scalar;
        magnitude = magnitude * scalar;
    }

    public void divide(float scalar) {
        x = x / scalar;
        y = y / scalar;
        magnitude = magnitude / scalar;
    }

    public void normalize() {
        x = x / magnitude;
        y = y / magnitude;
        magnitude = 1;
    }

    public void setMagnitude(float number) {
        this.normalize();
        this.multiply(number);
    }

    public void limit(float value) {
        if (this.getMagnitude() > value) {
            this.setMagnitude(value);
        }
    }

    public void swap() {
        float tmp = this.x;
        this.x = this.y;
        this.y = tmp;
    }

    public void setPosition(float x, float y) {
        this.x = x; this.y = y;
    }

    public static JVector2 opposite(JVector2 vector) {
        float invX = vector.y;
        float invY = vector.x;
        return new JVector2(invX, invY);
    }

    public void add(JVector2 vector) {
        x += vector.x;
        y += vector.y;
        magnitude = getMagnitude();
    }

    public void sub(JVector2 vector) {
        x -= vector.x;
        y -= vector.y;
        magnitude = getMagnitude();
    }

    public static JVector2 add2Vecs(JVector2 vector1, JVector2 vector2) {
        float sumX = vector1.getX() + vector2.getX();
        float sumY = vector1.getY() + vector2.getY();
        return new JVector2(sumX, sumY);
    }

    public static JVector2 sub2Vecs(JVector2 vector1, JVector2 vector2) {
        float subX = vector1.getX() - vector2.getX();
        float subY = vector1.getY() - vector2.getY();
        return new JVector2(subX, subY);
    }

    public static float dist2Vecs(JVector2 vector1, JVector2 vector2) {
        float intervalX = vector2.getX() - vector1.getX();
        float intervalY = vector2.getY() - vector1.getY();
        float distance = (float)Math.sqrt(Math.pow(intervalX, 2) + Math.pow(intervalY, 2));
        return distance;
    }

    public static float dist(float x1, float y1, float x2, float y2) {
        float intervalX = x2 - x1;
        float intervalY = y2 - y1;
        float distance = (float)Math.sqrt(Math.pow(intervalX, 2) + Math.pow(intervalY, 2));
        return distance;
    }

    public static float dotProduct(JVector2 vector1, JVector2 vector2) {
        float result = vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY();
        return result;
    }

    public static float dist2(float x1, float y1, float x2, float y2) {
        float intervalX = x2 - x1;
        float intervalY = y2 - y1;
        float distance = (float)(Math.pow(intervalX, 2) + Math.pow(intervalY, 2));
        return distance;
    }

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
