package com.group.golf.genetics;

import com.group.golf.Ball;
import com.group.golf.Physics.Collision;

public interface GoalChecker {

    boolean goalReached(double[] goal, Individual[] population, Ball virtualBall, Collision virtualCollision);

}
