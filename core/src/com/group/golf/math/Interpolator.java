package com.group.golf.math;

/**
 * A class to hold static methods for interpolation
 * @author Kaspar Kallast
 * @author Julian Marrades
 */
public class Interpolator {

    /**
     * Get the 2D-array of BicubicInterpolators out of point information
     * @param points the 2D-array of points
     * @param dx the 2D-array of dx
     * @param dy the 2D-array of dy
     * @param dxy the 2D-array of dxy
     * @return the 2D-array of BicubicInterpolators
     */
    public static Computable[][] getInterpolators(Point3D[][] points, float[][] dx, float[][] dy, float[][] dxy) {
        Computable[][] interpolators = new Computable[points.length - 1][points[0].length - 1];
        fillComputable(points, interpolators, dx, dy, dxy);
        return interpolators;
    }

    /**
     * Overloaded getInterpolators
     * @param points the 2D-array of points
     * @return the 2D-array of BicubicInterpolators
     */
    public static Computable[][] getInterpolators(Point3D[][] points) {
        Computable[][] interpolators = new Computable[points.length - 1][points[0].length - 1];
        float[][] dx = computeDx(points);
        float[][] dy = computeDy(points);
        float[][] dxy = computeDxy(points, dy);
        fillComputable(points, interpolators, dx, dy, dxy);
        for (int i = 0; i < interpolators.length; i++) {

        }
        return interpolators;
    }

    /**
     * Fill the computable 2D-array of interpolators
     * @param points the point coordinates
     * @param interpolators the array to fill
     * @param dx the derivatives in terms of x
     * @param dy the derivatives in terms of y
     * @param dxy the mixed derivatives in terms of x and y
     */
    private static void fillComputable(Point3D[][] points, Computable[][] interpolators, float[][] dx, float[][] dy,
                                       float[][] dxy) {
        for (int y = 0; y < interpolators[0].length; y++) {
            for (int x = 0; x < interpolators.length; x++) {
                Point3D[][] localPoints = new Point3D[][]{{points[x][y], points[x][y + 1]},
                        {points[x + 1][y], points[x + 1][y + 1]}};
                float[][] localDx = new float[][]{{dx[x][y], dx[x][y + 1]},
                        {dx[x + 1][y], dx[x + 1][y + 1]}};
                float[][] localDy = new float[][]{{dy[x][y], dy[x][y + 1]},
                        {dy[x + 1][y], dy[x + 1][y + 1]}};
                float[][] localDxy = new float[][]{{dxy[x][y], dxy[x][y + 1]},
                        {dxy[x + 1][y], dxy[x + 1][y + 1]}};
                interpolators[x][y] = new BicubicInterpolator(localPoints, localDx, localDy, localDxy);
            }
        }
    }

    /**
     * Compute the partial derivatives in terms of x
     * @param points the points coordinates
     * @return the 2D-array containing the partial derivatives
     */
    private static float[][] computeDx(Point3D[][] points) {
        float[][] dx = new float[points.length][points[0].length];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (i == 0) {
                    dx[i][j] = (points[i+1][j].getZ() - points[i][j].getZ())/(points[i+1][j].getX() - points[i][j].getX());
                } else if (i == points.length - 1) {
                    dx[i][j] = (points[i][j].getZ() - points[i-1][j].getZ())/(points[i][j].getX() - points[i-1][j].getX());
                } else {
                    dx[i][j] = (points[i+1][j].getZ() - points[i-1][j].getZ())/(points[i+1][j].getX() - points[i-1][j].getX());
                }
            }
        }
        return dx;
    }

    /**
     * Compute the partial derivatives in terms of y
     * @param points the points coordinates
     * @return the 2D-array containing the partial derivatives
     */
    private static float[][] computeDy(Point3D[][] points) {
        float[][] dy = new float[points.length][points[0].length];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (j == 0) {
                    dy[i][j] = (points[i][j+1].getZ() - points[i][j].getZ())/(points[i][j+1].getY() - points[i][j].getY());
                } else if (j == points[i].length - 1) {
                    dy[i][j] = (points[i][j].getZ() - points[i][j-1].getZ())/(points[i][j].getY() - points[i][j-1].getY());
                } else {
                    dy[i][j] = (points[i][j+1].getZ() - points[i][j-1].getZ())/(points[i][j+1].getY() - points[i][j-1].getY());
                }
            }
        }
        return dy;
    }

    /**
     * Compute the mixed derivatives in terms of x and y
     * @param points the points coordinates
     * @param dy the partial derivates in terms of y
     * @return the mixed derivatives
     */
    private static float[][] computeDxy(Point3D[][] points, float[][] dy) {
        Point3D[][] derivatives = new Point3D[points.length][points[0].length];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                derivatives[i][j] = new Point3D(points[i][j].getX(), points[i][j].getY(), dy[i][j]);
            }
        }
        return computeDx(derivatives);
    }

}
