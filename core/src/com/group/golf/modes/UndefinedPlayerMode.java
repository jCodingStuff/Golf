package com.group.golf.modes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
 * A mode for any number of human players
 * @author Julian Marrades
 * @author Alexandros Chimonas
 * @author Martijn Hilders
 */
public class UndefinedPlayerMode extends GameMode {

    private final Golf game;

    private Sound hitSound;
//    private Sound loseSound;
    private Sound winSound;


    private Ball[] balls;
    private JVector2[] ballsPixels;


    private boolean landed;
    private int counter;

    private boolean touchFlag;
    private int firstX;
    private int firstY;
    private int lastX;
    private int lastY;

    /**
     * Create a new instance of UndefinedPlayerMode
     * @param game the game instance
     * @param course the course being played
     * @param balls the balls to be handled by the players
     */
    public UndefinedPlayerMode(Golf game, Course course, Ball[] balls) {
        this.game = game;
        this.course = course;
        this.balls = balls;
        this.initPixels();
        this.counter = 0;
        this.landed = false;

        this.setUpBalls();
//        this.setUpPhysics("RK4");

        // Setup sounds
        this.hitSound = Gdx.audio.newSound(Gdx.files.internal("golf_hit_1.wav"));
//        this.loseSound = Gdx.audio.newSound(Gdx.files.internal("defeat_2.wav"));
        this.winSound = Gdx.audio.newSound(Gdx.files.internal("success_2.wav"));
    }

    /**
     * Setup the balls
     */
    private void setUpBalls() {
        for (Ball ball : this.balls) {
            ball.setTexture(new Texture(Gdx.files.internal("ball_soccer2.png")));
            ball.setX(this.course.getStart()[0]);
            ball.setY(this.course.getStart()[1]);
        }
    }

    /**
     * Initialize the pixels for the balls
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
        this.computePixels();
        for (int i = 0; i < this.balls.length; i++) {
            this.balls[i].render(batch, this.ballsPixels[i].getX(), this.ballsPixels[i].getY());
        }
        this.highlightBall();
    }

    /**
     * Highlight the current ball
     */
    private void highlightBall() {
        if (Gdx.input.isKeyPressed(Input.Keys.H)) {
            this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            this.game.shapeRenderer.setColor(Golf.HIGHLIGHT_COLOR);
            this.game.shapeRenderer.circle(this.ballsPixels[this.counter].getX(),
                    this.ballsPixels[this.counter].getY(), Ball.RADIUS);
            this.game.shapeRenderer.end();
        }
    }

//    @Override
//    public void water() {
//        Ball ball = this.balls[this.counter];
//        if (engine.isWater()) {
//            ball.setX(engine.getHitCoord()[0]);
//            ball.setY(engine.getHitCoord()[1]);
//            this.loseSound.play(0.2f);
//        }
//    }

    /**
     * Advance turn
     */
    private void incrementCounter() {
        this.counter++;
        if (this.counter >= this.balls.length) this.counter = 0;
    }

    /**
     * Perform movement
     * @param cam the camera being used
     * @return false if the goal has been reached, true otherwise
     */
    @Override
    public boolean move(OrthographicCamera cam) {
        Ball currentBall = this.balls[this.counter];
        if (!currentBall.isMoving()) {

            // Check if the goal is achieved
            if (this.engine.isGoalAchieved(currentBall)) {
                System.out.println("Ball landed: " + currentBall.getX() + " " + currentBall.getY());
                this.informWinner();
                this.winSound.play();
                try { Thread.sleep(3000); }
                catch (Exception e) {}
                this.game.setScreen(new CourseSelectorScreen(this.game));
                return false;
            }
            // If landed print
            if (this.landed) {
                System.out.println("Ball landed: " + currentBall.getX() + " " + currentBall.getY());
                this.landed = false;
                this.incrementCounter();
            }
            // Make a move
            this.userInput(cam);
            return true;
        } else {
            this.engine.movement(Golf.DELTA, false);
            return true;
        }
    }

    /**
     * Inform the winner via console
     */
    private void informWinner() {
        int playerNum = this.counter + 1;
        System.out.println("Player " + playerNum + " WINS!");
    }

    /**
     * Look for user input
     * @param cam the camera to unproject user input
     */
    private void userInput(OrthographicCamera cam) {
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
            if (this.firstX != this.lastX || this.lastY != this.firstY) {
                float xLength = Math.abs(lastX - firstX);
                float yLength = Math.abs(lastY - firstY);

                xLength = MathLib.map(xLength, 0, 600, 0, this.course.getVmax());
                yLength = MathLib.map(yLength, 0, 600, 0, this.course.getVmax());

                if (lastX < firstX)
                    xLength *= -1;
                if (lastY < firstY)
                    yLength *= -1;

                double modulus = Math.sqrt(Math.pow((lastX - firstX), 2) + Math.pow((lastY - firstY), 2));


                xLength *= this.scales[0] * CourseScreen.SCALE_MULTIPLIER;
                yLength *= this.scales[1] * CourseScreen.SCALE_MULTIPLIER;

                this.engine.hit(balls[this.counter],xLength, yLength);
                this.landed = true;

                this.hitSound.play(Golf.HIT_VOLUME);

                int playerNum = this.counter + 1;
                System.out.println("Player " + playerNum + " moved!");
            }
            this.touchFlag = false;
        }
    }

    @Override
    public void extraChecks() {
    }

    /**
     * Dispose sounds
     */
    @Override
    public void dispose() {
        this.hitSound.dispose();
        this.winSound.dispose();
    }
}