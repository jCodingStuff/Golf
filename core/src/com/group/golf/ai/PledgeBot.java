package com.group.golf.ai;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
/**
 * Class to generate instances of PledgeBot
 * @author Kaspar Kallast
 * @version 0.2, 19-06-2018
 */
import com.badlogic.gdx.math.Vector2;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.math.Line2D;
import com.group.golf.math.MathLib;
import com.group.golf.math.Point3D;

public class PledgeBot implements Bot { 

	private static final double A_SCALAR = 15; 
    private static final double F_SCALAR = 2; 
    private static final double BIG_SCALAR = 30; 
	
	private final Course course;
    private final Ball ball;
    private Physics engine;
    private Collision collision;
    private List<Rectangle> walls;
    private Rectangle lastWall;
    private Line2D right;
    private Line2D forward;
    private Line2D left;
    private double lineLength = 150;
    private int currentDir = 0;

    /**
     * Create a new instance of PledgeBot
     * @param course the course
     * @param ball the ball
     */
    public PledgeBot(Course course, Ball ball) {
		this.course = course;
		this.ball = ball;
		this.walls = course.getWalls();
	}

    @Override
    public void setPhysics(Physics physics) {
        this.engine = physics;
    }

    @Override
    public void setCollision(Collision collision) {
        this.collision = collision;
    }

    @Override
    public void makeMove() {
    	double extraPower = 6.5; 
    	double distanceLimit = 0.75; 
    	double[] goalCoords = this.course.getGoal();
    	//double[] distances = new double[] {goalCoords[0]-this.ball.getX(), goalCoords[1]-this.ball.getY()};
    	
    	// 0 = forward, 1 = left, 2 = bot, 3 = right     the 'way' the ball is 'facing'.
    	setLines();
    	 if (goalBallDistance() < 0.35) {
    		 double[] distances = new double[] {goalCoords[0]-this.ball.getX(), goalCoords[1]-this.ball.getY()};
    		 this.engine.hit(75, 75);
    	 }
    	 else	if (currentDir == 0) {
    		 System.out.println("At 0");
    	if (rightClear()) {
    		this.engine.hit(200, 0);
    		currentDir = 3;
    	}
    	else if (forwardClear()) {
    		this.engine.hit(0, 200);
    		currentDir = 0;
    	}
    	else if (leftClear()) {
    		this.engine.hit(-200, 0);
    		currentDir = 1;
    	}
    	else currentDir = 1;
    	}
    	else if (currentDir == 1) {
    		System.out.println("At 1");
    		if (forwardClear()) {
        		this.engine.hit(0, 200);
        		currentDir = 0;
        	}
    		else	if (leftClear()) {
        		this.engine.hit(-200, 0);
        		currentDir = 1;
        	}
    		else if (bottomClear()) {
        		this.engine.hit(0, -200);
        		currentDir = 2;
        	}
    		else currentDir = 2;
    	}
    	
    	else if (currentDir == 2) {
    		System.out.println("At 2");
    		if (leftClear()) {
        		this.engine.hit(-200, 0);
        		currentDir = 1;
        	}
    		else if (bottomClear()) {
        		this.engine.hit(0, -200);
        		currentDir = 2;
        	}
    		else if (rightClear()) {
        		this.engine.hit(200, 0);
        		currentDir = 3;
        	}
    		else currentDir = 3;
    	}
    	
    	else if (currentDir == 3) {
    		System.out.println("At 3");
    		if (bottomClear()) {
        		this.engine.hit(0, -200);
        		currentDir = 2;
        	}
    		else if (rightClear()) {
        		this.engine.hit(200, 0);
        		currentDir = 3;
        	}
    		else if (forwardClear()) {
        		this.engine.hit(0, 200);
        		currentDir = 0;
        	}
    		else currentDir = 0;
    	}
    	}
    	
    	/*
    	if ((Math.abs(distances[0]) + Math.abs(distances[1])) / 2 < Math.abs(distanceLimit)) {
    		System.out.println("distanceLimit applied");
    		distances[0] *= extraPower;
        	distances[1] *= extraPower; 
    	}
    	distances[0] *= BIG_SCALAR;
    	distances[1] *= BIG_SCALAR; 
        this.scale(distances);
    	this.engine.hit(distances[0], distances[1]);
    }

    private void scale(double[] forces) {
        double[] derivatives = this.engine.calculateSlope(new double[]{this.ball.getX(), this.ball.getY()});
        for (int i = 0; i < forces.length; i++) {
            forces[i] = this.scaleForce(forces[i], derivatives[i]);
        }
        */
    
    
    // Initialize arrows
    private void setLines() {
    	// Right arrow
    	Point3D rightUsea = new Point3D(this.ball.getX(), this.ball.getY());
    	Point3D rightUseb = new Point3D(this.ball.getX() + lineLength, this.ball.getY());
        this.right = new Line2D(rightUsea, rightUseb);
     // Forward arrow
    	Point3D forwardUsea = new Point3D(this.ball.getX(), this.ball.getY());
    	Point3D forwardUseb = new Point3D(this.ball.getX(), this.ball.getY() + lineLength);
        this.forward = new Line2D(forwardUsea, forwardUseb);
     // Left arrow
    	Point3D leftUsea = new Point3D(this.ball.getX(), this.ball.getY());
    	Point3D leftUseb = new Point3D(this.ball.getX() - lineLength, this.ball.getY());
        this.left = new Line2D(leftUsea, leftUseb);
    }
    
    // Check if right side of ball is clear
    private boolean rightClear() {
    	boolean result = true;
    	int i = 0;
    	int j = 0;
    	while ((result == true) && (i < walls.size())) {
    		j = j + 1;
    		float coordX = (float) this.ball.last()[0];
        	float coordY = (float) this.ball.last()[1];
        	double[] real = MathLib.toPixel(coords, new double[]{offsets[0], offsets[1]},
                    new double[]{scales[0], scales[1]});
        	float realFloatX = (float) real[0];
        	float realFloatY = (float) real[1];
    		    /*
    			float a = (float) this.ball.getX() + (float) j;
        		float b = (float) this.ball.getY();
        		System.out.println(a);
        		System.out.println(b);
        		*/
        		if (walls.get(i).contains(a, b) == true) {
        			System.out.println("rightClear became false");
        			result = false;
        		}
        		else {
        			result = true;
        		}
        		if (j == lineLength) {
            		i++;
            		j = 0;
            		}
    	}
    	if (result == false) {
    		System.out.println("Right not clear");
    	}
    	return result;
    }
    
 // Check if ahead of ball is clear
    private boolean forwardClear() {
    	boolean result = true;
    	int i = 0;
    	int j = 0;
    	while ((result == true) && (i < walls.size())) {
    		j = j + 1;
    		float a = (float) this.ball.getX();
    		float b = (float) this.ball.getY() + (float) j;
    		if (walls.get(i).contains(a, b) == true) {
    			System.out.println("forwardClear became false");
    			result = false;
    		}
    		else {
    			result = true;
    		}
    		if (j == lineLength) {
    		i++;
    		j = 0;
    		}
    	}
    	if (result == false) {
    		System.out.println("Forward not clear");
    	}
    	return result;
    }
    
 // Check if left side of ball is clear
    private boolean leftClear() {
    	boolean result = true;
    	int i = 0;
    	int j = 0;
    	while ((result == true) && (i < walls.size())) {
    		j = j + 1;
    		float a = (float) this.ball.getX() - (float) j;
    		float b = (float) this.ball.getY();
    		if (walls.get(i).contains(a, b) == true) {
    			System.out.println("leftClear became false");
    			result = false;
    		}
    		else {
    			result = true;
    		}
    		if (j == lineLength) {
        		i++;
        		j = 0;
        		}
    	}
    	if (result == false) {
    		System.out.println("Left not clear");
    	}
    	return result;
    }
    
 // Check if leftt side of ball is clear
    private boolean bottomClear() {
    	boolean result = true;
    	int i = 0;
    	int j = 0;
    	while ((result == true) && (i < walls.size())) {
    		j = j + 1;
    		float a = (float) this.ball.getX();
    		float b = (float) this.ball.getY() - (float) j;
    		if (walls.get(i).contains(a, b) == true) {
    			System.out.println("bottomClear became false");
    			result = false;
    		}
    		else {
    			result = true;
    		}
    		if (j == lineLength) {
        		i++;
        		j = 0;
        		}
    	}
    	if (result == false) {
    		System.out.println("Bottom not clear");
    	}
    	return result;
    }
    
    // Find distance between given wall and ball
    private double goalBallDistance() {
    	Point3D goalPoint = new Point3D(this.course.getGoal()[0], this.course.getGoal()[1]);
    	Point3D ballPoint = new Point3D(this.ball.getX(), this.ball.getY());
    	return MathLib.distanceSquared(ballPoint, goalPoint);
    }

    private double scaleForce(double force, double d) {
    	double scaledForce;
    	if (force > 0 && d > 0) {
    		scaledForce = force * Math.abs(d) * A_SCALAR;
    	}
    	else if (force > 0 && d < 0) {
    		scaledForce = force / (Math.abs(d) * F_SCALAR);
    	}
    	else if (force < 0 && d > 0) {
    		scaledForce = force / (Math.abs(d) * F_SCALAR);
    	}
    	else if (force < 0 && d < 0) {
    		scaledForce = force * Math.abs(d) * A_SCALAR;
    	}
    	else {
    		scaledForce = force;
    	}
    	
    	return scaledForce;
    }
    
}
