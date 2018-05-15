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
    Ball ball;
    Texture flag;
    OrthographicCamera cam;
    Music music;
    Sound hitSound;
    Sound loseSound;
    Sound winSound;

    private Physics engine;
    private Collision collision;

    // Reading moves stuff
    private List<String> moves;
    private int counter;

    // Ball stuff
    double[] lastStop;

    // Graphing things
    private int goalSize;
    private double scaleX;
    private double scaleY;
    private double xoffset;
    private double yoffset;
    private double[][] heights;
    private double maximum;
    private double minimum;
    private Color[][] colors;
    private double ballX;
    private double ballY;

    // Gaming stuff
    boolean touchFlag;
    private int firstX;
    private int firstY;
    private int lastX;
    private int lastY;

    // Bot
    private Bot bot;


    /**
     * Create a new course screen
     * @param game the Golf instance
     * @param course the Course instance
     * @param ball the Ball instance
     * @param moves the moves to read
     */
    public CourseScreen(final Golf game, Course course, Ball ball, String moves) {
        this.game = game;
        this.course = course;

        // Setup Ball
        this.ball = ball;
        this.ball.setCourseScreen(this);
        this.ball.setTexture(new Texture(Gdx.files.internal("ball_soccer2.png")));
        this.ball.setX(this.course.getStart()[0]);
        this.ball.setY(this.course.getStart()[1]);

        this.setupCommon();

        // Setup moves
        this.counter = 0;
        this.moves = new ArrayList<String>();
        Scanner in = new Scanner(moves);
        while (in.hasNextLine()) {
            this.moves.add(in.nextLine());
        }
        in.close();
    }

    /**
     * Create a new CourseScreen instance
     * @param game the Golf instace
     * @param course the Course instance
     * @param ball the Ball instance
     */
    public CourseScreen(final Golf game, Course course, Ball ball) {
        this.game = game;
        this.course = course;

        // Setup Ball
        this.ball = ball;
        this.ball.setCourseScreen(this);
        this.ball.setTexture(new Texture(Gdx.files.internal("ball_soccer2.png")));
        this.ball.setX(this.course.getStart()[0]);
        this.ball.setY(this.course.getStart()[1]);

        this.setupCommon();

        // Setup moves
        this.moves = null;
        // Setup bot
        this.bot = null;
    }

    public CourseScreen(final Golf game, Course course, Ball ball, Bot bot) {
        this.game = game;
        this.course = course;

        // Setup Ball
        this.ball = ball;
        this.ball.setCourseScreen(this);
        this.ball.setTexture(new Texture(Gdx.files.internal("ball_soccer2.png")));
        this.ball.setX(this.course.getStart()[0]);
        this.ball.setY(this.course.getStart()[1]);

        this.setupCommon();

        this.bot = bot;
        this.moves = null;

        this.bot.setPhysics(this.engine);
        this.bot.setCollision(this.collision);
    }

    /**
     * Setup common properties to all gamemodes
     */
    private void setupCommon() {

        // Setup lastStop
        this.lastStop = MathLib.copyDoubleArr(this.course.getStart());

        // Setup sounds
        this.hitSound = Gdx.audio.newSound(Gdx.files.internal("golf_hit_1.wav"));
        this.loseSound = Gdx.audio.newSound(Gdx.files.internal("defeat_2.wav"));
        this.winSound = Gdx.audio.newSound(Gdx.files.internal("success_2.wav"));

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

        // Setup engine and collision system
        this.engine = new Physics(this.course, this.ball);
        this.collision = new Collision(this.ball, this.course);
    }

    /**
     * Compute the pixel coordinates of the ball
     */
    private void computeBallPixels() {
        double[] ballPixels = MathLib.toPixel(new double[]{this.ball.getX(), this.ball.getY()},
                new double[]{this.xoffset, this.yoffset}, new double[]{this.scaleX, this.scaleY});
        this.ballX = ballPixels[0];
        this.ballY = ballPixels[1];
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
     * Compute the scale
     */
    private void calcScale() {
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
    private void calcOffsets() {
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
     * Compute the heights matrix
     */
    private void calcHeightsMatrix() {
        this.heights = new double[Golf.VIRTUAL_WIDTH][Golf.VIRTUAL_HEIGHT];
        this.maximum = Double.MIN_VALUE;
        this.minimum = Double.MAX_VALUE;
        for (int x = 0; x < this.heights.length; x++) {
            for (int y = 0; y < this.heights[x].length; y++) {
                double value = this.course.getHeight(this.xoffset + x*this.scaleX,
                        this.yoffset + y*this.scaleY);
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
        this.xoffset = points[0][0].getX();
        this.yoffset = points[0][0].getY();

        // Setup scales
        int xLength = this.course.getFunctions().length;
        int yLength = this.course.getFunctions()[0].length;

        // scaleX
        BicubicInterpolator botRightInterp = (BicubicInterpolator) this.course.getFunctions()[xLength - 1][0];
        Point3D[][] botRightPoints = botRightInterp.getPoints();
        double rightX = botRightPoints[1][0].getX();
        this.scaleX = (rightX - this.xoffset) / Golf.VIRTUAL_WIDTH;

        // scaleY
        BicubicInterpolator topLeftInterp = (BicubicInterpolator) this.course.getFunctions()[0][yLength - 1];
        Point3D[][] topLeftPoints = topLeftInterp.getPoints();
        double topY = botRightPoints[0][1].getY();
        this.scaleY = (topY - this.yoffset) / Golf.VIRTUAL_HEIGHT;
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

        // Check the walls
        this.collision.checkForWalls(this.ballX, this.ballY);

        // Update pixel position of ball
        this.computeBallPixels();

        // Check if the ball is stopped
        if (!this.ball.isMoving()) {
            // Check if the goal is achieved
            if (this.collision.isGoalAchieved()) {
                this.winSound.play();
                try { Thread.sleep(3000); }
                catch (Exception e) {}
                this.game.setScreen(new CourseSelectorScreen(this.game));
                this.dispose();
                return;
            }

            // Store position
            this.lastStop[0] = this.ball.getX();
            this.lastStop[1] = this.ball.getY();

            // Make a move
            if (this.bot != null && Gdx.input.isKeyPressed(Input.Keys.SPACE)) this.bot.makeMove();
            else if (this.moves != null && this.counter < this.moves.size()) { // Mode 2 is active
                this.fileMoves();
            }
            else { // Mode 1 is active
                this.userMoves();
            }
        } else {
            this.engine.movement(delta);
        }
        this.ball.limit(this.course.getVmax());

        // Recompute pixel positions of the ball
        this.computeBallPixels();

        // Check for water
        if (this.collision.ballInWater()) {
            this.ball.reset();
            this.ball.setX(this.lastStop[0]);
            this.ball.setY(this.lastStop[1]);
            this.loseSound.play(0.2f);
        }

        // And again, before every check recompute pixel position of the ball
        this.computeBallPixels();

        // Render the ball
        this.ball.render();
    }

    /**
     * Perfom a file move
     */
    private void fileMoves() {
        StringTokenizer tokenizer = new StringTokenizer(this.moves.get(this.counter));
        double force = Double.parseDouble(tokenizer.nextToken());
        double angle = Double.parseDouble(tokenizer.nextToken());
        double forceX = force * Math.cos(angle);
        double forceY = force * Math.sin(angle);
        this.engine.hit(forceX, forceY);
        this.counter++;
        this.hitSound.play();
    }

    /**
     * Look for user moves
     */
    private void userMoves() {
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
                this.cam.unproject(firstV);
                Vector3 secondV = new Vector3(this.lastX, this.lastY,0);
                this.cam.unproject(secondV);

                System.out.println("firstX=" + this.firstX + ", firstY=" + this.firstY);
                System.out.println("lastX=" + this.lastX + ", lastY=" + this.lastY);

                double forceX = Math.abs(lastX - firstX) * 20;
                double forceY = Math.abs(lastY - firstY) * 20;

                if (lastX < firstX )
                    forceX *= -1;
                if (lastY > firstY)
                    forceY *= -1;

                double modulus = Math.sqrt(Math.pow((lastX - firstX), 2) + Math.pow((lastY - firstY), 2));
                double force = MathLib.map(modulus, 0, 300, 0, 600);

                this.engine.hit(forceX, forceY);

                this.hitSound.play();
            }
            this.touchFlag = false;
        }
    }

    /**
     * Render the goal
     */
    private void renderGoal() {
        this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        double[] real = MathLib.toPixel(this.course.getGoal(), new double[]{this.xoffset, this.yoffset},
                new double[]{this.scaleX, this.scaleY});
        float realX = (float) real[0];
        float realY = (float) real[1];
        this.game.shapeRenderer.setColor(0, 0, 0, 1);
        this.game.shapeRenderer.ellipse(realX - this.goalSize/2, realY - this.goalSize/2,
                this.goalSize, this.goalSize);
        this.game.shapeRenderer.end();
        this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        double tolerance = this.course.getTolerance();
        float toleranceX = (float) (tolerance * 1/(this.scaleX));
        float toleranceY = (float) (tolerance * 1/(this.scaleY));
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
        this.hitSound.dispose();
        this.loseSound.dispose();
        this.winSound.dispose();
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

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
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
