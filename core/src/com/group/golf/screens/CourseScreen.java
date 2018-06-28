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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
import org.w3c.dom.css.Rect;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * A screen class to render a course
 * @author Julian Marrades
 * @author Alexandros Chimonas
 * @author Martijn Hilders
 * @author Kaspar Kallast
 */
public class CourseScreen implements Screen {

    final Golf game;

    private static final Color WALL_COLOR = new Color(0.545f, 0.553f, 0.478f, 1);

    Course course;
    Texture flag;
    Texture flag2;
    private Texture wallTexture;
    private List<TextureRegion> wallsRegions;
    OrthographicCamera cam;
    Music music;

    // Gamemode
    private GameMode gameMode;
    private GameMode activeMode; // Initially WallCreationMode

    // Graphing things
    private int goalSize;
    private double[][] heights;
    private float maximum;
    private float minimum;
    private Color[][] colors;

    private float scaleX;
    private float scaleY;
    public static final double SCALE_MULTIPLIER = 75;
    private float xoffset;
    private float yoffset;

    private boolean started;

    /**
     * Create a new instance of CourseScreen
     * @param game the game instance
     * @param course the course to be rendered
     * @param gameMode the desired gamemode
     * @param wallMode the wall editor gamemode
     */
    public CourseScreen(final Golf game, Course course, GameMode gameMode, GameMode wallMode) {
        this.game = game;
        this.course = course;
        this.gameMode = gameMode;
        this.activeMode = wallMode;

        this.setupCommon();


        System.out.println("Offsets: [" + xoffset + ", " + yoffset + "]");
        System.out.println("Scales: [" + scaleX + ", " + scaleY + "]");
        this.activeMode.setOffsets(new float[]{this.xoffset, this.yoffset});
        this.activeMode.setScales(new float[]{this.scaleX, this.scaleY});
    }

    /**
     * Setup common properties to all gamemodes
     */
    private void setupCommon() {
        this.started = false;

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
        this.flag2 = new Texture(Gdx.files.internal("golf_flag2.png"));

        // Setup Wall texture
        this.wallTexture = new Texture(Gdx.files.internal("walltexture.jpg"));
        this.wallsRegions = new ArrayList<TextureRegion>();
        this.updateWallRegions();

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
        this.maximum = Float.MIN_VALUE;
        this.minimum = Float.MAX_VALUE;
        for (int x = 0; x < this.heights.length; x++) {
            for (int y = 0; y < this.heights[x].length; y++) {
                float value = this.course.getHeight(this.getXoffset() + x*this.getScaleX(),
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
                    float blue = maxWater + minWater - MathLib.map((float)Math.abs(this.heights[x][y]), 0,
                            (float)Math.abs(this.minimum), minWater, maxWater);
                    this.colors[x][y] = new Color(0, 0, blue, 1);
                }
                else { // Grass
                    // Higher, darker
                    float green = maxTerrain + minTerrain - (float) MathLib.map((float)this.heights[x][y], 0,
                            (float)this.maximum, minTerrain, maxTerrain);
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
        float rightX = botRightPoints[1][0].getX();
        this.setScaleX((rightX - this.getXoffset())/ Golf.VIRTUAL_WIDTH);

        // scaleY
        BicubicInterpolator topLeftInterp = (BicubicInterpolator) this.course.getFunctions()[0][yLength - 1];
        Point3D[][] topLeftPoints = topLeftInterp.getPoints();
        float topY = topLeftPoints[0][1].getY();
        this.setScaleY((topY - this.getYoffset()) / Golf.VIRTUAL_HEIGHT);

        this.course.setOffsets(new float[]{this.xoffset, this.yoffset});
        this.course.setScales(new float[]{this.scaleX, this.scaleY});
    }

    /**
     * Start playing background music
     */
    @Override
    public void show() {
        this.music.play();
    }

    /**
     * Render common elements to all gamemodes (terrain, goals, walls, etc.)
     * @param delta the time between last and current frame
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Look for quit
        this.checkQuit();

        // Look for start
        this.checkForStart();

        // Cam and projection matrices
        this.cam.update();
        this.game.batch.setProjectionMatrix(this.cam.combined);
        this.game.shapeRenderer.setProjectionMatrix(this.cam.combined);

        // Render the course
        this.renderTerrain();

        // Render walls
        this.updateWallRegions();
        this.checkWallPrint();
        this.checkWallDelete();
        this.renderWalls();

        // Render the goal
        this.renderGoal(this.course.getGoal(), this.flag);
        float[] goal2 = this.course.getGoal2();
        if (goal2 != null) this.renderGoal(goal2, this.flag2);

        boolean moved = this.activeMode.move(this.cam);
        if (!moved) this.dispose(); // Goal reached

//        this.activeMode.water();

        this.activeMode.extraChecks();

        this.activeMode.render(this.game.batch);
    }

    /**
     * Check if user wants to quit playing
     */
    private void checkQuit() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            this.game.setScreen(new CourseSelectorScreen(this.game));
            this.dispose();
        }
    }

    /**
     * Chack if user wants to display information about the course graphic walls
     */
    private void checkWallPrint() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            List<Rectangle> walls = this.course.getWalls();
            if (!walls.isEmpty()) {
                System.out.println("Current Walls: ");
                for (Rectangle wall: this.course.getWalls()) {
                    System.out.println(wall);
                }
            } else {
                System.out.println("There are no walls...");
            }
        }
    }

    /**
     * Check if user wants to delete a wall
     */
    private void checkWallDelete() {
        if (!this.started && Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            List<Rectangle> walls = this.course.getWalls();
            Vector2 mousePos = mousePosition(this.cam);
            boolean deleted = false;
            for (int i = walls.size() - 1; i >= 0 && !deleted; i--) {
                if (walls.get(i).contains(new Vector2(mousePos.x, mousePos.y))) {
                    Rectangle deletedWall = walls.get(i);
                    walls.remove(i);
                    this.wallsRegions.remove(i);
                    deleted = true;
                    System.out.println("Wall Removed: " + deletedWall);
                }
            }
        }
    }

    /**
     * Update the sprites for the walls
     */
    private void updateWallRegions() {
        List<Rectangle> walls = this.course.getWalls();
        int wallNum = walls.size();
        while (wallNum > this.wallsRegions.size()) {
            int rndX = (int) (Math.random() * 100);
            int rndY = (int) (Math.random() * 100);
            this.wallsRegions.add(new TextureRegion(this.wallTexture, rndX, rndY, (int) walls.get(wallNum - 1).width,
                    (int) walls.get(wallNum - 1).height));
        }
    }

    /**
     * Check is user wants to start (game phase / end wall editor)
     */
    private void checkForStart() {
        if (!this.started && Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.activeMode = this.gameMode; // Start real GameMode
            this.activeMode.setOffsets(new float[]{this.xoffset, this.yoffset});
            this.activeMode.setScales(new float[]{this.scaleX, this.scaleY});
            this.started = true;
            System.out.println("GAME STARTS NOW! FIGHT!");
        }
    }

    /**
     * Render the graphic walls
     */
    private void renderWalls() {
        this.game.batch.begin();
        List<Rectangle> walls = this.course.getWalls();
        for (int i = 0; i < walls.size(); i++) {
            Rectangle current = walls.get(i);
            this.game.batch.draw(this.wallsRegions.get(i), current.getX(), current.getY(), current.getWidth(),
                    current.getHeight());
        }
        this.game.batch.end();
    }

    /**
     * Compute the scale
     */
    public void calcScale() {
        double dist = this.course.getDistance();
        double limitDist = 0.40625;
        this.scaleX = 0.000625f;
        while (dist > limitDist) {
            this.scaleX *= 2;
            limitDist *= 2;
        }
        this.scaleY = scaleX;
        this.course.setScales(new float[]{this.scaleX, this.scaleY});
    }

    /**
     * Compute the screen offsets
     */
    public void calcOffsets() {
        float x1 = this.course.getStart()[0];
        float x2 = this.course.getGoal()[0];
        float xUnits = Golf.VIRTUAL_WIDTH / (1/this.scaleX);
        this.xoffset = (x1 + x2 - xUnits) / 2.0f;
        float y1 = this.course.getStart()[1];
        float y2 = this.course.getGoal()[1];
        float yUnits = Golf.VIRTUAL_HEIGHT / (1/this.scaleY);
        this.yoffset = (y1 + y2 - yUnits) / 2.0f;
        this.course.setOffsets(new float[]{this.xoffset, this.yoffset});
    }

    /**
     * Render the goal
     */
    private void renderGoal(float[] goal, Texture texture) {
        this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        float[] real = MathLib.toPixel(goal, new float[]{this.getXoffset(), this.getYoffset()},
                new float[]{this.getScaleX(), this.getScaleY()});
        float realX = real[0];
        float realY = real[1];
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
        this.game.batch.draw(texture, realX - 3, realY, 52, 62);
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

    /**
     * Stop playing music if hide
     */
    @Override
    public void hide() {
        this.music.stop();
    }

    /**
     * Dispose sounds and sprites
     */
    @Override
    public void dispose() {
        this.music.dispose();
        this.flag.dispose();
//        this.activeMode.dispose();
        this.gameMode.dispose();
    }

    /**
     * Get the mouse position of the user
     * @param cam the camera being used at the very moment
     * @return a Vector2 instance containing the (x, y) coordinates of the mouse tip
     */
    public static Vector2 mousePosition(OrthographicCamera cam) {
        Vector3 mouse3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(mouse3D);
        return new Vector2(mouse3D.x, mouse3D.y);
    }




    // GETTER AND SETTER AREA
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

    public float getMaximum() {
        return maximum;
    }

    public void setMaximum(float maximum) {
        this.maximum = maximum;
    }

    public float getMinimum() {
        return minimum;
    }

    public void setMinimum(float minimum) {
        this.minimum = minimum;
    }

    public Color[][] getColors() {
        return colors;
    }

    public void setColors(Color[][] colors) {
        this.colors = colors;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getXoffset() {
        return xoffset;
    }

    public void setXoffset(float xoffset) {
        this.xoffset = xoffset;
    }

    public float getYoffset() {
        return yoffset;
    }

    public void setYoffset(float yoffset) {
        this.yoffset = yoffset;
    }
}
