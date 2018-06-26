package com.group.golf.ai;

import java.util.List;
import java.util.Random;

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
    private List<Rectangle> walls;
    private Rectangle lastWall;
    private Line2D right;
    private Line2D forward;
    private Line2D left;
    private float lineLength = 38; // By default it´s 30.
    private int currentDir = 0;
    private float extraHitPower = 0;
    private int extraHitPowerCount = 0;
    private int extraHitPowerCounta = 0;
    private boolean cancelHit = false;
    private int moveCount;
    private float[] ballCoords = new float[1500];
    private boolean repeat = false;
    private List<Rectangle> rects;
    private float distanceLimit = 3850;
	private float upperBounda = (float) 8.5; 
	private float lowerBounda = (float) 1;
	private float hitForce = (float) 4.1; // Default 4.1
	private int repeatCount;
	private float extraCheckLength = 7;
	private int actualMoveCount = 0; // For experiments

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
    public void makeMove() {
    	// Calculate ball coords in pixels.
    	//calcExtraPower(lowerBounda, upperBounda);
    	int min = 0;
    	int max = 1;
    	int random = min + (int)(Math.random() * ((max - min) + 1));
    	System.out.println("random: " + random);
    	actualMoveCount++;
    	System.out.println("actualMoveCount: "+actualMoveCount);
    	System.out.println("repeatCount: " + repeatCount);
    	float coordXd = this.ball.getX();
     	float coordYd = this.ball.getY();
     	float[] coords = new float[]{coordXd, coordYd};
     	float[] real = MathLib.toPixel(coords, new float[]{this.course.getOffsets()[0], this.course.getOffsets()[1]},
                 new float[]{this.course.getScales()[0], this.course.getScales()[1]});
     	float realFloatX = (float) real[0];
     	float realFloatY = (float) real[1];

 			float a = (float) realFloatX;
     		float b = (float) realFloatY;

     	// Record ball location and check for repetition in ball movement.
     		if (moveCount >= 10) {
     		ballCoords[moveCount] = a;
     		ballCoords[moveCount + 1] = b;

     		float[] actualMoves = new float[moveCount];
        	for (int i = 0; i < moveCount; i++) {
        		actualMoves[i] = ballCoords[i];
        	}


     		repeat = repetition(actualMoves);
     		}

    	// System.out.println("extraHitPowerCount: " + extraHitPowerCount);
    	if (repeat && repeatCount > 1) {
    		this.engine.hit(this.ball,(float)(Math.random() * ((upperBounda - lowerBounda) + 1) + lowerBounda), (float)(Math.random() * ((upperBounda - lowerBounda) + 1) + lowerBounda));
    		extraHitPowerCounta = 0;
    		cancelHit = true;
    	}
    	
         // 0 = forward, 1 = left, 2 = bot, 3 = right     the 'way' the ball is 'facing'.
    	if (cancelHit == false) {
    	 if (goalBallDistance() < distanceLimit) {
    		 float hitScalar = (float) 0.065;

         		float coordXdGoal = this.course.getGoal()[0];
            	float coordYdGoal = this.course.getGoal()[1];
            	float[] coordsGoal = new float[]{coordXdGoal, coordYdGoal};
            	float[] realGoal = MathLib.toPixel(coordsGoal, new float[]{this.course.getOffsets()[0], this.course.getOffsets()[1]},
                        new float[]{this.course.getScales()[0], this.course.getScales()[1]});
            	float realFloatXGoal = realGoal[0];
            	float realFloatYGoal = realGoal[1];

        			float aGoal = realFloatXGoal;
            		float bGoal = realFloatYGoal;
    		 float[] distances = new float[] {aGoal-a, bGoal-b};
    		 this.engine.hit(this.ball, distances[0]*hitScalar, distances[1]*hitScalar);
    	 }
    	 else	if (currentDir == 0) {
    	if (rightClear()) {
    		this.engine.hit(this.ball,hitForce + extraHitPower, 0);
    		currentDir = 3;
    	}
    	else if (forwardClear()) {
    		this.engine.hit(this.ball, 0, hitForce + extraHitPower);
    		currentDir = 0;
    	}
    	else if (leftClear()) {
    		this.engine.hit(this.ball, -hitForce - extraHitPower, 0);
    		currentDir = 1;
    	}
    	else currentDir = 1;
    	}
    	else if (currentDir == 1) {
    		if (forwardClear()) {
        		this.engine.hit(this.ball, 0, hitForce + extraHitPower);
        		currentDir = 0;
        	}
    		else	if (leftClear()) {
        		this.engine.hit(this.ball, -hitForce - extraHitPower, 0);
        		currentDir = 1;
        	}
    		else if (bottomClear()) {
        		this.engine.hit(this.ball, 0, -hitForce - extraHitPower);
        		currentDir = 2;
        	}
    		else currentDir = 2;
    	}

    	else if (currentDir == 2) {
    		if (leftClear()) {
        		this.engine.hit(this.ball, -hitForce - extraHitPower, 0);
        		currentDir = 1;
        	}
    		else if (bottomClear()) {
        		this.engine.hit(this.ball, 0, -hitForce - extraHitPower);
        		currentDir = 2;
        	}
    		else if (rightClear()) {
        		this.engine.hit(this.ball, hitForce + extraHitPower, 0);
        		currentDir = 3;
        	}
    		else currentDir = 3;
    	}

    	else if (currentDir == 3) {
    		if (bottomClear()) {
        		this.engine.hit(this.ball, 0, -hitForce - extraHitPower);
        		currentDir = 2;
        	}
    		else if (rightClear()) {
        		this.engine.hit(this.ball, hitForce + extraHitPower, 0);
        		currentDir = 3;
        	}
    		else if (forwardClear()) {
        		this.engine.hit(this.ball, 0, hitForce + extraHitPower);
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
    
    // Calculate random extraPower to apply to ball
    private void calcExtraPower(float lower, float upper) {
    	// Randomly picks 1 or 0, which determines whether the extraPower will be negative.
    	int min = 0;
    	int max = 1;
    	int random = min + (int)(Math.random() * ((max - min) + 1));
    	System.out.println("random: " + random);
    	Random r = new Random();
		extraHitPower = lowerBounda + r.nextFloat() * (upperBounda - lowerBounda);
		if (random == 1) {
			extraHitPower = -extraHitPower;
		}
		System.out.println("Extra random error: " + extraHitPower);
		extraHitPowerCount = 0;
    }

    // Place rectangle in given place (added to list of walls). Not in use right now.
    private void placeRect(float x, float y) {
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
	   float[] loc1 = new float[] {(float) Math.round(tracker[tracker.length - 2] * 10) / 10, (float) Math.round(tracker[tracker.length - 1] * 10) / 10};
	   float[] loc2 = new float[] {(float) Math.round(tracker[tracker.length - 4] * 10) / 10, (float) Math.round(tracker[tracker.length - 3] * 10) / 10};
	   float[] loc3 = new float[] {(float) Math.round(tracker[tracker.length - 6] * 10) / 10, (float) Math.round(tracker[tracker.length - 5] * 10) / 10};
	   float[] loc4 = new float[] {(float) Math.round(tracker[tracker.length - 8] * 10) / 10, (float) Math.round(tracker[tracker.length - 7] * 10) / 10};
	   float[] loc5 = new float[] {(float) Math.round(tracker[tracker.length - 10] * 10) / 10, (float) Math.round(tracker[tracker.length - 9] * 10) / 10};
	   
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
		   repeatCount++;
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
        	float coordXd = this.ball.getX();
        	float coordYd = this.ball.getY();
        	float[] coords = new float[]{coordXd, coordYd};
        	float[] real = MathLib.toPixel(coords, new float[]{this.course.getOffsets()[0], this.course.getOffsets()[1]},
                    new float[]{this.course.getScales()[0], this.course.getScales()[1]});
        	float realFloatX = real[0];
        	float realFloatY = real[1];

    			float a = realFloatX + (float) j;
        		float b = realFloatY;
        		float extraB1 = realFloatY + (float) extraCheckLength; // new
        		float extraB2 = realFloatY - (float) extraCheckLength; // new

        		if ((walls.get(i).contains(a, b)) || (walls.get(i).contains(a, extraB1)) || (walls.get(i).contains(a, extraB2))) {
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
    	return result;
    }

 // Check if ahead of ball is clear
    private boolean forwardClear() {
    	boolean result = true;
    	int i = 0;
    	int j = 0;
    	while ((result == true) && (i < walls.size())) {
    		j = j + 1;
        	float coordXd = this.ball.getX();
        	float coordYd = this.ball.getY();
        	float[] coords = new float[]{coordXd, coordYd};
        	float[] real = MathLib.toPixel(coords, new float[]{this.course.getOffsets()[0], this.course.getOffsets()[1]},
                    new float[]{this.course.getScales()[0], this.course.getScales()[1]});
        	float realFloatX = real[0];
        	float realFloatY = real[1];

    			float a = realFloatX;
        		float b = realFloatY + (float) j;
        		float extraA1 = realFloatX - extraCheckLength; // new
        		float extraA2 = realFloatX + extraCheckLength; // new
    		if ((walls.get(i).contains(a, b)) || (walls.get(i).contains(extraA1, b)) ||  walls.get(i).contains(extraA2, b)) {
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
    	return result;
    }


 // Check if left side of ball is clear
    private boolean leftClear() {
    	boolean result = true;
    	int i = 0;
    	int j = 0;
    	while ((result == true) && (i < walls.size())) {
    		j = j + 1;
        	float coordXd = this.ball.getX();
        	float coordYd = this.ball.getY();
        	float[] coords = new float[]{coordXd, coordYd};
        	float[] real = MathLib.toPixel(coords, new float[]{this.course.getOffsets()[0], this.course.getOffsets()[1]},
                    new float[]{this.course.getScales()[0], this.course.getScales()[1]});
        	float realFloatX = real[0];
        	float realFloatY = real[1];

    			float a = realFloatX - (float) j;
        		float b = realFloatY;
        		float extraB1 = realFloatY + (float) extraCheckLength; // new
        		float extraB2 = realFloatY - (float) extraCheckLength; // new

        		if ((walls.get(i).contains(a, b)) || (walls.get(i).contains(a, extraB1)) || (walls.get(i).contains(a, extraB2))) {
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
    	return result;
    }

 // Check if bottom side of ball is clear
    private boolean bottomClear() {
    	boolean result = true;
    	int i = 0;
    	int j = 0;
    	while ((result == true) && (i < walls.size())) {
    		j = j + 1;
        	float coordXd = this.ball.getX();
        	float coordYd = this.ball.getY();
        	float[] coords = new float[]{coordXd, coordYd};
        	float[] real = MathLib.toPixel(coords, new float[]{this.course.getOffsets()[0], this.course.getOffsets()[1]},
                    new float[]{this.course.getScales()[0], this.course.getScales()[1]});
        	float realFloatX = real[0];
        	float realFloatY = real[1];

    			float a = realFloatX;
        		float b = realFloatY - (float) j;
        		float extraA1 = realFloatX + extraCheckLength; // new
        		float extraA2 = realFloatX - extraCheckLength; // new

    		if ((walls.get(i).contains(a, b) == true) || (walls.get(i).contains(extraA1, b)) || (walls.get(i).contains(extraA2, b))) {
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
    	return result;
    }

    // Find distance between given wall and ball
    private float goalBallDistance() {
    	float coordXd = this.ball.getX();
    	float coordYd = this.ball.getY();
    	float[] coords = new float[]{coordXd, coordYd};
    	float[] real = MathLib.toPixel(coords, new float[]{this.course.getOffsets()[0], this.course.getOffsets()[1]},
                new float[]{this.course.getScales()[0], this.course.getScales()[1]});
    	float realFloatX = (float) real[0];
    	float realFloatY = (float) real[1];

    		float coordXdGoal = this.course.getGoal()[0];
        	float coordYdGoal = this.course.getGoal()[1];
        	float[] coordsGoal = new float[]{coordXdGoal, coordYdGoal};
        	float[] realGoal = MathLib.toPixel(coordsGoal, new float[]{this.course.getOffsets()[0], this.course.getOffsets()[1]},
                    new float[]{this.course.getScales()[0], this.course.getScales()[1]});
        	float realFloatXGoal = realGoal[0];
        	float realFloatYGoal = realGoal[1];

    	Point3D goalPoint = new Point3D(realFloatXGoal, realFloatYGoal);
    	Point3D ballPoint = new Point3D(realFloatX, realFloatY);
    	return MathLib.distanceSquared(ballPoint, goalPoint);
    }



}
