package com.group.golf.math;

/**
 * A class to represent a function f(x, y) = z
 *
 * @author Julian Marrades
 * @version 0.1, 17/03/2018
 */

public class Function {

    private String formula;
    private GMath calc;

    private final double[] xRange;
    private final double[] yRange;

    /**
     * Create a new Function instance
     * @param formula the formula of the function
     */
    public Function(String formula) {
        this.formula = formula;
        this.calc = new GMath(formula, false);
        this.xRange = null;
        this.yRange = null;
    }

    /**
     * Create a new Function spline instance
     * @param formula the formula of the function
     * @param xRange the domain of x-coordinate
     * @param yRange the domain of y-coordinate
     */
    public Function(String formula, double[] xRange, double[] yRange) {
        this.formula = formula;
        this.calc = new GMath(formula, false);
        this.xRange = xRange;
        this.yRange = yRange;
    }

    /**
     * Check if a given coordinate is in the domain of the current function
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the coordinate is inside the domain, false otherwise
     */
    public boolean isInDomain(double x, double y) {
        return x >= this.xRange[0] && x <= this.xRange[1] && y >= this.yRange[0] && y <= this.yRange[1];
    }

    /**
     * Check if the function is a spline
     * @return true if it is spline, false otherwise
     */
    public boolean isSpline() {
        return this.xRange != null || this.yRange != null;
    }

    /**
     * Get access to the computation engine of the function
     * @return the GMath instance that function objects hold
     */
    public GMath getGMath() {
        return this.calc;
    }

    /**
     * Compute the outcome of the function given values for x and y
     * @param x the value for x
     * @param y the value for y
     * @return the value of z = f(x, y)
     */
    public double getZ(double x, double y) {
        return this.calc.compute(x, y);
    }

    /**
     * Get access to the formula of the Function
     * @return the formula of the function
     */
    public String getName() {
        return this.formula;
    }

    /**
     * Set a new formula to the function
     * @param formula the new formula
     */
    public void setName(String formula) {
        this.formula = formula;
        this.calc.setFormula(formula);
    }

    /**
     * Get access to the x-domain of the function
     * @return the x-domain
     */
    public double[] getxRange() {
        return xRange;
    }

    /**
     * Get access to the y-domain of the function
     * @return the y-domain
     */
    public double[] getyRange() {
        return yRange;
    }

    @Override
    public Function clone() {
        return new Function(this.formula, MathLib.copyDoubleArr(this.xRange), MathLib.copyDoubleArr(this.yRange));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Function other = (Function) obj;
        return this.formula.equals(other.formula);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " [ z = f(x, y) = " + this.formula + " ]";
    }

}
