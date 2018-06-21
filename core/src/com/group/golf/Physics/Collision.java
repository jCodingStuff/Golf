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
 * @author Julian Marrades
 * @version 0.1, 13-04-2018
 */
public class Collision {

    private static final double STEP = 0.001;
    private static final double STOP_CONDITION = 0.17;

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

    public Collision(Collision other) {
        this.course = other.course;
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
    
    public boolean checkForGraphicWalls(double ballX, double ballY, List<Rectangle> rects) {
        for (Rectangle wall : rects) {
            if (this.hittingWallRight(ballX, ballY, wall)) { // Hitting by the right
//                System.out.println("Hitting by the right!");
                if (this.stopConditions()) return false;
                this.ball.invertVelocityX();
            } else if (this.hittingWallLeft(ballX, ballY, wall)) { // Hitting by the left
//                System.out.println("Hitting by the left!");
                if (this.stopConditions()) return false;
                this.ball.invertVelocityX();
            } else if (this.hittingWallTop(ballX, ballY, wall)) { // Hitting from the top
//                System.out.println("Hitting by the top!");
                if (this.stopConditions()) return false;
                this.ball.invertVelocityY();
            } else if (this.hittingWallBottom(ballX, ballY, wall)) { // Hitting from the bottom
//                System.out.println("Hitting by the bottom!");
                if (this.stopConditions()) return false;
                this.ball.invertVelocityY();
            }
        }
        return true;
    }

    private boolean hittingWallRight(double ballX, double ballY, Rectangle wall) {
        return (ballY - Ball.RADIUS/2 <= wall.y + wall.height &&
                ballY + Ball.RADIUS/2 >= wall.y &&
                ballX - (wall.x + wall.width) >= -Ball.RADIUS &&
                ballX - (wall.x + wall.width) <= Ball.RADIUS &&
                this.ball.getVelocityX() < 0);
    }

    private boolean hittingWallLeft(double ballX, double ballY, Rectangle wall) {
        return (ballY - Ball.RADIUS/2 <= wall.y + wall.height &&
                ballY + Ball.RADIUS/2 >= wall.y &&
                ballX - wall.x <= Ball.RADIUS &&
                ballX - wall.x >= -Ball.RADIUS &&
                this.ball.getVelocityX() > 0);
    }

    private boolean hittingWallTop(double ballX, double ballY, Rectangle wall) {
        return (ballX - Ball.RADIUS/2 >= wall.x &&
                ballX - Ball.RADIUS/2 <= wall.x + wall.width &&
                ballY - (wall.y + wall.height) <= Ball.RADIUS &&
                ballY - (wall.y + wall.height) >= -Ball.RADIUS &&
                this.ball.getVelocityY() < 0);
    }

    private boolean hittingWallBottom(double ballX, double ballY, Rectangle wall) {
        return (ballX - Ball.RADIUS/2 >= wall.x &&
                ballX - Ball.RADIUS/2 <= wall.x + wall.width &&
                ballY - wall.y <= Ball.RADIUS &&
                ballY - wall.y >= -Ball.RADIUS &&
                this.ball.getVelocityY() > 0);
    }

    private boolean stopConditions() {
        if (Math.abs(this.ball.getVelocityX()) <= STOP_CONDITION &&
                Math.abs(this.ball.getVelocityY()) <= STOP_CONDITION) {
            this.ball.reset();
            //System.out.println("Ball stopping because of the wall!");
            return true;
        } else {
            return false;
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
            for (float x = a.getX(); x <= b.getX() && !water; x += STEP) {
                if (this.course.getHeight(x, path.getY(x)) < 0) { // Ball in water
                    water = true;
                }
            }
        } else { // B is on the left of A
            for (float x = b.getX(); x <= a.getX() && !water; x += STEP) {
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
        this.lastX = this.ball.getX();
        this.lastY = this.ball.getY();
    }
}
