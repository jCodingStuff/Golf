package com.group.golf.modes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
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

public class WallCreationMode implements GameMode {

    private final Golf game;

    private Course course;
    private Ball[] balls;
    private JVector2[] ballsPixels;

    private double[] scales;
    private double[] offsets;

    private boolean touchFlag;
    private int firstX;
    private int firstY;
    private int lastX;
    private int lastY;

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
    public void water() {}

    @Override
    public boolean move(OrthographicCamera cam) {
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

                // Generate rectangle and add it to the course
                System.out.println("FirstX = " + this.firstX + ", FirstY = " + this.firstY);
                System.out.println("LastX = " + this.lastX + ", LastY = " + this.lastY);
                Rectangle newWall = MathLib.createRectangle(firstX, firstY, lastX, lastY);
                this.course.addWall(newWall);
            }
            this.touchFlag = false;
        }
        return true;
    }

    @Override
    public void extraChecks() {

    }

    @Override
    public void setOffsets(double[] offsets) {
        this.offsets = offsets;
    }

    @Override
    public void setScales(double[] scales) {
        this.scales = scales;
    }

    @Override
    public void dispose() {}
}
