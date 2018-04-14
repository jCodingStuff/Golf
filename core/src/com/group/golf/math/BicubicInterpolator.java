package com.group.golf.math;

public class BicubicInterpolator implements Computable {

    private static final double[][] A = new double[][]{{1, 0, 0, 0},
                                                       {0, 0, 1, 0},
                                                       {-3, 3, -2, 1},
                                                       {2, -2, 1, 1}};
    private static final double[][] B = new double[][]{{1, 0, 0, 0},
                                                       {0, 0, 1, 0},
                                                       {-3, 3, -2, 1},
                                                       {2, -2, 1, 1}};
    private double[][] coefficients;

    private Point3D[][] points;
    private double x0;
    private double y0;

    public BicubicInterpolator(Point3D[][] points, double[][] dx, double[][] dy, double[][] dxy) {
    	this.points = points;
    	this.x0 = this.points[0][0].getX();
    	this.y0 = this.points[0][0].getY();
    	fitter(dx, dy, dxy);
    }

    public Point3D[][] getPoints() {
    	return points;
    }

    private void fitter(double[][] dx, double[][] dy, double[][] dxy) {
    	double[][] values = new double[4][4];

    	values[0][0] = points[0][0].getZ();
    	values[0][1] = points[0][1].getZ();
    	values[0][2] = dy[0][0];
    	values[0][3] = dy[0][1];
    	values[1][0] = points[1][0].getZ();
    	values[2][0] = dx[0][0];
    	values[3][0] = dx[1][0];
    	values[1][1] = points[0][1].getZ();
    	values[2][1] = dx[0][1];
    	values[3][1] = dy[1][1];
    	values[1][2] = dx[1][0];
    	values[2][2] = dxy[0][0];
    	values[3][2] = dxy[1][0];
    	values[1][3] = dy[1][1];
    	values[2][3] = dxy[0][1];
    	values[3][3] = dy[1][1];

    	double[][] res1 = MathLib.multiply(values, A);
    	this.coefficients = MathLib.multiply(res1, B);
    }

	@Override
	public double getZ(double x, double y) {
		double result = 0;

		for (int i = 0; i < this.coefficients.length; i++) {
			for (int j = 0; j < this.coefficients[i].length; j++) {
				result += this.coefficients[i][j] * Math.pow((x - this.x0), i) * Math.pow((y - this.y0), j);
			}
		}

		return result;
	}
}
