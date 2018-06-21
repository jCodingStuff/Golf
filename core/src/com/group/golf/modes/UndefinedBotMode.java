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

public class UndefinedBotMode extends GameMode {

    private final Golf game;

    private Bot[] bots;

    private Sound hitSound;
    private Sound loseSound;
    private Sound winSound;

    private Ball[] balls;
    private JVector2[] ballsPixels;

    private boolean landed;
    private int counter;

    public UndefinedBotMode(Golf game, Bot[] bots, Course course, Ball[] balls) {
        this.game = game;
        this.bots = bots;
        this.course = course;
        this.balls = balls;
        this.initPixels();
        this.counter = 0;
        this.landed = false;

        this.setUpBalls();
        this.setUpPhysics("RK4");

        // Setup sounds
        this.hitSound = Gdx.audio.newSound(Gdx.files.internal("golf_hit_1.wav"));
//        this.loseSound = Gdx.audio.newSound(Gdx.files.internal("defeat_2.wav"));
        this.winSound = Gdx.audio.newSound(Gdx.files.internal("success_2.wav"));
    }

    private void setUpBots() {
        for (int i = 0; i < this.bots.length; i++) {
            this.bots[i].setPhysics(this.engine);
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
            float[] ballPixels = MathLib.toPixel(new float[]{this.balls[i].getX(), this.balls[i].getY()},
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

//    @Override
//    public void water() {
//        Ball ball = this.balls[this.counter];
//        if (engine.isWater()) {
//            ball.setX(engine.getHitCoord()[0]);
//            ball.setY(engine.getHitCoord()[1]);
//            this.loseSound.play(0.2f);
//        }
//    }

    private void incrementCounter() {
        this.counter++;
        if (this.counter >= this.balls.length) this.counter = 0;
    }

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
            this.engine.movement(Golf.DELTA);
            return true;
        }
    }

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

    private void botMove() {
        this.bots[this.counter].makeMove();
        this.informMove();
        this.landed = true;
        this.hitSound.play();
    }

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


    @Override
    public void setScales(float[] scales) {
        this.scales = scales;
        this.setUpBots();
    }

    @Override
    public void dispose() {
        this.hitSound.dispose();
        this.winSound.dispose();
        this.loseSound.dispose();
    }
}
