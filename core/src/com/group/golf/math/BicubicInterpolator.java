package com.group.golf.math;

/**
 * A class to use bicubic polynomials for interpolation
 * @author Kaspar Kallast
 * @author Julian Marrades
 */
public class BicubicInterpolator implements Computable {

    private static final float[][] A = new float[][]{{1, 0, 0, 0},
                                                       {0, 0, 1, 0},
                                                       {-3, 3, -2, -1},
                                                       {2, -2, 1, 1}};
    private static final float[][] B = new float[][]{{1, 0, -3, 2},
                                                       {0, 0, 3, -2},
                                                       {0, 1, -2, 1},
                                                       {0, 0, -1, 1}};
    private float[][] coefficients;

    private Point3D[][] points;
    private float x0;
    private float y0;

	/**
	 * Create a new Instance of BicubicInterpolator
	 * @param points the 2D-array of points
	 * @param dx the 2D-array of dx
	 * @param dy the 2D-array of dy
	 * @param dxy the 2D-array of dxy
	 */
    public BicubicInterpolator(Point3D[][] points, float[][] dx, float[][] dy, float[][] dxy) {
    	this.points = points;
    	this.x0 = this.points[0][0].getX();
    	this.y0 = this.points[0][0].getY();

    	fitter(dx, dy, dxy);

    	this.printInfo(dx, dy, dxy);

    }

	/**
	 * Print info about the instance of BicubicInterpolator
	 */
	private void printInfo(float[][] dx, float[][] dy, float[][] dxy) {
		System.out.println("Bicubic interpolator info:");
		System.out.println("\tPoints:");
		for (int x = 0; x < points.length; x++) {
			for (int y = 0; y < points[x].length; y++) {
				System.out.println("\t\t[" + x + ", " + y + "] -> " + this.points[x][y]);
			}
		}

		this.printHelp("Coefficients", this.coefficients);
		this.printHelp("Dx", dx);
		this.printHelp("Dy", dy);
		this.printHelp("Dxy", dxy);
	}

	private void printHelp(String text, float[][] matrix) {
		System.out.println("\t" + text + ":");
		for (int i = 0; i < matrix.length; i++) {
			System.out.print("\t\t[");
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.print(matrix[i][j]);
				if (j != matrix[i].length - 1) System.out.print(" ");
			}
			System.out.println("]");
		}
	}

	/**
	 * Get access to the 2D-array of points representing the corners of the square
	 * @return the corners of the square
	 */
	public Point3D[][] getPoints() {
    	return points;
    }


	/**
	 * Compute the coefficients for the bicubic polynomial
	 * @param dx the 2D-array of dx
	 * @param dy the 2D-array of dy
	 * @param dxy the 2D-array of dxy
	 */
    private void fitter(float[][] dx, float[][] dy, float[][] dxy) {
    	float[][] values = new float[4][4];

    	// First row
    	values[0][0] = this.points[0][0].getZ();
		values[0][1] = this.points[0][1].getZ();
		values[0][2] = dy[0][0];
		values[0][3] = dy[0][1];

		// Second row
		values[1][0] = this.points[1][0].getZ();
		values[1][1] = this.points[1][1].getZ();
		values[1][2] = dy[1][0];
		values[1][3] = dy[1][1];

		// Third row
		values[2][0] = dx[0][0];
		values[2][1] = dx[0][1];
		values[2][2] = dxy[0][0];
		values[2][3] = dxy[0][1];

		// Forth row
		values[3][0] = dx[1][0];
		values[3][1] = dx[1][1];
		values[3][2] = dxy[1][0];
		values[3][3] = dxy[1][1];

    	float[][] res1 = MathLib.multiply(A, values);
    	this.coefficients = MathLib.multiply(res1, B);
    }

	/**
	 * Make use of the coefficients to compute f(x, y)
	 * @param x the x-component of the coordinate
	 * @param y the y-component of the coordinate
	 * @return z = f(x, y)
	 */
	@Override
	public float getZ(float x, float y) {
		float result = 0;

		x -= this.x0;
		y -= this.y0;

		for (int i = 0; i < this.coefficients.length; i++) {
			for (int j = 0; j < this.coefficients[i].length; j++) {
				result += this.coefficients[i][j] * Math.pow(x, i) * Math.pow(y, j);
			}
		}

		return result;
	}
}
