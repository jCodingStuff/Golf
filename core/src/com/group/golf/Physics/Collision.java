package com.group.golf.Physics;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
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
 * A class to handle collisions in the Golf game
 * @author Julian Marrades
 * @author Alexandros Chimonas
 * @author Kaspar Kallast
 */
public class Collision {

    private static final float STEP = 0.001f;
    private static final float STOP_CONDITION = 0.15f;

    private Ball ball;
    private final Course course;

    private float lastX;
    private float lastY;



    /**
     * Create a new instance of Collision
     * @param course the course in which the ball rolls
     */
    public Collision(Course course) {
        this.course = course;
        this.lastX = this.course.getStart()[0];
        this.lastX = this.course.getStart()[1];
    }

    /**
     * Create a new Collision instance from a template
     * @param other the other Collision instance acting as a template
     */
    public Collision(Collision other) {
        this.course = other.course;
        this.lastX = other.lastX;
        this.lastY = other.lastY;
    }

    /**
     * Check if the ball is within the tolerance range from the goal coordinates
     * @return true if the goal has been reached, false otherwise
     */
    public boolean isGoalAchieved() {
        float xToGoal = this.course.getGoal()[0] - this.ball.getX();
        float yToGoal = this.course.getGoal()[1] - this.ball.getY();
        float distToGoal = (float) Math.sqrt(xToGoal * xToGoal + yToGoal * yToGoal);
        return distToGoal <= this.course.getTolerance();
    }

    /**
     * Check if the ball is within the tolerance range from the goal2 coordinates
     * @return true if the goal2 has been reached, false otherwise
     */
    public boolean isGoalAchieved2() {
        float xToGoal = this.course.getGoal2()[0] - this.ball.getX();
        float yToGoal = this.course.getGoal2()[1] - this.ball.getY();
        float distToGoal = (float) Math.sqrt(xToGoal * xToGoal + yToGoal * yToGoal);
        return distToGoal <= this.course.getTolerance();
    }

    /**
     * React when the ball hits a wall
     * @param ballX the pixel-x position of the ball
     * @param ballY the pixel-y position of the ball
     */
    public void checkForWalls(float ballX, float ballY) {
        float vx = this.ball.getVelocityX();
        float vy = this.ball.getVelocityY();
        if ((ballX < Ball.RADIUS && vx < 0) || (ballX > Golf.VIRTUAL_WIDTH - Ball.RADIUS && vx > 0)) {
            this.ball.invertVelocityX();
//            System.out.println("Hitting side wall");
//            System.out.println("BallX: " + ballX + ", BallY" + ballY);

        }
        if ((ballY < Ball.RADIUS && vy < 0) || (ballY > Golf.VIRTUAL_HEIGHT - Ball.RADIUS && vy > 0)) {
            this.ball.invertVelocityY();
//            System.out.println("Hitting up wall");
//            System.out.println("BallX: " + ballX + ", BallY" + ballY);

        }
    }

    /**
     * Manage collisions with graphic walls
     * @param ballX the pixel x-coordinate of the ball
     * @param ballY the pixel y-coordinate of the ball
     * @param rects the set of walls
     */
    public void checkForGraphicWalls(float ballX, float ballY, List<Rectangle> rects) {
        for (Rectangle wall : rects) {
            if (this.hittingWallRight(ballX, ballY, wall)) { // Hitting by the right
//                System.out.println("Hitting by the right!");
                this.stopConditions();
                this.ball.invertVelocityX();
            } else if (this.hittingWallLeft(ballX, ballY, wall)) { // Hitting by the left
//                System.out.println("Hitting by the left!");
                this.stopConditions();
                this.ball.invertVelocityX();
            } else if (this.hittingWallTop(ballX, ballY, wall)) { // Hitting from the top
//                System.out.println("Hitting by the top!");
                this.stopConditions();
                this.ball.invertVelocityY();
            } else if (this.hittingWallBottom(ballX, ballY, wall)) { // Hitting from the bottom
//                System.out.println("Hitting by the bottom!");
                this.stopConditions();
                this.ball.invertVelocityY();
            }
        }
    }

    /**
     * Check if a wall is being hit by the right
     * @param ballX the pixel x-coordinate of the ball
     * @param ballY the pixel y-coordinate of the ball
     * @param wall the wall to check
     * @return true if the wall has been hit by the right, false otherwise
     */
    private boolean hittingWallRight(float ballX, float ballY, Rectangle wall) {
        return (ballY <= wall.y + wall.height &&
                ballY >= wall.y &&
                ballX - (wall.x + wall.width) >= -Ball.RADIUS &&
                ballX - (wall.x + wall.width) <= Ball.RADIUS &&
                this.ball.getVelocityX() < 0);
    }

    /**
     * Check if a wall is being hit by the left
     * @param ballX the pixel x-coordinate of the ball
     * @param ballY the pixel y-coordinate of the ball
     * @param wall the wall to check
     * @return true if the wall has been hit by the left, false otherwise
     */
    private boolean hittingWallLeft(float ballX, float ballY, Rectangle wall) {
        return (ballY <= wall.y + wall.height &&
                ballY >= wall.y &&
                ballX - wall.x <= Ball.RADIUS &&
                ballX - wall.x >= -Ball.RADIUS &&
                this.ball.getVelocityX() > 0);
    }

    /**
     * Check if a wall is being hit by the top
     * @param ballX the pixel x-coordinate of the ball
     * @param ballY the pixel y-coordinate of the ball
     * @param wall the wall to check
     * @return true if the wall has been hit by the top, false otherwise
     */
    private boolean hittingWallTop(float ballX, float ballY, Rectangle wall) {
        return (ballX >= wall.x &&
                ballX <= wall.x + wall.width &&
                ballY - (wall.y + wall.height) <= Ball.RADIUS &&
                ballY - (wall.y + wall.height) >= -Ball.RADIUS &&
                this.ball.getVelocityY() < 0);
    }

    /**
     * Check if a wall is being hit by the bottom
     * @param ballX the pixel x-coordinate of the ball
     * @param ballY the pixel y-coordinate of the ball
     * @param wall the wall to check
     * @return true if the wall has been hit by the bottom, false otherwise
     */
    private boolean hittingWallBottom(float ballX, float ballY, Rectangle wall) {
        return (ballX >= wall.x &&
                ballX <= wall.x + wall.width &&
                ballY - wall.y <= Ball.RADIUS &&
                ballY - wall.y >= -Ball.RADIUS &&
                this.ball.getVelocityY() > 0);
    }

    /**
     * Stop the ball if it meets the stop conditions when hitting a wall
     */
    private void stopConditions() {
        if (Math.abs(this.ball.getVelocityX()) <= STOP_CONDITION &&
                Math.abs(this.ball.getVelocityY()) <= STOP_CONDITION) {
            this.ball.reset();
        }
    }
   
    /**
     * Check if the ball has gone to water or is currently in water
     * @return true if it has gone or is in water, false otherwise
     */
    public boolean ballInWater() {
        boolean water = false;

        float ballX = this.ball.getX();
        float ballY = this.ball.getY();
        Line2D path = new Line2D(this.lastX, this.lastY, ballX, ballY);

        // Evaluate the line
        if (ballX >= this.lastX) { // Ball is moving to the right
            for (float x = this.lastX; x <= ballX && !water; x += STEP) {
                if (this.course.getHeight(x, path.getY(x)) < 0) { // Ball in water
                    water = true;
                }
            }
        } else { // Ball is moving to the left
            for (float x = ballX; x <= this.lastX && !water; x += STEP) {
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

//    /**
//     * Check if there is water in the straight line that joins two points
//     * @param a a Point3D instance
//     * @param b a Point3D instance
//     * @return
//     */
//    public boolean isWaterBetween(Point3D a, Point3D b) {
//        Line2D path = new Line2D(a, b);
//        boolean water = false;
//        if (b.getX() >= a.getX()) { // B is on the right of A
//            for (float x = a.getX(); x <= b.getX() && !water; x += STEP) {
//                if (this.course.getHeight(x, path.getY(x)) < 0) { // Ball in water
//                    water = true;
//                }
//            }
//        } else { // B is on the left of A
//            for (float x = b.getX(); x <= a.getX() && !water; x += STEP) {
//                if (this.course.getHeight(x, path.getY(x)) < 0) { // Ball in water
//                    water = true;
//                }
//            }
//        }
//        return water;
//    }

    /**
     * Get access to the ball instance being handled
     * @return the instance of ball
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * Set a new ball instance
     * @param ball the new ball instance
     */
    public void setBall(Ball ball) {
        this.ball = ball;
        this.lastX = this.ball.getX();
        this.lastY = this.ball.getY();
    }
}
