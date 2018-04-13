package com.group.golf.math;

public class BicubicInterpolator {

    private static final double[][] A = new double[][]{{1, 0, 0, 0},
                                                       {0, 0, 1, 0},
                                                       {-3, 3, -2, 1},
                                                       {2, -2, 1, 1}};
    private static final double[][] B = new double[][]{{1, 0, 0, 0},
                                                       {0, 0, 1, 0},
                                                       {-3, 3, -2, 1},
                                                       {2, -2, 1, 1}};
    private double[][] coefficients;

    public BicubicInterpolator(Point3D[][] points, double[][] dx, double[][] dy, double[][] dxy) {}

}
