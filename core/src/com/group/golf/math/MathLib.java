package com.group.golf.math;

import com.group.golf.Golf;

/**
 * A class to hold static methods for Math purposes
 */
public class MathLib {

    /**
     * Map a value ranging from [min1, max2] to another value in the interval [min2, max2]
     * @param value tha value to map
     * @param min1 the left extreme of the initial interval (inclusive)
     * @param max1 the right extreme of the initial interval (inclusive)
     * @param min2 the left extreme of the final interval (inclusive)
     * @param max2 the right extreme of the final interval (inclusive)
     * @return the result value which belongs to [min2, max2]
     */
    public static double map(double value, double min1, double max1, double min2, double max2) {
        double result = 0;
        if (value <= min1) result = min2;
        else if (value >= max1) result = max2;
        else {
            result = min2 + ((value - min1) / (max1 - min1)) * (max2 - min2);

        }
        return result;
    }

    /**
     * Create a copy of a double array
     * @param arr the array to copy
     * @return the copy of the array
     */
    public static double[] copyDoubleArr(double[] arr) {
        double[] result = new double[arr.length];
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
    public static double[] toPixel(double[] from, double[] offsets, double[] scales) {
        double realX = (from[0] - offsets[0]) * (1 / scales[0]);
        double realY = (from[1] - offsets[1]) * (1 / scales[1]);
        return new double[]{realX, realY};
    }

    /**
     * Multiply two matrices
     * @param a the first matrix
     * @param b the second matrix
     * @return the matrix resulting of the multiplication of a and b
     */
 	public static double[][] multiply(double[][] a, double[][] b) {
 		// Create new matrix to store added values
        double[][] C = new double[b.length][a[0].length];
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                for (int k = 0; k < b[0].length; k++) {
                    C[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return C;
 	}

    /**
     * Check if two double arrays match
     * @param arr1 one array
     * @param arr2 another array
     * @return true if they match, false otherwise
     */
    public static boolean arrayMatch(double[] arr1, double[] arr2) {
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
    public static double distanceSquared(Point3D a, Point3D b) {
        double x = b.getX() - a.getX();
        double y = b.getY() - a.getY();
        return Math.pow(x, 2) + Math.pow(y, 2);
    }

    /**
     * Get a random double value within a range
     * @param low lower bound of the range
     * @param high upper bound of the range
     * @return a random double x such that low <= x < high
     */
    public static double randomDouble(double low, double high) {
        return Math.random()*(high - low) + low;
    }

}
