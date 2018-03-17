/**
* A class to represent a function f(x, y) = z
*
* @author Julian Marrades
* @version 0.1, 17/03/2018
*/

public class Function {

  String name;

  /**
  * Create a new Function instance
  * @param name the name of the function
  */
  public Function(String name) {
    this.name = name;
  }

  /**
  * Get access to the name of the Function
  * @return the name of the function
  */
  public String getName() {
    return this.name;
  }

  /**
  * Set a new name to the function
  * @param name the new name
  */
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public Function clone() {
    return new Function(this.name.clone());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (this.getClass() != obj.getClass()) return false;
    Function other = (Function) obj;
    return this.name.equals(other.name);
  }

  @Override
  public String toString() {
    return "z = f(x, y) = " + this.name;
  }

}
