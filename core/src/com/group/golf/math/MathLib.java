package com.group.golf.math;

import com.badlogic.gdx.math.Rectangle;
import com.group.golf.Golf;

/**
 * A class to hold static methods for Math purposes
 * @author Julian Marrades
 * @author Kaspar Kallast
 * @author Alexandros Chimonas
 */
public class MathLib {

    /**
     * Compute the average of 2 numbers
     * @param a one number
     * @param b another number
     * @return the average of a and b
     */
    public static float average(float a, float b) {
        return (a+b)/2;
    }

    /**
     * Map a value ranging from [min1, max2] to another value in the interval [min2, max2]
     * @param value tha value to map
     * @param min1 the left extreme of the initial interval (inclusive)
     * @param max1 the right extreme of the initial interval (inclusive)
     * @param min2 the left extreme of the final interval (inclusive)
     * @param max2 the right extreme of the final interval (inclusive)
     * @return the result value which belongs to [min2, max2]
     */
    public static float map(float value, float min1, float max1, float min2, float max2) {
        float result = 0;
        if (value <= min1) result = min2;
        else if (value >= max1) result = max2;
        else {
            result = min2 + ((value - min1) / (max1 - min1)) * (max2 - min2);

        }
        return result;
    }

    /**
     * Create a copy of a float array
     * @param arr the array to copy
     * @return the copy of the array
     */
    public static float[] copyfloatArr(float[] arr) {
        float[] result = new float[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    /**
     * Create a copy of a float array
     * @param arr the array to copy
     * @return the copy of the array
     */
    public static float[] copyFloatArr(float[] arr) {
        float[] result = new float[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    /**
     * Translate function coordinate to pixel coordinate
     * @param from function coordinates
     * @param offsets the offset values
     * @param scales the scales
     * @return the pixel value
     */
    public static float[] toPixel(float[] from, float[] offsets, float[] scales) {
        float realX = (from[0] - offsets[0]) * (1 / scales[0]);
        float realY = (from[1] - offsets[1]) * (1 / scales[1]);
        return new float[]{realX, realY};
    }

    /**
     * Multiply two matrices
     * @param a the first matrix
     * @param b the second matrix
     * @return the matrix resulting of the multiplication of a and b
     */
 	public static float[][] multiply(float[][] a, float[][] b) {
 		float[][] result = new float[a.length][b[0].length];
 		for (int i = 0; i < result.length; i++) {
 		    for (int j = 0; j < result[i].length; j++) {
 		        result[i][j] = rowByColumn(a, b, i, j);
            }
        }
 		return result;
 	}

    /**
     * Multiply the row of a matrix by the column of another matrix
     * @param matrix1 the first matrix
     * @param matrix2 the second matrix
     * @param row the row of the first matrix
     * @param column the column of the second matrix
     * @return the dot-product of row and column
     */
 	public static float rowByColumn(float[][] matrix1, float[][] matrix2, int row, int column) {
 	    float result = 0;
 	    for (int j = 0; j < matrix1[row].length; j++) {
 	        result += matrix1[row][j] * matrix2[j][column];
        }
        return result;
    }

    /**
     * Check if two float arrays match
     * @param arr1 one array
     * @param arr2 another array
     * @return true if they match, false otherwise
     */
    public static boolean arrayMatch(float[] arr1, float[] arr2) {
        if (arr1 == null && arr2 != null) return false;
        else if (arr1 != null && arr2 == null) return false;
        else if (arr1.length != arr2.length) return false;
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) return false;
        }
        return true;
    }

    /**
     * Get euclidean 2D-distance (squared) between two points
     * @param a one point
     * @param b another distance
     * @return squared distance between a and b
     */
    public static float distanceSquared(Point3D a, Point3D b) {
        float x = b.getX() - a.getX();
        float y = b.getY() - a.getY();
        return (float)(Math.pow(x, 2) + Math.pow(y, 2));
    }

    /**
     * Get a random float value within a range
     * @param low lower bound of the range
     * @param high upper bound of the range
     * @return a random float x between low (including) and high (excluding)
     */
    public static float randomFloat(float low, float high) {
        return (float)(Math.random()*(high - low) + low);
    }

    /**
     * Generate a rectangle from user mouse drag
     * @param firstX x-coordinate of mouse press
     * @param firstY y-coordinate of mouse press
     * @param lastX x-coordinate of mouse release
     * @param lastY y-coordinate of mouse release
     * @return the rectangle formed by (firstX, firstY) and (lastX, lastY) as opposite corners
     */
    public static Rectangle createRectangle(int firstX, int firstY, int lastX, int lastY) {
        float width = Math.abs(firstX - lastX);
        float height = Math.abs(firstY - lastY);
        float left = Math.min(firstX, lastX);
        float bot = Math.min(firstY, lastY);
        return new Rectangle(left, bot, width, height);
    }

}
