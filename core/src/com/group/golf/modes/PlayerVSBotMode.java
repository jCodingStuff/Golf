package com.group.golf.modes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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

public class PlayerVSBotMode implements GameMode {

    private final Golf game;

    private Sound hitSound;
    private Sound loseSound;
    private Sound winSound;

    private Bot bot;

    // All arrays have 2 items: player and bot
    private Physics[] engines;
    private Collision[] collisions;
    private Course course;

    private Ball[] balls;
    private JVector2[] ballsPixels;

    private double[] scales;
    private double[] offsets;

    private boolean landed;
    private int counter;

    private boolean touchFlag;
    private int firstX;
    private int firstY;
    private int lastX;
    private int lastY;

    public PlayerVSBotMode(Golf game, Bot bot, Course course, Ball[] balls) {
        this.game = game;
        this.bot = bot;
        this.course = course;
        this.balls = balls;
        this.initPixels();
        this.counter = 0;
        this.landed = false;

        this.setUpBalls();
        this.setUpPhysics();

        // Setup sounds
        this.hitSound = Gdx.audio.newSound(Gdx.files.internal("golf_hit_1.wav"));
        this.loseSound = Gdx.audio.newSound(Gdx.files.internal("defeat_2.wav"));
        this.winSound = Gdx.audio.newSound(Gdx.files.internal("success_2.wav"));
    }

    private void setUpPhysics() {
        this.engines = new Physics[this.balls.length];
        this.collisions = new Collision[this.balls.length];
        for (int i = 0; i < this.engines.length; i++) {
            this.engines[i] = new Physics(this.course, this.balls[i]);
            this.collisions[i] = new Collision(this.balls[i], this.course);
        }
    }

    private void setUpBalls() {
        for (Ball ball : this.balls) {
            ball.setTexture(new Texture(Gdx.files.internal("ball_soccer2.png")));
            ball.setX(this.course.getStart()[0]);
            ball.setY(this.course.getStart()[1]);
        }
    }

    private void initPixels() {
        this.ballsPixels = new JVector2[this.balls.length];
        for (int i = 0; i < this.ballsPixels.length; i++) {
            this.ballsPixels[i] = new JVector2(0, 0);
        }
    }

    private void computePixels() {
        for (int i = 0; i < this.ballsPixels.length; i++) {
            double[] ballPixels = MathLib.toPixel(new double[]{this.balls[i].getX(), this.balls[i].getY()},
                    this.offsets, this.scales);
            this.ballsPixels[i].setPosition(ballPixels[0], ballPixels[1]);
        }
    }

    @Override
    public void render(Batch batch) {
        this.computePixels();
        for (int i = 0; i < this.balls.length; i++) {
            this.balls[i].render(batch, this.ballsPixels[i].getX(), this.ballsPixels[i].getY());
        }
    }

    @Override
    public void water() {
        Ball ball = this.balls[this.counter];
        Physics engine = this.engines[this.counter];
        if (engine.isWater() && ball.getSize() == 0) {
            ball.clear();
            ball.setX(engine.getHitCoord()[0]);
            ball.setY(engine.getHitCoord()[1]);
            this.loseSound.play(0.2f);
        }
    }

    private void incrementCounter() {
        this.counter++;
        if (this.counter >= this.balls.length) this.counter = 0;
    }

    @Override
    public boolean move(OrthographicCamera cam) {
        Ball currentBall = this.balls[this.counter];
        if (currentBall.getSize() == 0) {

            // Check if the goal is achieved
            if (this.collisions[this.counter].isGoalAchieved()) {
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
            currentBall.dequeue();
            return true;
        }
    }

    private void botMove() {
        this.bot.makeMove();
        this.landed = true;
        System.out.println("Bot moves!");
        this.hitSound.play();
    }

    private void informWinner() {
        if (this.counter == 0) {
            System.out.println("HUMAN WINS!");
        } else {
            System.out.println("BOT WINS! (YOU ARE GARBAGE LOL)");
        }
    }

    private void userInput(OrthographicCamera cam) {
        if (Gdx.input.isTouched()) {
            if (!this.touchFlag) {
                this.firstX = Gdx.input.getX();
                this.firstY = Gdx.input.getY();
                this.touchFlag = true;
            }
            this.lastX = Gdx.input.getX();
            this.lastY = Gdx.input.getY();
        }
        else if (this.touchFlag) {
            if (this.firstX != this.lastX || this.lastY != this.firstY) {
                Vector3 firstV = new Vector3(this.firstX, this.firstY,0);
                cam.unproject(firstV);
                Vector3 secondV = new Vector3(this.lastX, this.lastY,0);
                cam.unproject(secondV);


                double xLength = Math.abs(lastX - firstX);
                double yLength = Math.abs(lastY - firstY);

                if (lastX < firstX)
                    xLength *= -1;
                if (lastY > firstY)
                    yLength *= -1;

                double modulus = Math.sqrt(Math.pow((lastX - firstX), 2) + Math.pow((lastY - firstY), 2));

                xLength *= this.scales[0] * CourseScreen.SCALE_MULTIPLIER;
                yLength *= this.scales[1] * CourseScreen.SCALE_MULTIPLIER;

                this.engines[this.counter].hit(xLength, yLength);
                this.landed = true;

                this.hitSound.play();

                int playerNum = this.counter + 1;
                System.out.println("Human moved!");
            }
            this.touchFlag = false;
        }
    }

    @Override
    public void extraChecks() {

    }

    @Override
    public void setOffsets(double[] offsets) {
        this.offsets = offsets;
        for (Physics engine : this.engines) engine.setOffsets(offsets);
    }

    @Override
    public void setScales(double[] scales) {
        this.scales = scales;
        for (Physics engine : this.engines) engine.setScales(scales);
        this.setUpBot();
    }

    private void setUpBot() {
        this.bot.setPhysics(this.engines[1]);
        this.bot.setCollision(this.collisions[1]);
    }

    @Override
    public void dispose() {
        this.hitSound.dispose();
        this.winSound.dispose();
        this.loseSound.dispose();
    }

}
