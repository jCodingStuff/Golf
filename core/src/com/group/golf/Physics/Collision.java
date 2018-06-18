package com.group.golf.Physics;

import com.badlogic.gdx.math.Rectangle;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.math.Line2D;
import com.group.golf.math.MathLib;
import com.group.golf.math.Point3D;
import java.io.*;
import java.util.List;

/**
 * @author Julian Marrades
 * @version 0.1, 13-04-2018
 */
public class Collision {

    private static final double STEP = 0.001;

    private Ball ball;
    private final Course course;

    private double lastX;
    private double lastY;



    /**
     * Create a new instance of Collision
     * @param ball the ball to evaluate
     * @param course the course in which the ball rolls
     */
    public Collision(Ball ball, Course course) {
        this.ball = ball;
        this.course = course;
        this.lastX = this.course.getStart()[0];
        this.lastX = this.course.getStart()[1];
    }

    public Collision(Collision other) {
        this.course = other.course;
        this.ball = other.ball;
        this.lastX = other.lastX;
        this.lastY = other.lastY;
    }

    /**
     * Check if the ball is within the tolerance range from the goal coordinate
     * @return
     */
    public boolean isGoalAchieved() {
        double xToGoal = this.course.getGoal()[0] - this.ball.getX();
        double yToGoal = this.course.getGoal()[1] - this.ball.getY();
        double distToGoal = Math.sqrt(xToGoal * xToGoal + yToGoal * yToGoal);
        return distToGoal <= this.course.getTolerance();
    }
    
    // For multiplayer
    public boolean isGoalAchieved2() {
        double xToGoal = this.course.getGoal2()[0] - this.ball.getX();
        double yToGoal = this.course.getGoal2()[1] - this.ball.getY();
        double distToGoal = Math.sqrt(xToGoal * xToGoal + yToGoal * yToGoal);
        return distToGoal <= this.course.getTolerance();
    }

    /**
     * React when the ball hits a wall
     * @param ballX the pixel-x position of the ball
     * @param ballY the pixel-y position of the ball
     */
    public void checkForWalls(double ballX, double ballY) {
        double vx = this.ball.getVelocityX();
        double vy = this.ball.getVelocityY();
        if ((ballX < Ball.RADIUS && vx < 0) || (ballX > Golf.VIRTUAL_WIDTH - Ball.RADIUS && vx > 0)) {
            this.ball.setVelocityX(-this.ball.getVelocityX());

        }
        if ((ballY < Ball.RADIUS && vy < 0) || (ballY > Golf.VIRTUAL_HEIGHT - Ball.RADIUS && vy > 0)) {
            this.ball.setVelocityY(-this.ball.getVelocityY());
        }
    }
    
    public void checkForGraphicWalls(List<Rectangle> rects, double[] offsets, double[] scales) {
    	try {
    	float extraPower = (float) 0.8;
    	float coordX = (float) this.ball.last()[0];
    	float coordY = (float) this.ball.last()[1];
    	double coordXd = this.ball.last()[0];
    	double coordYd = this.ball.last()[1];
    	double[] coords = new double[]{coordXd, coordYd};
    	double[] real = MathLib.toPixel(coords, new double[]{offsets[0], offsets[1]},
                new double[]{scales[0], scales[1]});
    	float realFloatX = (float) real[0];
    	float realFloatY = (float) real[1];
    	 double vx = this.ball.getVelocityX();
    	 double vy = this.ball.getVelocityY();
    	for (int i = 0; i < rects.size(); i++) {
    		
    		System.out.println("RectangleY: " + rects.get(i).getX() + " RectangleY: " + rects.get(i).getY() );
        	System.out.println("BallX: " + realFloatX + " BallY: " + realFloatY);
            
        	if (rects.get(i).contains(realFloatX,realFloatY+this.ball.RADIUS+(this.ball.RADIUS * extraPower)) || rects.get(i).contains(realFloatX,realFloatY-this.ball.RADIUS-(this.ball.RADIUS * extraPower))) {
        	    if(vx>-1.0 || vx<1.0)
        	        this.ball.setVelocityX(0);
    			System.out.println("Contains");
    			this.ball.setVelocityY(-this.ball.getVelocityY());
    		}
    		if (rects.get(i).contains(realFloatX+this.ball.RADIUS+(this.ball.RADIUS * extraPower),realFloatY) || rects.get(i).contains(realFloatX-this.ball.RADIUS-(this.ball.RADIUS * extraPower),realFloatY)) {
                if(vy>-1.0 || vy<1.0)
                    this.ball.setVelocityY(0);
    			System.out.println("Contains");
    			this.ball.setVelocityX(-this.ball.getVelocityX());
        	}
    	}
    	} catch (NullPointerException e) {
    		System.out.println("Ignored graphic wall check.");
    	}
    	
        
    }
   
    /**
     * Check if the ball has gone to water or is currently in water
     * @return true if it has gone or is in water, false otherwise
     */
    public boolean ballInWater() {
        boolean water = false;

        double ballX = this.ball.last()[0];
        double ballY = this.ball.last()[1];
        Line2D path = new Line2D(this.lastX, this.lastY, ballX, ballY);

        // Evaluate the line
        if (ballX >= this.lastX) { // Ball is moving to the right
            for (double x = this.lastX; x <= ballX && !water; x += STEP) {
                if (this.course.getHeight(x, path.getY(x)) < 0) { // Ball in water
                    water = true;
                }
            }
        } else { // Ball is moving to the left
            for (double x = ballX; x <= this.lastX && !water; x += STEP) {
                if (this.course.getHeight(x, path.getY(x)) < 0) { // Ball in water
                    water = true;
                }
            }
        }


        if (water) {
            // Make the current position of the ball the last
            this.lastX = Physics.hitCoord[0];
            this.lastY = Physics.hitCoord[1];
        } else {
            this.lastX = ballX;
            this.lastY = ballY;
        }

        return water;
    }

    /**
     * Check if there is water in the straight line that joins two points
     * @param a a Point3D instance
     * @param b a Point3D instance
     * @return
     */
    public boolean isWaterBetween(Point3D a, Point3D b) {
        Line2D path = new Line2D(a, b);
        boolean water = false;
        if (b.getX() >= a.getX()) { // B is on the right of A
            for (double x = a.getX(); x <= b.getX() && !water; x += STEP) {
                if (this.course.getHeight(x, path.getY(x)) < 0) { // Ball in water
                    water = true;
                }
            }
        } else { // B is on the left of A
            for (double x = b.getX(); x <= a.getX() && !water; x += STEP) {
                if (this.course.getHeight(x, path.getY(x)) < 0) { // Ball in water
                    water = true;
                }
            }
        }
        return water;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }
}
