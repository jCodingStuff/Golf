package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.math.MathLib;

/**
 * A class to draw the course
 */
public class CourseScreen implements Screen {

    final Golf game;
    Course course;
    Ball ball;
    Texture ballImage;
    Texture flag;
    OrthographicCamera cam;
    Music music;

    // Graphing things
    private int goalSize;
    private int ballSize;
    private double scale;
    private double xoffset;
    private double yoffset;
    private double[][] heights;
    private double maximum;
    private double minimum;
    private Color[][] colors;

    /**
     * Create a new CourseScreen instance
     * @param game the game itself
     * @param course the course instance we want to graph
     * @param ball the ball to play
     */
    public CourseScreen(final Golf game, Course course, Ball ball) {
        this.game = game;
        this.course = course;

        // Setup Cam
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        // Setup Music
        this.music = Gdx.audio.newMusic(Gdx.files.internal("mario64_ost.mp3"));
        this.music.setVolume(0.2f);
        this.music.setLooping(true);

        // Setup Course
        this.setUpCourse();

        // Setup Ball
        this.ball = ball;
        this.ball.setX(this.course.getStart()[0]);
        this.ball.setY(this.course.getStart()[1]);
        this.ballSize = 20;
        this.ballImage = new Texture(Gdx.files.internal("ball_soccer2.png"));

        // Setup Goal
        this.goalSize = 17;
        this.flag = new Texture(Gdx.files.internal("golf_flag.png"));
    }

    private void setUpCourse() {
        // Set up the scale, each pixel of the screen represents 0.01 units
        this.scale = 0.01;

        // The center of the screen is the middle point between the start and the goal
        this.calcOffsets();

        // Setup the heights matrix
        this.calcHeightsMatrix();

        // Setup the colors matrix
        this.calcColorsMatrix();
    }

    private void calcHeightsMatrix() {
        this.heights = new double[Golf.VIRTUAL_WIDTH][Golf.VIRTUAL_HEIGHT];
        this.maximum = Double.MIN_VALUE;
        this.minimum = Double.MAX_VALUE;
        for (int x = 0; x < this.heights.length; x++) {
            for (int y = 0; y < this.heights[x].length; y++) {
                double value = this.course.getFunction().getZ(xoffset + x*this.scale, yoffset + y*this.scale);
                if (value > this.maximum) this.maximum = value;
                else if (value < this.minimum) this.minimum = value;
                this.heights[x][y] = value;
            }
        }
    }

    private void calcColorsMatrix() {
        float min = 0.3f;
        float max = 1f;
        this.colors = new Color[Golf.VIRTUAL_WIDTH][Golf.VIRTUAL_HEIGHT];
        for (int x = 0; x < this.colors.length; x++) {
            for (int y = 0; y < this.colors[x].length; y++) {
                if (this.heights[x][y] <= 0) {
                    this.colors[x][y] = new Color(0, 0, 1, 1);
                }
                else {
                    // Higher, darker
                    float green = max - min - (float) MathLib.map(this.heights[x][y], this.minimum,
                            this.maximum, min, max);
                    this.colors[x][y] = new Color(0, green, 0, 1);
                }
            }
        }
    }

    private void calcOffsets() {
        double x1 = this.course.getStart()[0];
        double x2 = this.course.getGoal()[0];
        double xUnits = Golf.VIRTUAL_WIDTH / 100.0;
        this.xoffset = (x1 + x2 - xUnits) / 2.0;
        double y1 = this.course.getStart()[1];
        double y2 = this.course.getGoal()[1];
        double yUnits = Golf.VIRTUAL_HEIGHT / 100.0;
        this.yoffset = (y1 + y2 - yUnits) / 2.0;
    }

    @Override
    public void show() {
        this.music.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Cam and projection matrices
        this.cam.update();
        this.game.batch.setProjectionMatrix(this.cam.combined);
        this.game.shapeRenderer.setProjectionMatrix(this.cam.combined);

        // Render the course
        this.renderTerrain();

        // Render the goal
        this.renderGoal();

        // Render the ball
        this.renderBall();

    }

    private void renderGoal() {
        this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        float realX = (float) ((this.course.getGoal()[0] - xoffset) * 100.0);
        float realY = (float) ((this.course.getGoal()[1] - yoffset) * 100.0);
        this.game.shapeRenderer.setColor(0, 0, 0, 1);
        this.game.shapeRenderer.ellipse(realX - this.goalSize/2, realY - this.goalSize/2,
                this.goalSize, this.goalSize);
        this.game.shapeRenderer.end();
        this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        float tolerance = (float) this.course.getTolerance() * 100;
        this.game.shapeRenderer.setColor(1, 0, 0, 1);
        this.game.shapeRenderer.ellipse(realX - tolerance/2, realY - tolerance/2, tolerance, tolerance);
        this.game.shapeRenderer.end();
        this.game.batch.begin();
        this.game.batch.draw(this.flag, realX - 3, realY, 52, 62);
        this.game.batch.end();
    }

    private void renderBall() {
        float realX = (float) ((this.ball.getX() - xoffset) * 100.0);
        float realY = (float) ((this.ball.getY() - yoffset) * 100.0);
        this.game.batch.begin();
        this.game.batch.draw(this.ballImage, realX - this.ballSize/2, realY - this.ballSize/2,
                this.ballSize, this.ballSize);
        this.game.batch.end();
    }

    private void renderTerrain() {
        this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int x = 0; x < this.colors.length; x++) {
            for (int y = 0; y < this.colors[x].length; y++) {
                this.game.shapeRenderer.setColor(this.colors[x][y]);
                this.game.shapeRenderer.rect(x, y, 1, 1); // Draw 1 pixel squares
            }
        }
        this.game.shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        this.music.stop();
    }

    @Override
    public void dispose() {
        this.music.dispose();
    }
}
