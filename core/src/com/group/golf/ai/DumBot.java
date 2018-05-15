package com.group.golf.ai;

/**
 * Class to generate instances of DumBot
 * @author Kaspar Kallast
 * @version 0.1, 15-05-2018
 */
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;

public class DumBot {
	
	private final Course course;
    private final Ball ball;
    private final Physics engine;
    private final Collision collision;
	
    public DumBot(Course course, Ball ball, Physics engine, Collision collision) {
		this.course = course;
		this.ball = ball;
		this.engine = engine;
		this.collision = collision;
	}

}
