package com.group.golf.math;

public class JVector2 {

    private double x;
    private double y;
    private double magnitude;

    public JVector2(double x, double y) {
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
        this.magnitude = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public double getX() {
        return x;
    }

    public float floatX() {
        return (float) this.x;
    }

    public int intX() {
        return (int) x;
    }

    public double getY() {
        return y;
    }

    public float floatY() {
        return (float) this.y;
    }

    public int intY() {
        return (int) y;
    }

    public double getMagnitude() {
        return this.magnitude;
    }

    public void multiply(double scalar) {
        x = x * scalar;
        y = y * scalar;
        magnitude = magnitude * scalar;
    }

    public void divide(double scalar) {
        x = x / scalar;
        y = y / scalar;
        magnitude = magnitude / scalar;
    }

    public void normalize() {
        x = x / magnitude;
        y = y / magnitude;
        magnitude = 1;
    }

    public void setMagnitude(double number) {
        this.normalize();
        this.multiply(number);
    }

    public void limit(double value) {
        if (this.getMagnitude() > value) {
            this.setMagnitude(value);
        }
    }

    public static JVector2 opposite(JVector2 vector) {
        double invX = vector.y;
        double invY = vector.x;
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
        double sumX = vector1.getX() + vector2.getX();
        double sumY = vector1.getY() + vector2.getY();
        return new JVector2(sumX, sumY);
    }

    public static JVector2 sub2Vecs(JVector2 vector1, JVector2 vector2) {
        double subX = vector1.getX() - vector2.getX();
        double subY = vector1.getY() - vector2.getY();
        return new JVector2(subX, subY);
    }

    public static double dist2Vecs(JVector2 vector1, JVector2 vector2) {
        double intervalX = vector2.getX() - vector1.getX();
        double intervalY = vector2.getY() - vector1.getY();
        double distance = Math.sqrt(Math.pow(intervalX, 2) + Math.pow(intervalY, 2));
        return distance;
    }

    public static double dist(double x1, double y1, double x2, double y2) {
        double intervalX = x2 - x1;
        double intervalY = y2 - y1;
        double distance = Math.sqrt(Math.pow(intervalX, 2) + Math.pow(intervalY, 2));
        return distance;
    }

    public static double dotProduct(JVector2 vector1, JVector2 vector2) {
        double result = vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY();
        return result;
    }

    public static double angleBetween(JVector2 vector1, JVector2 vector2) { //Gives the angle in radians
        double topPart = dotProduct(vector1, vector2);
        double botPart = vector1.getMagnitude() * vector2.getMagnitude();
        double cosine = topPart / botPart;
        return Math.acos(cosine);
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
