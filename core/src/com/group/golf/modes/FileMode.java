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
import com.group.golf.math.JVector2;
import com.group.golf.math.MathLib;
import com.group.golf.screens.CourseScreen;
import com.group.golf.screens.CourseSelectorScreen;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class FileMode extends GameMode {

    private final Golf game;

    private Sound hitSound;
//    private Sound loseSound;
    private Sound winSound;

    private Physics engine;
    private Course course;

    private Ball ball;
    private JVector2 ballPixels;

    private float[] scales;
    private float[] offsets;

    private boolean landed;
    private int counter;
    private List<String> moves;

    public FileMode(Golf game, Course course, Ball ball, String moves) {
        this.game = game;
        this.course = course;
        this.ball = ball;
        this.counter = 0;
        this.ballPixels = new JVector2(0, 0);
        this.landed = false;

        this.setUpMoves(moves);

        this.setUpBall();

        // Setup sounds
        this.hitSound = Gdx.audio.newSound(Gdx.files.internal("golf_hit_1.wav"));
//        this.loseSound = Gdx.audio.newSound(Gdx.files.internal("defeat_2.wav"));
        this.winSound = Gdx.audio.newSound(Gdx.files.internal("success_2.wav"));
    }

    private void setUpBall() {
        this.ball.setTexture(new Texture(Gdx.files.internal("ball_soccer2.png")));
        this.ball.setX(this.course.getStart()[0]);
        this.ball.setY(this.course.getStart()[1]);
    }

    private void setUpMoves(String moves) {
        this.moves = new ArrayList<String>();
        Scanner in = new Scanner(moves);
        while (in.hasNextLine()) {
            this.moves.add(in.nextLine());
        }
        in.close();
    }

    private void computePixels() {
        float[] ballPixels = MathLib.toPixel(new float[]{this.ball.getX(), this.ball.getY()},
                    this.offsets, this.scales);
        this.ballPixels.setPosition(ballPixels[0], ballPixels[1]);
    }

    @Override
    public void render(Batch batch) {
        this.computePixels();
        this.ball.render(batch, this.ball.getX(), this.ball.getY());
    }

//    @Override
//    public void water() {
//        if (this.engine.isWater()) {
//            this.ball.setX(engine.getHitCoord()[0]);
//            this.ball.setY(engine.getHitCoord()[1]);
//            this.loseSound.play(0.2f);
//        }
//    }

    @Override
    public boolean move(OrthographicCamera cam) {
        if (!this.ball.isMoving()) {
            // Check if the goal is achieved
            if (this.engine.isGoalAchieved(ball)) {
                System.out.println("Ball landed: " + ball.getX() + " " + ball.getY());
                this.winSound.play();
                try { Thread.sleep(3000); }
                catch (Exception e) {}
                this.game.setScreen(new CourseSelectorScreen(this.game));
                return false;
            }
            if (this.landed) {
                System.out.println("Ball landed: " + this.ball.getX() + " " + this.ball.getY());
                this.landed = false;
            }
            // Make a move
            if (this.counter < this.moves.size() && Gdx.input.isKeyPressed(Input.Keys.SPACE)) this.readMove();
            return true;
        } else {
            this.engine.movement(Golf.DELTA, false);
            return true;
        }
    }

    private void readMove() {
        StringTokenizer tokenizer = new StringTokenizer(this.moves.get(this.counter));
        float force = Float.parseFloat(tokenizer.nextToken());
        float angle = Float.parseFloat(tokenizer.nextToken());
        double forceX = force * Math.cos(angle);
        double forceY = force * Math.sin(angle);
        this.engine.hit(ball,(float)(forceX * this.scales[0] * CourseScreen.SCALE_MULTIPLIER),
                (float)(forceY * this.scales[1] * CourseScreen.SCALE_MULTIPLIER));
        this.landed = true;
        this.counter++;
        this.hitSound.play(Golf.HIT_VOLUME);
    }

    @Override
    public void extraChecks() {

    }


    @Override
    public void dispose() {
        this.hitSound.dispose();
        this.winSound.dispose();
    }
}
