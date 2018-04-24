package com.group.golf.math;

/**
 * A class to use bicubic polynomials for interpolation
 */
public class BicubicInterpolator implements Computable {

    private static final double[][] A = new double[][]{{1, 0, 0, 0},
                                                       {0, 0, 1, 0},
                                                       {-3, 3, -2, -1},
                                                       {2, -2, 1, 1}};
    private static final double[][] B = new double[][]{{1, 0, -3, 2},
                                                       {0, 0, 3, -2},
                                                       {0, 1, -2, 1},
                                                       {0, 0, -1, 1}};
    private double[][] coefficients;

    private Point3D[][] points;
    private Point3D[][] offsetPoints;
    private double x0;
    private double y0;

	/**
	 * Create a new Instance of BicubicInterpolator
	 * @param points the 2D-array of points
	 * @param dx the 2D-array of dx
	 * @param dy the 2D-array of dy
	 * @param dxy the 2D-array of dxy
	 */
    public BicubicInterpolator(Point3D[][] points, double[][] dx, double[][] dy, double[][] dxy) {
    	this.points = points;
    	this.x0 = this.points[0][0].getX();
    	this.y0 = this.points[0][0].getY();

    	this.calcOffsetPoints();

    	fitter(dx, dy, dxy);
    }

	/**
	 * Get access to the 2D-array of points representing the corners of the square
	 * @return the corners of the square
	 */
	public Point3D[][] getPoints() {
    	return points;
    }

	/**
	 * Move the points to square with bot-left corner at (0, 0)
	 */
	private void calcOffsetPoints() {
    	this.offsetPoints = new Point3D[this.points.length][this.points[0].length];
    	for (int i = 0; i < this.points.length; i++) {
    		for (int j = 0; j < this.points[i].length; j++) {
    			this.offsetPoints[i][j] = new Point3D(i, j, this.points[i][j].getZ());
			}
		}
	}

	/**
	 * Compute the coefficients for the bicubic polynomial
	 * @param dx the 2D-array of dx
	 * @param dy the 2D-array of dy
	 * @param dxy the 2D-array of dxy
	 */
    private void fitter(double[][] dx, double[][] dy, double[][] dxy) {
    	double[][] values = new double[4][4];

    	values[0][0] = offsetPoints[0][0].getZ();
    	values[0][1] = offsetPoints[0][1].getZ();
    	values[0][2] = dy[0][0];
    	values[0][3] = dy[0][1];
    	values[1][0] = offsetPoints[1][0].getZ();
    	values[2][0] = dx[0][0];
    	values[3][0] = dx[1][0];
    	values[1][1] = offsetPoints[0][1].getZ();
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

		x -= this.x0;
		y -= this.y0;

		for (int i = 0; i < this.coefficients.length; i++) {
			for (int j = 0; j < this.coefficients[i].length; j++) {
				result += this.coefficients[i][j] * Math.pow((x - this.x0), i) * Math.pow((y - this.y0), j);
			}
		}

		return result;
	}
}
