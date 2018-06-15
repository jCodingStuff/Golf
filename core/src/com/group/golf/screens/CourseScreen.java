package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.ai.Bot;
import com.group.golf.math.BicubicInterpolator;
import com.group.golf.math.MathLib;
import com.group.golf.math.Point3D;
import com.group.golf.modes.GameMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * A class to draw the course
 */
public class CourseScreen implements Screen {

    final Golf game;

    Course course;
    Texture flag;
    OrthographicCamera cam;
    Music music;

    // Gamemode
    private GameMode gameMode;

    // Graphing things
    private int goalSize;
    private double[][] heights;
    private double maximum;
    private double minimum;
    private Color[][] colors;

    private double scaleX;
    private double scaleY;
    public static final double SCALE_MULTIPLIER = 75;
    private double xoffset;
    private double yoffset;

    public CourseScreen(final Golf game, Course course, GameMode gameMode) {
        this.game = game;
        this.course = course;
        this.gameMode = gameMode;

        this.setupCommon();

        this.gameMode.setOffsets(new double[]{this.xoffset, this.yoffset});
        this.gameMode.setScales(new double[]{this.scaleX, this.scaleY});
    }

    /**
     * Setup common properties to all gamemodes
     */
    private void setupCommon() {

        // Setup Cam
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        // Setup Music
        this.music = Gdx.audio.newMusic(Gdx.files.internal("mario64_ost.mp3"));
        this.music.setVolume(0.2f);
        this.music.setLooping(true);

        // Setup Course
        this.setUpCourse();

        // Setup Goal
        this.goalSize = 20;
        this.flag = new Texture(Gdx.files.internal("golf_flag.png"));

    }

    /**
     * Set up the course for rendering
     */
    private void setUpCourse() {
        if (!this.course.isSpline()) { // Do normal setup
            this.calcScale();
            this.calcOffsets();
        } else { // Do spline setup
            this.splineSetup();
        }

        // Setup the heights matrix
        this.calcHeightsMatrix();
        // Setup the colors matrix
        this.calcColorsMatrix();
    }


    /**
     * Compute the heights matrix
     */
    private void calcHeightsMatrix() {
        this.heights = new double[Golf.VIRTUAL_WIDTH][Golf.VIRTUAL_HEIGHT];
        this.maximum = Double.MIN_VALUE;
        this.minimum = Double.MAX_VALUE;
        for (int x = 0; x < this.heights.length; x++) {
            for (int y = 0; y < this.heights[x].length; y++) {
                double value = this.course.getHeight(this.getXoffset() + x*this.getScaleX(),
                        this.getYoffset() + y*this.getScaleY());
                if (value > this.maximum) this.maximum = value;
                else if (value < this.minimum) this.minimum = value;
                this.heights[x][y] = value;
            }
        }
    }

    /**
     * Compute the colors matrix
     */
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

    /**
     * Do setup for splines
     */
    private void splineSetup() {
        // Setup Offsets
        BicubicInterpolator botLeftInterp = (BicubicInterpolator) this.course.getFunctions()[0][0];
        Point3D[][] points = botLeftInterp.getPoints();
        this.setXoffset(points[0][0].getX());
        this.setYoffset(points[0][0].getY());

        // Setup scales
        int xLength = this.course.getFunctions().length;
        int yLength = this.course.getFunctions()[0].length;

        // scaleX
        BicubicInterpolator botRightInterp = (BicubicInterpolator) this.course.getFunctions()[xLength - 1][0];
        Point3D[][] botRightPoints = botRightInterp.getPoints();
        double rightX = botRightPoints[1][0].getX();
        this.setScaleX((rightX - this.getXoffset()) / Golf.VIRTUAL_WIDTH);

        // scaleY
        BicubicInterpolator topLeftInterp = (BicubicInterpolator) this.course.getFunctions()[0][yLength - 1];
        Point3D[][] topLeftPoints = topLeftInterp.getPoints();
        double topY = botRightPoints[0][1].getY();
        this.setScaleY((topY - this.getYoffset()) / Golf.VIRTUAL_HEIGHT);
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

        boolean moved = this.gameMode.move(this.cam);
        if (!moved) this.dispose();

        this.gameMode.water();

        this.gameMode.render(this.game.batch);
    }

    /**
     * Compute the scale
     */
    public void calcScale() {
        double dist = this.course.getDistance();
        double limitDist = 0.40625;
        this.scaleX = 0.000625;
        while (dist > limitDist) {
            this.scaleX *= 2;
            limitDist *= 2;
        }
        this.scaleY = scaleX;
    }

    /**
     * Compute the screen offsets
     */
    public void calcOffsets() {
        double x1 = this.course.getStart()[0];
        double x2 = this.course.getGoal()[0];
        double xUnits = Golf.VIRTUAL_WIDTH / (1/this.scaleX);
        this.xoffset = (x1 + x2 - xUnits) / 2.0;
        double y1 = this.course.getStart()[1];
        double y2 = this.course.getGoal()[1];
        double yUnits = Golf.VIRTUAL_HEIGHT / (1/this.scaleY);
        this.yoffset = (y1 + y2 - yUnits) / 2.0;
    }

    /**
     * Render the goal
     */
    private void renderGoal() {
        this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        double[] real = MathLib.toPixel(this.course.getGoal(), new double[]{this.getXoffset(), this.getYoffset()},
                new double[]{this.getScaleX(), this.getScaleY()});
        float realX = (float) real[0];
        float realY = (float) real[1];
        this.game.shapeRenderer.setColor(0, 0, 0, 1);
        this.game.shapeRenderer.ellipse(realX - this.goalSize/2, realY - this.goalSize/2,
                this.goalSize, this.goalSize);
        this.game.shapeRenderer.end();
        this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        double tolerance = this.course.getTolerance();
        float toleranceX = (float) (tolerance * 1/(this.getScaleX()));
        float toleranceY = (float) (tolerance * 1/(this.getScaleY()));
        this.game.shapeRenderer.setColor(1, 0, 0, 1);
        this.game.shapeRenderer.ellipse(realX - toleranceX, realY - toleranceY,
                toleranceX*2, toleranceY*2);
        this.game.shapeRenderer.end();
        this.game.batch.begin();
        this.game.batch.draw(this.flag, realX - 3, realY, 52, 62);
        this.game.batch.end();
    }

    /**
     * Render the terrain (course)
     */
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
        this.flag.dispose();
        this.gameMode.dispose();
    }

    public Golf getGame() {
        return game;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
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
}