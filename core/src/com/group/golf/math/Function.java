package com.group.golf.math;

/**
 * A class to represent a function f(x, y) = z
 *
 * @author Julian Marrades
 * @version 0.1, 17/03/2018
 */

public class Function implements Computable {

    private String formula;
    private GMath calc;

    /**
     * Create a new Function instance
     * @param formula the formula of the function
     */
    public Function(String formula) {
        this.formula = formula;
        this.calc = new GMath(formula, false);
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
    @Override
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

    @Override
    public Function clone() {
        return new Function(this.formula);
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
