package com.group.golf.modes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.ai.Bot;
import com.group.golf.math.JVector2;
import com.group.golf.math.MathLib;
import com.group.golf.screens.CourseScreen;
import com.group.golf.screens.CourseSelectorScreen;

/**
 * A mode for any number of bots
 * @author Julian Marrades
 */
public class UndefinedBotMode extends GameMode {

    private final Golf game;

    private Bot[] bots;

    private Sound hitSound;
//    private Sound loseSound;
    private Sound winSound;

    private Ball[] balls;
    private JVector2[] ballsPixels;

    private boolean landed;
    private int counter;

    /**
     * Create a new instance of UndefinedBotMove
     * @param game the game instance
     * @param bots the bots that will play
     * @param course the course being played
     * @param balls the balls that will be managed by the bots
     */
    public UndefinedBotMode(Golf game, Bot[] bots, Course course, Ball[] balls) {
        this.game = game;
        this.bots = bots;
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
     * Setup the bots
     */
    private void setUpBots() {
        for (int i = 0; i < this.bots.length; i++) {
            this.bots[i].setPhysics(this.engine);
        }
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
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) this.botMove();
            return true;
        } else {
            this.engine.movement(Golf.DELTA, false);
            return true;
        }
    }

    /**
     * Inform winner via console
     */
    private void informWinner() {
        int botNum = this.counter + 1;
        String name = this.bots[this.counter].getClass().getName();
        boolean found = false;
        int index = 0;
        for (int i = name.length() - 1; i >=0 && !found; i--) {
            if (name.charAt(i) == '.') {
                found = true;
                index = i;
            }
        }
        name = name.substring(index + 1);
        System.out.println(name + " (index " + this.counter + ") WINS!");
    }

    /**
     * Make the current bot perform a move
     */
    private void botMove() {
        this.bots[this.counter].makeMove();
        this.informMove();
        this.landed = true;
        this.hitSound.play(Golf.HIT_VOLUME);
    }

    /**
     * Inform of the move just made
     */
    private void informMove() {
        int botNum = this.counter + 1;
        String name = this.bots[this.counter].getClass().getName();
        boolean found = false;
        int index = 0;
        for (int i = name.length() - 1; i >=0 && !found; i--) {
            if (name.charAt(i) == '.') {
                found = true;
                index = i;
            }
        }
        name = name.substring(index + 1);
        System.out.println(name + " (index " + this.counter + ") moved!");
    }

    @Override
    public void extraChecks() {

    }


    /**
     * Setup scales and setup bots
     * @param scales the new scales
     */
    @Override
    public void setScales(float[] scales) {
        this.scales = scales;
        this.setUpBots();
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
