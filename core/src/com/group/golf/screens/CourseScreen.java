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

    Golf game;

    Course course;
    Ball ball;
    Texture ballImage;
    Texture flag;
    OrthographicCamera cam;
    Music music;

    // Graphing things
    private int goalSize;
    private String moves;
    private int ballSize;
    private double scale;
    private double xoffset;
    private double yoffset;
    private double[][] heights;
    private double maximum;
    private double minimum;
    private Color[][] colors;

    public CourseScreen(Golf game, Course course, Ball ball, String moves) {
        this.game = game;
        this.course = course;

        this.moves = moves;

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
        this.goalSize = 20;
        this.flag = new Texture(Gdx.files.internal("golf_flag.png"));
    }

    /**
     * Create a new CourseScreen instance
     * @param game the game itself
     * @param course the course instance we want to graph
     * @param ball the ball to play
     */
    public CourseScreen(Golf game, Course course, Ball ball) {
        this.game = game;
        this.course = course;

        this.moves = "";

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
        this.goalSize = 20;
        this.flag = new Texture(Gdx.files.internal("golf_flag.png"));
    }

    private void setUpCourse() {
        // Set up the scale, each pixel of the screen represents this.scale units
        double dist = this.course.getDistance();
        if (dist <= 1.5) this.scale = 0.0025;
        else if (dist <= 3) this.scale = 0.005;
        else if (dist <= 6) this.scale = 0.01;
        else if (dist <= 12) this.scale = 0.02;
        else this.scale = 0.03;

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
                double value = this.course.getFunction().getZ(this.xoffset + x*this.scale,
                        this.yoffset + y*this.scale);
                if (value > this.maximum) this.maximum = value;
                else if (value < this.minimum) this.minimum = value;
                this.heights[x][y] = value;
            }
        }
    }

    private void calcColorsMatrix() {
        float minTerrain = 0.1f;
        float maxTerrain = 0.9f;
        float minWater = 0.2f;
        float maxWater = 1f;
        this.colors = new Color[Golf.VIRTUAL_WIDTH][Golf.VIRTUAL_HEIGHT];
        for (int x = 0; x < this.colors.length; x++) {
            for (int y = 0; y < this.colors[x].length; y++) {
                if (this.heights[x][y] < 0) { // Water
                    // More negative, darker
                    float blue = maxWater + minWater - (float) MathLib.map(Math.abs(this.heights[x][y]), 0,
                            Math.abs(this.minimum), minWater, maxWater);
                    this.colors[x][y] = new Color(0, 0, blue, 1);
                }
                else { // Grass
                    // Higher, darker
                    float green = maxTerrain + minTerrain - (float) MathLib.map(this.heights[x][y], 0,
                            this.maximum, minTerrain, maxTerrain);
                    this.colors[x][y] = new Color(0, green, 0, 1);
                }
            }
        }
    }

    private void calcOffsets() {
        double x1 = this.course.getStart()[0];
        double x2 = this.course.getGoal()[0];
        double xUnits = Golf.VIRTUAL_WIDTH / (1/this.scale);
        this.xoffset = (x1 + x2 - xUnits) / 2.0;
        double y1 = this.course.getStart()[1];
        double y2 = this.course.getGoal()[1];
        double yUnits = Golf.VIRTUAL_HEIGHT / (1/this.scale);
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
        float realX = (float) ((this.course.getGoal()[0] - this.xoffset) * (1/this.scale));
        float realY = (float) ((this.course.getGoal()[1] - this.yoffset) * (1/this.scale));
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
        float realX = (float) ((this.ball.getX() - this.xoffset) * (1/this.scale));
        float realY = (float) ((this.ball.getY() - this.yoffset) * (1/this.scale));
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
        this.ballImage.dispose();
        this.flag.dispose();
    }

    public Golf getGame() {
        return game;
    }

    public void setGame(Golf game) { this.game = game; };

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public Texture getBallImage() {
        return ballImage;
    }

    public void setBallImage(Texture ballImage) {
        this.ballImage = ballImage;
    }

    public Texture getFlag() {
        return flag;
    }

    public void setFlag(Texture flag) {
        this.flag = flag;
    }

    public OrthographicCamera getCam() {
        return cam;
    }

    public void setCam(OrthographicCamera cam) {
        this.cam = cam;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public int getGoalSize() {
        return goalSize;
    }

    public void setGoalSize(int goalSize) {
        this.goalSize = goalSize;
    }

    public int getBallSize() {
        return ballSize;
    }

    public void setBallSize(int ballSize) {
        this.ballSize = ballSize;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getXoffset() {
        return xoffset;
    }

    public void setXoffset(double xoffset) {
        this.xoffset = xoffset;
    }

    public double getYoffset() {
        return yoffset;
    }

    public void setYoffset(double yoffset) {
        this.yoffset = yoffset;
    }

    public double[][] getHeights() {
        return heights;
    }

    public void setHeights(double[][] heights) {
        this.heights = heights;
    }

    public double getMaximum() {
        return maximum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    public double getMinimum() {
        return minimum;
    }

    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    public Color[][] getColors() {
        return colors;
    }

    public void setColors(Color[][] colors) {
        this.colors = colors;
    }
}
