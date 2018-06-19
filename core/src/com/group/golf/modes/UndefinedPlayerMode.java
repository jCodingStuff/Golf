package com.group.golf.modes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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

public class UndefinedPlayerMode implements GameMode {

    private final Golf game;

    private Sound hitSound;
    private Sound loseSound;
    private Sound winSound;

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

    public UndefinedPlayerMode(Golf game, Course course, Ball[] balls) {
        this.game = game;
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
        if (engine.isWater()) {
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
        if (!currentBall.isMoving()) {

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
            this.userInput(cam);
            return true;
        } else {
            this.engines[0].movement(currentBall,Gdx.graphics.getDeltaTime());
            return true;
        }
    }

    private void informWinner() {
        int playerNum = this.counter + 1;
        System.out.println("Player " + playerNum + " WINS!");
    }

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

                if (lastX < firstX)
                    xLength *= -1;
                if (lastY < firstY)
                    yLength *= -1;

                double modulus = Math.sqrt(Math.pow((lastX - firstX), 2) + Math.pow((lastY - firstY), 2));

                xLength *= this.scales[0] * CourseScreen.SCALE_MULTIPLIER;
                yLength *= this.scales[1] * CourseScreen.SCALE_MULTIPLIER;

                this.engines[this.counter].hit(balls[this.counter],xLength, yLength);
                this.landed = true;

                this.hitSound.play();

                int playerNum = this.counter + 1;
                System.out.println("Player " + playerNum + " moved!");
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
    }

    @Override
    public void dispose() {
        this.hitSound.dispose();
        this.winSound.dispose();
        this.loseSound.dispose();
    }
}