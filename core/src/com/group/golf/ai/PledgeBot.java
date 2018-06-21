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
	
	private final Course course;
    private final Ball ball;
    private Physics engine;
    private Collision collision;
    private List<Rectangle> walls;
    private Rectangle lastWall;
    private Line2D right;
    private Line2D forward;
    private Line2D left;
    private double lineLength = 30; // In the very beginning it was 30.
    private int currentDir = 0;
    private double extraHitPower = 0;
    private int extraHitPowerCount = 0;
    private int extraHitPowerCounta = 0;
    private boolean cancelHit = false;
    private int moveCount;
    private float[] ballCoords = new float[1500];
    private boolean repeat = false;
    private List<Rectangle> rects;
    private double distanceLimit = 3850; 
	private double upperBound = 138;
	private double lowerBound = 7.5;
	private double upperBounda = 300;
	private double lowerBounda = -300;

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
    	// Calculate ball coords in pixels.
    	double coordXd = this.ball.getX();
     	double coordYd = this.ball.getY();
     	double[] coords = new double[]{coordXd, coordYd};
     	double[] real = MathLib.toPixel(coords, new double[]{this.course.getOffsets()[0], this.course.getOffsets()[1]},
                 new double[]{this.course.getScales()[0], this.course.getScales()[1]});
     	float realFloatX = (float) real[0];
     	float realFloatY = (float) real[1];
 		    
 			float a = (float) realFloatX;
     		float b = (float) realFloatY;
     		
     	// Record ball location and check for repetition
     		if (moveCount >= 10) {
     		ballCoords[moveCount] = a;
     		ballCoords[moveCount + 1] = b;
     		
     		float[] actualMoves = new float[moveCount];
        	for (int i = 0; i < moveCount; i++) {
        		actualMoves[i] = ballCoords[i];
        	}
     		
     		
     		repeat = repetition(actualMoves);
     		}
     		
    	System.out.println("extraHitPowerCount: " + extraHitPowerCount);
    	if (repeat) {
    		this.engine.hit((double)(Math.random() * ((upperBounda - lowerBounda) + 1) + lowerBounda), (double)(Math.random() * ((upperBounda - lowerBounda) + 1) + lowerBounda));
    		extraHitPowerCounta = 0;
    		cancelHit = true;
    	}
    	
    	/*
    	if (extraHitPowerCount == 2) {
    		extraHitPower = (double)(Math.random() * ((upperBound - lowerBound) + 1) + lowerBound);
    		extraHitPowerCount = 0;
    	}
    	*/
    	
    	// 0 = forward, 1 = left, 2 = bot, 3 = right     the 'way' the ball is 'facing'.
    	if (cancelHit == false) {
    	 if (goalBallDistance() < distanceLimit) {
    		 double hitScalar = 4.25;
         		
         		double coordXdGoal = this.course.getGoal()[0];
            	double coordYdGoal = this.course.getGoal()[1];
            	double[] coordsGoal = new double[]{coordXdGoal, coordYdGoal};
            	double[] realGoal = MathLib.toPixel(coordsGoal, new double[]{this.course.getOffsets()[0], this.course.getOffsets()[1]},
                        new double[]{this.course.getScales()[0], this.course.getScales()[1]});
            	float realFloatXGoal = (float) realGoal[0];
            	float realFloatYGoal = (float) realGoal[1];
        		    
        			float aGoal = (float) realFloatXGoal;
            		float bGoal = (float) realFloatYGoal;
    		 System.out.println("Hitting directly to goal.");
    		 double[] distances = new double[] {aGoal-a, bGoal-b};
    		 this.engine.hit(distances[0]*hitScalar, distances[1]*hitScalar);
    	 }
    	 else	if (currentDir == 0) {
    		 System.out.println("At 0");
    	if (rightClear()) {
    		this.engine.hit(200 + extraHitPower, 0);
    		currentDir = 3;
    	}
    	else if (forwardClear()) {
    		this.engine.hit(0, 200 + extraHitPower);
    		currentDir = 0;
    	}
    	else if (leftClear()) {
    		this.engine.hit(-200 - extraHitPower, 0);
    		currentDir = 1;
    	}
    	else currentDir = 1;
    	}
    	else if (currentDir == 1) {
    		System.out.println("At 1");
    		if (forwardClear()) {
        		this.engine.hit(0, 200 + extraHitPower);
        		currentDir = 0;
        	}
    		else	if (leftClear()) {
        		this.engine.hit(-200 - extraHitPower, 0);
        		currentDir = 1;
        	}
    		else if (bottomClear()) {
        		this.engine.hit(0, -200 - extraHitPower);
        		currentDir = 2;
        	}
    		else currentDir = 2;
    	}
    	
    	else if (currentDir == 2) {
    		System.out.println("At 2");
    		if (leftClear()) {
        		this.engine.hit(-200 - extraHitPower, 0);
        		currentDir = 1;
        	}
    		else if (bottomClear()) {
        		this.engine.hit(0, -200 - extraHitPower);
        		currentDir = 2;
        	}
    		else if (rightClear()) {
        		this.engine.hit(200 + extraHitPower, 0);
        		currentDir = 3;
        	}
    		else currentDir = 3;
    	}
    	
    	else if (currentDir == 3) {
    		System.out.println("At 3");
    		if (bottomClear()) {
        		this.engine.hit(0, -200 - extraHitPower);
        		currentDir = 2;
        	}
    		else if (rightClear()) {
        		this.engine.hit(200 + extraHitPower, 0);
        		currentDir = 3;
        	}
    		else if (forwardClear()) {
        		this.engine.hit(0, 200 + extraHitPower);
        		currentDir = 0;
        	}
    		else currentDir = 0;
    	}
      }
    	 cancelHit = false;
    	 extraHitPowerCount++;
    	 extraHitPowerCounta++;
    	 extraHitPower = 0;
    	 moveCount += 2;
    }
    
    // Place rectangle in given place (added to list of walls)
    private void placeRect(double x, double y) {
    	float floatX = (float) x;
    	float floatY = (float) y;
    	float rectWidth = 30;
    	float rectHeight = 30;
    	Rectangle rect = new Rectangle();
    	rect.setCenter(floatX, floatY - 15);
    	rect.setWidth(rectWidth);
    	rect.setHeight(rectHeight);
    	walls.add(rect);  	
    }
    
    // Check for movement repetition
   private boolean repetition(float[] tracker) {
	   boolean isRepeating = false;
	   
	   float[] loc1 = new float[] {tracker[tracker.length - 2], tracker[tracker.length - 1]};
	   float[] loc2 = new float[] {tracker[tracker.length - 4], tracker[tracker.length - 3]};
	   float[] loc3 = new float[] {tracker[tracker.length - 6], tracker[tracker.length - 5]};
	   float[] loc4 = new float[] {tracker[tracker.length - 8], tracker[tracker.length - 7]};
	   float[] loc5 = new float[] {tracker[tracker.length - 10], tracker[tracker.length - 9]};
	   
	   if ((loc1[0] == loc2[0]) &&  (loc1[1] == loc2[1]) && (loc1[0] == loc3[0]) && (loc1[1] == loc3[1])) {
		   isRepeating = true;
	   }
	   else if ((loc1[0] == loc3[0]) && (loc1[1] == loc3[1])) {
		   isRepeating = true;
	   }
	   else if ((loc1[0] == loc4[0]) && (loc1[1] == loc4[1])) {
		   isRepeating = true;
	   }
	   else if ((loc1[0] == loc5[0]) && (loc1[1] == loc5[1])) {
		   isRepeating = true;
	   }
	   
	  
	   if (isRepeating == true) {
		   System.out.println("Repetition detected!");
	   }
	   
	   // placeRect(loc2[0], loc2[1]);
	   return isRepeating;
   }
    
    // Check if right side of ball is clear
    private boolean rightClear() {
    	boolean result = true;
    	int i = 0;
    	int j = 0;
    	while ((result == true) && (i < walls.size())) {
    		j = j + 1;
        	double coordXd = this.ball.getX();
        	double coordYd = this.ball.getY();
        	double[] coords = new double[]{coordXd, coordYd};
        	double[] real = MathLib.toPixel(coords, new double[]{this.course.getOffsets()[0], this.course.getOffsets()[1]},
                    new double[]{this.course.getScales()[0], this.course.getScales()[1]});
        	float realFloatX = (float) real[0];
        	float realFloatY = (float) real[1];
    		    
    			float a = (float) realFloatX + (float) j;
        		float b = (float) realFloatY;
        		
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
        	double coordXd = this.ball.getX();
        	double coordYd = this.ball.getY();
        	double[] coords = new double[]{coordXd, coordYd};
        	double[] real = MathLib.toPixel(coords, new double[]{this.course.getOffsets()[0], this.course.getOffsets()[1]},
                    new double[]{this.course.getScales()[0], this.course.getScales()[1]});
        	float realFloatX = (float) real[0];
        	float realFloatY = (float) real[1];
    		    
    			float a = (float) realFloatX;
        		float b = (float) realFloatY + (float) j;
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
        	double coordXd = this.ball.getX();
        	double coordYd = this.ball.getY();
        	double[] coords = new double[]{coordXd, coordYd};
        	double[] real = MathLib.toPixel(coords, new double[]{this.course.getOffsets()[0], this.course.getOffsets()[1]},
                    new double[]{this.course.getScales()[0], this.course.getScales()[1]});
        	float realFloatX = (float) real[0];
        	float realFloatY = (float) real[1];
    		    
    			float a = (float) realFloatX - (float) j;
        		float b = (float) realFloatY;
    		
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
    
 // Check if left side of ball is clear
    private boolean bottomClear() {
    	boolean result = true;
    	int i = 0;
    	int j = 0;
    	while ((result == true) && (i < walls.size())) {
    		j = j + 1;
        	double coordXd = this.ball.getX();
        	double coordYd = this.ball.getY();
        	double[] coords = new double[]{coordXd, coordYd};
        	double[] real = MathLib.toPixel(coords, new double[]{this.course.getOffsets()[0], this.course.getOffsets()[1]},
                    new double[]{this.course.getScales()[0], this.course.getScales()[1]});
        	float realFloatX = (float) real[0];
        	float realFloatY = (float) real[1];
    		    
    			float a = (float) realFloatX;
        		float b = (float) realFloatY - (float) j;
    		
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
    	double coordXd = this.ball.getX();
    	double coordYd = this.ball.getY();
    	double[] coords = new double[]{coordXd, coordYd};
    	double[] real = MathLib.toPixel(coords, new double[]{this.course.getOffsets()[0], this.course.getOffsets()[1]},
                new double[]{this.course.getScales()[0], this.course.getScales()[1]});
    	float realFloatX = (float) real[0];
    	float realFloatY = (float) real[1];
		    
			float a = (float) realFloatX;
    		float b = (float) realFloatY;
    		
    		double coordXdGoal = this.course.getGoal()[0];
        	double coordYdGoal = this.course.getGoal()[1];
        	double[] coordsGoal = new double[]{coordXdGoal, coordYdGoal};
        	double[] realGoal = MathLib.toPixel(coordsGoal, new double[]{this.course.getOffsets()[0], this.course.getOffsets()[1]},
                    new double[]{this.course.getScales()[0], this.course.getScales()[1]});
        	float realFloatXGoal = (float) realGoal[0];
        	float realFloatYGoal = (float) realGoal[1];
    		    
    			float aGoal = (float) realFloatXGoal;
        		float bGoal = (float) realFloatYGoal;
    	
    	Point3D goalPoint = new Point3D(aGoal, bGoal);
    	Point3D ballPoint = new Point3D(a, b);
    	System.out.println("Goal ball distance: " + MathLib.distanceSquared(ballPoint, goalPoint));
    	return MathLib.distanceSquared(ballPoint, goalPoint);
    }

    
    
}
