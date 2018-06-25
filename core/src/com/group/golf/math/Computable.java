package com.group.golf.math;

/**
 * An interface for holding Computable objects, those that yield a Z out of an X and Y
 */
public interface Computable {

	/**
	 * Get the Z value given a coordinate
	 * @param x the x-component of the coordinate
	 * @param y the y-component of the coordinate
	 * @return the Z that relates to the given (x, y)
	 */
	float getZ(float x, float y);
	
}
