package com.group.golf.ai;

import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;

/**
 * Interface to hold bots
 * @author Julian Marrades
 */
public interface Bot {

    /**
     * Make a move towards the goal
     */
    void makeMove();

    /**
     * Set physics to the bot
     * @param physics the new physics
     */
    void setPhysics(Physics physics);

}
