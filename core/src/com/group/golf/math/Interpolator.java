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
        return interpolators;
    }

}
