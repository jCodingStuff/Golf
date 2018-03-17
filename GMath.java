import java.util.StringTokenizer;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

/**
* A class with methods to implement Shunting-Yard Algorithm and
* RPN computations
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
  * Fill precedence and associativity maps
  */
  private void fillMaps() {
    // Fill precedence
    precedence.put("-", 2);
    precedence.put("+", 2);
    precedence.put("/", 3);
    precedence.put("*", 3);
    precedence.put("^", 4);

    // Fill associativity
    associativity.put("-", 0); // Left
    associativity.put("+", 0); // Left
    associativity.put("/", 0); // Left
    associativity.put("*", 0); // Left
    associativity.put("^", 1); // Right
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
      if (next.isNumeric()) { // If number
        output += next + " ";
      }
      // If constant pi or e
      else if (next.equalsIgnoreCaps("pi") || next.equalsIgnoreCaps("e")) {
        output += next + " ";
      }
      // If variable x or y
      else if (next.equalsIgnoreCaps("x") || next.equalsIgnoreCaps("y")) {
        output += next + " ";
      }
      else if (next.equals("(")) { // if Left Bracket
        operators.push(next);
      }
      else if (next.equals(")")) { // if right Bracket

      }
      else { // If operator
        int topPrecerence = -1;
        if (!operators.isEmpty()) {
          topPrecedence = precedence.get(operators.peek());
        }
        int nextPrecedence = precedence.get(next);
        while () {

        }
        operators.push(next);
      }
    }
    this.formula = output;
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
  public double compute(double x, double y) {

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
    return new GMath(this.formula.clone(), true);
  }

  @Override
  public String toString() {
    return "z = f(x, y) = " + this.formula;
  }

}
