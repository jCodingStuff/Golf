package com.group.golf.math;

/**
 * A class to hold static methods for interpolation
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
    public static Computable[][] getInterpolators(Point3D[][] points, double[][] dx, double[][] dy, double[][] dxy) {
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
        double[][] dx = computeDx(points);
        double[][] dy = computeDy(points);
        double[][] dxy = computeDxy(points);
        fillComputable(points, interpolators, dx, dy, dxy);
        return interpolators;
    }

    private static void fillComputable(Point3D[][] points, Computable[][] interpolators, double[][] dx, double[][] dy,
                                       double[][] dxy) {
        for (int x = 0; x < points.length - 1; x++) {
            for (int y = 0; y < points[x].length - 1; y++) {
                Point3D[][] localPoints = new Point3D[][]{{points[x][y], points[x][y+1]},
                        {points[x+1][y], points[x+1][y+1]}};
                double[][] localDx = new double[][]{{dx[x][y], dx[x][y+1]},
                        {dx[x+1][y], dx[x+1][y+1]}};
                double[][] localDy = new double[][]{{dy[x][y], dy[x][y+1]},
                        {dy[x+1][y], dy[x+1][y+1]}};
                double[][] localDxy = new double[][]{{dxy[x][y], dxy[x][y+1]},
                        {dxy[x+1][y], dxy[x+1][y+1]}};
                interpolators[x][y] = new BicubicInterpolator(localPoints, localDx, localDy, localDxy);
            }
        }
    }

    private static double[][] computeDx(Point3D[][] points) {
        double[][] dx = new double[points.length][points[0].length];
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

    private static double[][] computeDy(Point3D[][] points) {
        double[][] dy = new double[points.length][points[0].length];
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

    private static double[][] computeDxy(Point3D[][] points) {
        double[][] dxy = new double[points.length][points[0].length];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {

            }
        }
        return dxy;
    }

}
