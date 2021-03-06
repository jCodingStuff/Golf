package com.group.golf.modes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.*;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.math.JVector2;
import com.group.golf.math.MathLib;
import com.group.golf.screens.CourseScreen;
import com.group.golf.screens.CourseSelectorScreen;

/**
 * A gamemode to edit walls (wall editor)
 * @author Julian Marrades
 */
public class WallCreationMode extends GameMode {

    private final Golf game;


    private Ball[] balls;
    private JVector2[] ballsPixels;
    private Circle[] ballsCollision;

    private boolean touchFlag;
    private int firstX;
    private int firstY;
    private int lastX;
    private int lastY;

    /**
     * Create a new instance of WallCreationMode
     * @param game the game instance
     * @param course the course being played
     * @param balls the ball that will be handled during game-phase
     */
    public WallCreationMode(Golf game, Course course, Ball[] balls) {
        this.game = game;
        this.course = course;
        this.balls = balls;
        this.initPixels();
        if (this.course.getGoal2() == null) { // Normal mode
            this.setUpBalls();
        } else { // 2 Goal mode
            this.setUpTwoGoalMode();
        }
    }

    /**
     * Setup balls for cooperative mode
     */
    private void setUpTwoGoalMode() {
        // Ball1
        this.balls[0].setTexture(new Texture(Gdx.files.internal("ball_soccer2.png")));
        this.balls[0].setX(this.course.getStart()[0]);
        this.balls[0].setY(this.course.getStart()[1]);

        // Ball2
        this.balls[1].setTexture(new Texture(Gdx.files.internal("ball_soccer3.png")));
        this.balls[1].setX(this.course.getStart2()[0]);
        this.balls[1].setY(this.course.getStart2()[1]);
    }

    /**
     * Setup balls normally
     */
    private void setUpBalls() {
        for (Ball ball : this.balls) {
            ball.setTexture(new Texture(Gdx.files.internal("ball_soccer2.png")));
            ball.setX(this.course.getStart()[0]);
            ball.setY(this.course.getStart()[1]);
        }
    }

    /**
     * Initialize the pixels of the balls
     */
    private void initPixels() {
        this.ballsPixels = new JVector2[this.balls.length];
        for (int i = 0; i < this.ballsPixels.length; i++) {
            this.ballsPixels[i] = new JVector2(0, 0);
        }
    }

    /**
     * Compute the pixels of the balls
     */
    private void computePixels() {
        for (int i = 0; i < this.ballsPixels.length; i++) {
            float[] ballPixels = MathLib.toPixel(new float[]{this.balls[i].getX(), this.balls[i].getY()},
                    this.offsets, this.scales);
            this.ballsPixels[i].setPosition(ballPixels[0], ballPixels[1]);
        }
    }

    /**
     * Render the balls
     * @param batch the batch responsible for drawing sprites
     */
    @Override
    public void render(Batch batch) {
//        this.computePixels(); No need, balls don't move!
        for (int i = 0; i < this.balls.length; i++) {
            this.balls[i].render(batch, this.ballsPixels[i].getX(), this.ballsPixels[i].getY());
        }
    }

//    @Override
//    public void water() {}

    /**
     * Look for user input and generate walls
     * @param cam the camera being used which unprojects user input
     * @return always true since there is no movement
     */
    @Override
    public boolean move(OrthographicCamera cam) {
        Vector2 mousePos = CourseScreen.mousePosition(cam);
        if (Gdx.input.isTouched()) {
            if (!this.touchFlag) {
                this.firstX = (int)mousePos.x;
                this.firstY = (int)mousePos.y;
                this.touchFlag = true;
            }
            this.lastX = (int)mousePos.x;
            this.lastY = (int)mousePos.y;
        }
        else if (this.touchFlag) {
            if (this.firstX != this.lastX && this.lastY != this.firstY) {
                Rectangle newWall = MathLib.createRectangle(firstX, firstY, lastX, lastY);
                if (this.isWallAllowed(newWall)) {
                    this.course.addWall(newWall);
                    System.out.println("Created: " + newWall);
                } else {
                    System.out.println("Wall not valid!");
                }
            }
            this.touchFlag = false;
        }
        return true;
    }

    /**
     * Check if a wall is allowed
     * @param wall the wall to check
     * @return true if it does not overlap with wany ball and if its thickness is at least the diameter of the balls
     */
    private boolean isWallAllowed(Rectangle wall) {
        return !this.wallOverlapsBalls(wall) && wall.width >= 2*Ball.RADIUS && wall.height >= 2*Ball.RADIUS;
    }

    /**
     * Check if a wall overlaps a ball
     * @param wall the wall to check
     * @return true if it overlaps a ball, false otherwise
     */
    private boolean wallOverlapsBalls(Rectangle wall) {
        boolean overlap = false;
        for (int i = 0; i < this.balls.length && !overlap; i++) {
            if (Intersector.overlaps(this.ballsCollision[i], wall)) overlap = true;
        }
        return overlap;
    }

    /**
     * Setup pixel collision circles for the balls
     */
    private void setUpCollision() {
        // Setup balls collision
        this.ballsCollision = new Circle[this.balls.length];
        for (int i = 0; i < this.ballsCollision.length; i++) {
            this.ballsCollision[i] = new Circle(this.ballsPixels[i].getX(), this.ballsPixels[i].getY(),
                    Ball.RADIUS);
        }
    }

    @Override
    public void extraChecks() {

    }

    /**
     * Setop the scales, compute the pixels and set up collisions
     * @param scales the new scales
     */
    @Override
    public void setScales(float[] scales) {
        this.scales = scales;
        this.computePixels();
        this.setUpCollision();
    }

    @Override
    public void dispose() {}
}
