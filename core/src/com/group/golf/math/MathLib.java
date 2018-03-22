package com.group.golf.math;

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
        else result = value * ((max2 - min2) / (max1 - min1));
        return result;
    }

    /**
     * Create a copy of a double array
     * @param arr the array to copy
     * @return the copy of the array
     */
    public static double[] copyArr(double[] arr) {
        double[] result = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    /**
     * Translate function coordinate to pixel coordinate
     * @param from function coordinate
     * @param offset the offset value
     * @param scale the scale
     * @return the pixel value
     */
    public static double toPixel(double from, double offset, double scale) {
        return (from - offset) * (1/scale);
    }

}
