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
import com.group.golf.Physics.RK4;
import com.group.golf.ai.Bot;
import com.group.golf.math.JVector2;
import com.group.golf.math.MathLib;
import com.group.golf.screens.CourseScreen;
import com.group.golf.screens.CourseSelectorScreen;

/**
 * A player vs bot gamemode
 * @author Julian Marrades
 */
public class PlayerVSBotMode extends GameMode {

    private final Golf game;

    private Sound hitSound;
//    private Sound loseSound;
    private Sound winSound;

    private Bot bot;

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
     * Create a new instance of PlayerVSBotMode
     * @param game the game instance
     * @param bot the bot to use
     * @param course the course being played
     * @param balls the set of balls to use (user, bot)
     */
    public PlayerVSBotMode(Golf game, Bot bot, Course course, Ball[] balls) {
        this.game = game;
        this.bot = bot;
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
     * Setup the set of balls
     */
    private void setUpBalls() {
        for (Ball ball : this.balls) {
            ball.setTexture(new Texture(Gdx.files.internal("ball_soccer2.png")));
            ball.setX(this.course.getStart()[0]);
            ball.setY(this.course.getStart()[1]);
        }
    }

    /**
     * Setup the balls pixels
     */
    private void initPixels() {
        this.ballsPixels = new JVector2[this.balls.length];
        for (int i = 0; i < this.ballsPixels.length; i++) {
            this.ballsPixels[i] = new JVector2(0, 0);
        }
    }

    /**
     * Compute the pixels for the set of balls
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
     * Highlight the current ball
     */
    private void highlightBall() {
        if (Gdx.input.isKeyPressed(Input.Keys.H)) {
            this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            this.game.shapeRenderer.setColor(Golf.HIGHLIGHT_COLOR);
            this.game.shapeRenderer.circle(this.ballsPixels[0].getX(), this.ballsPixels[0].getY(), Ball.RADIUS);
            this.game.shapeRenderer.end();
        }
    }

    /**
     * Advance turn
     */
    private void incrementCounter() {
        this.counter++;
        if (this.counter >= this.balls.length) this.counter = 0;
    }

    /**
     * Perform the movement
     * @param cam the camera being used
     * @return true if the goal has not been reached, false otherwise
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
            if (this.counter == 0) {
                this.userInput(cam);
            }
            else if (this.counter == 1 && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                this.botMove();
            }
            return true;
        } else {
            this.engine.movement(Golf.DELTA, false);
            return true;
        }
    }

    /**
     * Make the bot perform a move
     */
    private void botMove() {
        this.bot.makeMove();
        this.landed = true;
        System.out.println("Bot moves!");
        this.hitSound.play(Golf.HIT_VOLUME);
    }

    /**
     * Inform of the winner via console
     */
    private void informWinner() {
        if (this.counter == 0) {
            System.out.println("HUMAN WINS!");
        } else {
            System.out.println("BOT WINS!");
        }
    }

    /**
     * Look for user input
     * @param cam the camera to unproject the user input
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

                this.engine.hit(balls[this.counter], xLength, yLength);
                this.landed = true;

                this.hitSound.play(Golf.HIT_VOLUME);

//                int playerNum = this.counter + 1;
                System.out.println("Human moved!");
            }
            this.touchFlag = false;
        }
    }

    @Override
    public void extraChecks() {

    }

    /**
     * Set the new scales and setup the bot
     * @param scales the new scales
     */
    @Override
    public void setScales(float[] scales) {
        this.scales = scales;
        this.setUpBot();
    }

    /**
     * Setup the bot
     */
    private void setUpBot() {
        this.bot.setPhysics(this.engine);
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
