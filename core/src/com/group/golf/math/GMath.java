package com.group.golf.math;

import java.util.StringTokenizer;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

/**
 * A class with methods to implement Shunting-Yard Algorithm and
 * RPN computations
 *
 * @author Julian Marrades
 * @version 0.1, 17/03/2018
 *
 * @author Julian Marrades
 * @version 0.2, 18/03/2018
 */
public class GMath {

    private static Map<String, Integer> precedence;
    private static Map<String, Integer> associativity; // 0: left;  1: right

    private String formula;

    /**
     * Generate a new GMath instance
     * @param formula the formula to work with
     * @param rpn if the formula is already expressed in RPN notation
     */
    public GMath(String formula, boolean rpn) {
        if (precedence == null && associativity == null) {
            this.fillMaps();
        }
        this.formula = formula;
        if (!rpn) this.toRPN();
    }

    /**
     * Give the formula a new value
     * @param formula the new value for the formula
     */
    public void setFormula(String formula) {
        this.formula = formula;
        this.toRPN();
    }

    /**
     * Fill precedence and associativity maps
     */
    private void fillMaps() {
        // Fill precedence
        precedence = new HashMap<String, Integer>();
        precedence.put("-", 1);
        precedence.put("+", 1);
        precedence.put("/", 2);
        precedence.put("*", 2);
        precedence.put("^", 3);
        precedence.put("cos", 4);
        precedence.put("sin", 4);
        precedence.put("(", 5);
        precedence.put(")", 5);

        // Fill associativity
        associativity = new HashMap<String, Integer>();
        associativity.put("-", 0); // Left
        associativity.put("+", 0); // Left
        associativity.put("/", 0); // Left
        associativity.put("*", 0); // Left
        associativity.put("^", 1); // Right
        associativity.put("cos", 1); // Right
        associativity.put("sin", 1); // Right
    }

    /**
     * Transform the formula string to RPN notation using Shunting-Yard Algorith
     */
    private void toRPN() {
        Stack<String> operators = new Stack<String>();
        String output = "";
        StringTokenizer tokenizer = new StringTokenizer(this.formula);
        while (tokenizer.hasMoreTokens()) {
            String next = tokenizer.nextToken();
            if (isNumeric(next)) { // If number
                output += next + " ";
            }
            // If constant pi or e
            else if (next.equalsIgnoreCase("pi") || next.equalsIgnoreCase("e")) {
                output += next + " ";
            }
            // If variable x or y
            else if (next.equalsIgnoreCase("x") || next.equalsIgnoreCase("y")) {
                output += next + " ";
            }
            else if (next.equals("(")) { // if Left Bracket
                operators.push(next);
            }
            else if (next.equals(")")) { // if right Bracket
                while (!operators.peek().equals("(")) {
                    output += operators.pop() + " ";
                }
                String leftBracket = operators.pop();
            }
            else { // If operator
                while ((topWithPrecedence(next, operators) || topEqualLeft(next, operators)) && !operators.peek().equals("(")) {
                    output += operators.pop() + " ";
                }
                operators.push(next);
            }
        }
        while (!operators.isEmpty()) output += operators.pop() + " ";
        // Starting index is inclusive, ending index is exclusive
        this.formula = output.substring(0, output.length() - 1);
    }

    private static boolean topEqualLeft(String op1, Stack<String> operators) {
        if (operators.isEmpty()) return false;
        else {
            int nextPrecedence = precedence.get(op1);
            int topPrecedence = precedence.get(operators.peek());
            int topAssociative = associativity.get(operators.peek());
            return topPrecedence == nextPrecedence && topAssociative == 0;
        }
    }

    private static boolean topWithPrecedence(String op1, Stack<String> operators) {
        if (operators.isEmpty()) return false;
        else {
            int nextPrecedence = precedence.get(op1);
            int topPrecedence = precedence.get(operators.peek());
            return topPrecedence > nextPrecedence;
        }
    }

    private static boolean isNumeric(String token) {
        boolean numeric = true;
        try {
            double num = Double.parseDouble(token);
        }
        catch (NumberFormatException e) {
            numeric = false;
        }
        return numeric;
    }

    /**
     * Compute the result of the formula swapping x and y for given values
     * @param x the value for x
     * @param y the value for y
     * @return the output of the function
     */
    public float compute(double x, double y) {
        Stack<Double> stack = new Stack<Double>();
        StringTokenizer tokenizer = new StringTokenizer(this.formula);
        while (tokenizer.hasMoreTokens()) {
            String next = tokenizer.nextToken();
            if (isNumeric(next)) stack.push(Double.parseDouble(next));
            else if (next.equalsIgnoreCase("pi")) stack.push(Math.PI);
            else if (next.equalsIgnoreCase("e")) stack.push(Math.E);
            else if (next.equalsIgnoreCase("x")) stack.push(x);
            else if (next.equalsIgnoreCase("y")) stack.push(y);
            else { // Token is an operator
                double op2 = stack.pop();
                double result = 0;
                if ("-".equals(next)) {
                    result = stack.pop() - op2;
                } else if ("+".equals(next)) {
                    result = stack.pop() + op2;
                } else if ("*".equals(next)) {
                    result = stack.pop() * op2;
                } else if ("/".equals(next)) {
                    result = stack.pop() / op2;
                } else if ("^".equals(next)) {
                    result = Math.pow(stack.pop(), op2);
                } else if ("cos".equals(next)) {
                    result = Math.cos(op2);
                } else if ("sin".equals(next)) {
                    result = Math.sin(op2);
                }
                stack.push(result);
            }
        }
        double output = stack.pop();
        return (float) output;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        GMath other = (GMath) obj;
        return this.formula.equals(other.formula);
    }

    @Override
    public GMath clone() {
        return new GMath(this.formula, true);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " [ z = f(x, y) = " + this.formula + " ]";
    }

}
