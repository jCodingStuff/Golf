package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.regex.PatternSyntaxException;

import javax.swing.plaf.ColorUIResource;

/**
 * A class to draw the course
 */
public class CourseScreen implements Screen {

    final Golf game;

    Course course;
    Ball ball;
    Ball ball2; // Multiplayer
    Texture flag;
    OrthographicCamera cam;
    Music music;
    Sound hitSound;
    Sound loseSound;
    Sound winSound;
    
    Texture wall;// Added by you
    private int wallCount; // Added by you
    
    

    private Physics engine;
    private Collision collision;
    private Physics engine2; // MP
    private Collision collision2; // MP
    private boolean multiplayerActive = false; // MP 

    // Reading moves stuff
    private List<String> moves;
    private int counter;

    // Graphing things
    private int goalSize;
    private double[][] heights;
    private double maximum;
    private double minimum;
    private Color[][] colors;

    // Gaming stuff
    private boolean touchFlag;
    private boolean touchFlag2; // MP
    private boolean user1turn = true; // MP
    private int firstX;
    private int firstY;
    private int lastX;
    private int lastY;

    private double scaleX;
    private double scaleY;
    private static final double SCALE_MULTIPLIER = 75;
    private double xoffset;
    private double yoffset;
    private double ballX;
    private double ballY;
    private double ballX2; // MP
    private double ballY2; // MP

    // Bot
    private Bot bot;
    private boolean landed = true;
    private boolean landed2 = true;

    //Score
    int movesCounter;
    FreeTypeFontGenerator subTitleGenerator;
    FreeTypeFontGenerator.FreeTypeFontParameter subTitleParameter;
    BitmapFont subTitleFont;


    /**
     * Create a new course screen
     *
     * @param game   the Golf instance
     * @param course the Course instance
     * @param ball   the Ball instance
     * @param moves  the moves to read
     */
    public CourseScreen(final Golf game, Course course, Ball ball, String moves) {
        this.game = game;
        this.course = course;
        this.ball = ball;
        this.ball2 = null;
        this.setupCommon();

        // Setup moves
        this.counter = 0;
        this.moves = new ArrayList<String>();
        Scanner in = new Scanner(moves);
        while (in.hasNextLine()) {
            this.moves.add(in.nextLine());
        }
        in.close();

        this.bot = null;
        this.movesCounter = 0;


    }
    
    // For multiplayer
    public CourseScreen(final Golf game, Course course, Ball ball, Ball ball2) {
        multiplayerActive = true;
    	
    	this.game = game;
        this.course = course;
        this.ball = ball;
        this.ball2 = ball2;

        this.setupCommon();

        // Setup moves
        this.moves = null;
        // Setup bot
        this.bot = null;
        this.movesCounter = 0;

        this.subTitleGenerator = new FreeTypeFontGenerator(Gdx.files.internal("MoonGetFont.otf"));
        this.subTitleParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        this.subTitleParameter.size = 10;
        this.subTitleParameter.shadowColor = Color.BLACK;
        this.subTitleParameter.shadowOffsetX = 3;
        this.subTitleParameter.shadowOffsetY = 3;
        this.subTitleFont = this.subTitleGenerator.generateFont(this.subTitleParameter);
    }

    /**
     * Create a new CourseScreen instance
     *
     * @param game   the Golf instace
     * @param course the Course instance
     * @param ball   the Ball instance
     */
    public CourseScreen(final Golf game, Course course, Ball ball) {
        this.game = game;
        this.course = course;
        this.ball = ball;
        this.ball2 = null;
        this.setupCommon();

        // Setup moves
        this.moves = null;
        // Setup bot
        this.bot = null;
        this.movesCounter = 0;

        this.subTitleGenerator = new FreeTypeFontGenerator(Gdx.files.internal("MoonGetFont.otf"));
        this.subTitleParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        this.subTitleParameter.size = 10;
        this.subTitleParameter.shadowColor = Color.BLACK;
        this.subTitleParameter.shadowOffsetX = 3;
        this.subTitleParameter.shadowOffsetY = 3;
        this.subTitleFont = this.subTitleGenerator.generateFont(this.subTitleParameter);
    }

    public CourseScreen(final Golf game, Course course, Ball ball, Bot bot) {
        this.game = game;
        this.course = course;
        this.ball = ball;
        this.ball2 = null;
        this.setupCommon();

        this.bot = bot;
        this.moves = null;

        this.bot.setPhysics(this.engine);
        this.bot.setCollision(this.collision);
        this.movesCounter = 0;
        this.subTitleGenerator = new FreeTypeFontGenerator(Gdx.files.internal("MoonGetFont.otf"));
        this.subTitleParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        this.subTitleParameter.size = 10;
        this.subTitleParameter.shadowColor = Color.BLACK;
        this.subTitleParameter.shadowOffsetX = 3;
        this.subTitleParameter.shadowOffsetY = 3;
        this.subTitleFont = this.subTitleGenerator.generateFont(this.subTitleParameter);
    }

    /**
     * Setup common properties to all gamemodes
     */
    private void setupCommon() {
        // Setup Ball
        this.ball.setTexture(new Texture(Gdx.files.internal("ball_soccer2.png")));
        this.ball.setX(this.course.getStart()[0]);
        this.ball.setY(this.course.getStart()[1]);
        
        // Setup Ball2
        if(ball2 != null) {
        this.ball2.setTexture(new Texture(Gdx.files.internal("ball_soccer2.png")));
        this.ball2.setX(this.course.getStart2()[0]);
        this.ball2.setY(this.course.getStart2()[1]);
        }

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
        this.engine.setOffsets(new double[]{this.xoffset, this.yoffset});
        this.engine.setScales(new double[]{this.scaleX, this.scaleY});
        
        this.engine2 = new Physics(this.course, this.ball2);
        this.collision2 = new Collision(this.ball2, this.course);
        this.engine2.setOffsets(new double[]{this.xoffset, this.yoffset});
        this.engine2.setScales(new double[]{this.scaleX, this.scaleY});
        
     // Setup wall(s), in this case the wall count is set to 0 as this is the normal game-mode without walls.
        setWallCount(0);
        for (int i = 0; i < getWallCount(); i++) {
        	System.out.println("Wallcount:" + i);
        wall = new Texture(Gdx.files.internal("woodwall4.png"));
        }

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
                double value = this.course.getHeight(this.getXoffset() + x * this.getScaleX(),
                        this.getYoffset() + y * this.getScaleY());
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
                } else { // Grass
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
        if (this.ball2 != null) {
        this.renderGoal2();
        }
        
        // Render the maze walls
        this.renderMaze();

        // Update pixel position of ball
        this.computeBallPixels();
        if (this.ball2 != null) {
        this.computeBallPixels2();
        }


        this.game.batch.begin();
        //score
        this.subTitleFont.draw(this.game.batch, "moves: " + movesCounter, 100, 100);
        this.game.batch.end();


        // Check if the ball is stopped
        if (this.ball.getSize() == 0) {
            // If landed print
            if (this.landed) {
                System.out.println("Ball landed: " + this.ball.getX() + " " + this.ball.getY());
                this.landed = false;
            }

            // Check if the goal is achieved
            if (this.collision.isGoalAchieved()) {
                this.winSound.play();

                try {
                    FileHandle f = Gdx.files.local("scores.txt");

                    String line = f.readString();
                    String[] values = line.split("\\s+");



//                    String[] s = line.split(" ");
//                   for(String aScore: s){
//                       if(movesCounter< Integer.parseInt(aScore)){
//                           aScore = String.valueOf(movesCounter);
//                       }
//                   }

//                    String str = String.valueOf(movesCounter);


                    for (int i = 0; i < values.length ; i ++) {
                        System.out.println("For loop");
                        if (Integer.parseInt(values[i]) > movesCounter) {
                            System.out.println("If statement");
                            String[] spare = new String[values.length-i];
                            System.arraycopy(values,i,spare,0,spare.length);
                            values[i] = Integer.toString(movesCounter);
                            String[] updatedValues = new String[values.length + 1];
                            for (int j = 0; j < i+1; j++) {
                                updatedValues[j] = values[j];
                            }
                            for (int j = i+1; j < updatedValues.length; j++) {
                                updatedValues[j] = spare[j-i];
                            }


                            for (int j = 0; j < updatedValues.length; j++)
                                System.out.print(updatedValues[j] + "   ");
//                            System.out.println(Arrays.toString(updatedValues));
                            break;
                        }

                        if (i == values.length-1) {
                            String[] updatedValues = new String[values.length + 1];
                            for (int j = 0; j < values.length; j++) {
                                updatedValues[j] = values[j];
                            }
                            updatedValues[values.length] = Integer.toString(movesCounter);
                        }
                    }

                    BufferedWriter writer = new BufferedWriter(new FileWriter("scores.txt", true));
                    writer.append(' ');
                    writer.append(Integer.toString(movesCounter));

                    writer.close();
                }
                catch(Exception e)
                {}

                try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                    }
                    this.game.setScreen(new CourseSelectorScreen(this.game));
                    this.dispose();
                    return;
                }


                // Make a move
                if (this.bot != null && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    System.out.println("\nBot moving!");
                    this.bot.makeMove();
                    this.movesCounter++;
                    this.landed = true;
                } else if (this.moves != null && this.counter < this.moves.size() &&
                        Gdx.input.isKeyPressed(Input.Keys.SPACE)) { // Mode 2 is active
                    System.out.println("\nFile moves!");
                    this.fileMoves();
                    this.movesCounter++;
                } else { // Mode 1 is active
                    this.userMoves();

                }
            } else {

                this.ball.dequeue();
            }

            this.computeBallPixels();

            // Check for water
            if (this.engine.isWater() && this.ball.getSize() == 0) {
                this.ball.clear();
                this.ball.setX(this.engine.getHitCoord()[0]);
                this.ball.setY(this.engine.getHitCoord()[1]);
                this.loseSound.play(0.2f);
            }

            // Compute pixel position of the ball
            this.computeBallPixels();

            // Render the ball
            this.ball.render(this.game.batch, this.ballX, this.ballY);
            
         // Check if the ball2 is stopped
            if (multiplayerActive == true)  {
            if (this.ball2.getSize() == 0) {
                // If landed print
                if (this.landed2) {
                    System.out.println("Ball landed: " + this.ball2.getX() + " " + this.ball2.getY());
                    this.landed2 = false;
                }

                // Check if the goal is achieved
                if (this.collision2.isGoalAchieved2()) {
                    this.winSound.play();

                    try {
                        FileHandle f = Gdx.files.local("scores.txt");

                        String line = f.readString();
                        String[] values = line.split("\\s+");



//                        String[] s = line.split(" ");
//                       for(String aScore: s){
//                           if(movesCounter< Integer.parseInt(aScore)){
//                               aScore = String.valueOf(movesCounter);
//                           }
//                       }

//                        String str = String.valueOf(movesCounter);


                        for (int i = 0; i < values.length ; i ++) {
                            System.out.println("For loop");
                            if (Integer.parseInt(values[i]) > movesCounter) {
                                System.out.println("If statement");
                                String[] spare = new String[values.length-i];
                                System.arraycopy(values,i,spare,0,spare.length);
                                values[i] = Integer.toString(movesCounter);
                                String[] updatedValues = new String[values.length + 1];
                                for (int j = 0; j < i+1; j++) {
                                    updatedValues[j] = values[j];
                                }
                                for (int j = i+1; j < updatedValues.length; j++) {
                                    updatedValues[j] = spare[j-i];
                                }


                                for (int j = 0; j < updatedValues.length; j++)
                                    System.out.print(updatedValues[j] + "   ");
//                                System.out.println(Arrays.toString(updatedValues));
                                break;
                            }

                            if (i == values.length-1) {
                                String[] updatedValues = new String[values.length + 1];
                                for (int j = 0; j < values.length; j++) {
                                    updatedValues[j] = values[j];
                                }
                                updatedValues[values.length] = Integer.toString(movesCounter);
                            }
                        }

                        BufferedWriter writer = new BufferedWriter(new FileWriter("scores.txt", true));
                        writer.append(' ');
                        writer.append(Integer.toString(movesCounter));

                        writer.close();
                    }
                    catch(Exception e)
                    {}

                    try {
                            Thread.sleep(3000);
                        } catch (Exception e) {
                        }
                        this.game.setScreen(new CourseSelectorScreen(this.game));
                        this.dispose();
                        return;
                    }


                    // Make a move
                    if (this.bot != null && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                        System.out.println("\nBot moving!");
                        this.bot.makeMove();
                        this.movesCounter++;
                        this.landed = true;
                    } else if (this.moves != null && this.counter < this.moves.size() &&
                            Gdx.input.isKeyPressed(Input.Keys.SPACE)) { // Mode 2 is active
                        System.out.println("\nFile moves!");
                        this.fileMoves();
                        this.movesCounter++;
                    } else { // Mode 1 is active
                        this.userMoves();

                    }
                } else {

                    this.ball2.dequeue();
                }

                this.computeBallPixels2();

                // Check for water
                if (this.engine2.isWater() && this.ball2.getSize() == 0) {
                    this.ball2.clear();
                    this.ball2.setX(this.engine2.getHitCoord()[0]);
                    this.ball2.setY(this.engine2.getHitCoord()[1]);
                    this.loseSound.play(0.2f);
                }

                // Compute pixel position of the ball
                this.computeBallPixels2();

                // Render the ball
                this.ball2.render(this.game.batch, this.ballX2, this.ballY2);
            }

    }


        /**
         * Perfom a file move
         */
        private void fileMoves () {
            StringTokenizer tokenizer = new StringTokenizer(this.moves.get(this.counter));
            double force = Double.parseDouble(tokenizer.nextToken());
            double angle = Double.parseDouble(tokenizer.nextToken());
            double forceX = force * Math.cos(angle);
            double forceY = force * Math.sin(angle);
            this.engine.hit(forceX, forceY);
            this.landed = true;
            this.counter++;
            this.hitSound.play();
        }

        /**
         * Look for user moves
         */

        private void userMoves () {

            if (Gdx.input.isTouched()) {
                if (!this.touchFlag) {
                    this.firstX = Gdx.input.getX();
                    this.firstY = Gdx.input.getY();
                    this.touchFlag = true;
                    movesCounter++;
                }
                this.lastX = Gdx.input.getX();
                this.lastY = Gdx.input.getY();

            } else if (this.touchFlag) {
                if (this.firstX != this.lastX || this.lastY != this.firstY) {
                    Vector3 firstV = new Vector3(this.firstX, this.firstY, 0);
                    this.cam.unproject(firstV);
                    Vector3 secondV = new Vector3(this.lastX, this.lastY, 0);
                    this.cam.unproject(secondV);


                    double xLength = Math.abs(lastX - firstX);
                    double yLength = Math.abs(lastY - firstY);

                    if (lastX < firstX)
                        xLength *= -1;
                    if (lastY > firstY)
                        yLength *= -1;

                    double modulus = Math.sqrt(Math.pow((lastX - firstX), 2) + Math.pow((lastY - firstY), 2));
                    // we don't need this !!
                    double force = MathLib.map(modulus, 0, 300, 0, 600);

                    xLength *= this.scaleX * SCALE_MULTIPLIER;
                    yLength *= this.scaleY * SCALE_MULTIPLIER;
                    
                    if (this.multiplayerActive == true) {
                    if (this.user1turn == true) {
                    this.engine.hit(xLength, yLength);
                    this.landed = true;

                    this.hitSound.play();
                    user1turn = false;
                    }
                    else {
                    	this.engine2.hit(xLength, yLength);
                        this.landed2 = true;

                        this.hitSound.play();
                        user1turn = true;
                    }
                    }
                    else {
                    	this.engine.hit(xLength, yLength);
                        this.landed = true;

                        this.hitSound.play();
                    }
                }
                this.touchFlag = false;
            }
        }


        /**
         * Compute the pixel coordinates of the ball
         */
        public void computeBallPixels () {
            double[] ballPixels = MathLib.toPixel(new double[]{this.ball.getX(), this.ball.getY()},
                    new double[]{this.xoffset, this.yoffset}, new double[]{this.scaleX, this.scaleY});
            this.ballX = ballPixels[0];
            this.ballY = ballPixels[1];
        }
        
        public void computeBallPixels2 () {
            double[] ballPixels2 = MathLib.toPixel(new double[]{this.ball2.getX(), this.ball2.getY()},
                    new double[]{this.xoffset, this.yoffset}, new double[]{this.scaleX, this.scaleY});
            this.ballX2 = ballPixels2[0];
            this.ballY2 = ballPixels2[1];
        }

        /**
         * Compute the scale
         */
        public void calcScale () {
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
        public void calcOffsets () {
            double x1 = this.course.getStart()[0];
            double x2 = this.course.getGoal()[0];
            double xUnits = Golf.VIRTUAL_WIDTH / (1 / this.scaleX);
            this.xoffset = (x1 + x2 - xUnits) / 2.0;
            double y1 = this.course.getStart()[1];
            double y2 = this.course.getGoal()[1];
            double yUnits = Golf.VIRTUAL_HEIGHT / (1 / this.scaleY);
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
    
    private void renderGoal2() {
        this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        double[] real = MathLib.toPixel(this.course.getGoal2(), new double[]{this.getXoffset(), this.getYoffset()},
                new double[]{this.getScaleX(), this.getScaleY()});
        float realX = (float) real[0];
        float realY = (float) real[1];
        this.game.shapeRenderer.setColor(0, 0, 0, 1);
        this.game.shapeRenderer.ellipse(realX - this.goalSize/2, realY - this.goalSize/2,
                this.goalSize, this.goalSize);
        this.game.shapeRenderer.end();
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
     * Render the maze and create a rectangle around each maze wall for collision with the ball.
     */
    private void renderMaze() {
    	Rectangle[] walls= new Rectangle[wallCount];
    	double[] real = MathLib.toPixel(this.course.getGoal(), new double[]{this.getXoffset(), this.getYoffset()},
                new double[]{this.getScaleX(), this.getScaleY()});
    	float realX = (float) real[0];
        float realY = (float) real[1];
        this.game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.game.shapeRenderer.setColor(Color.GRAY);
        this.game.batch.begin();
        for (int i = 0; i < wallCount; i++) {
        	float x = (float) 0.0;
            float y = (float) 60;
            float width = (float) 122;
            float height = (float) 22;
            walls[i] = new Rectangle(realX + x,realY + y, width, height);
           // this.game.shapeRenderer.rect(realX + x, realY + y, width, height);
        	this.game.batch.draw(this.wall, realX + x, realY + y, width, height);
        }
        engine.setWalls(walls);
        if (multiplayerActive == true) {
        	engine2.setWalls(walls);
        }
        this.game.batch.end();
        this.game.shapeRenderer.end();
    }

         /**
         * Render the terrain (course)
         */
        private void renderTerrain () {
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
        public void resize ( int width, int height){

        }

        @Override
        public void pause () {

        }

        @Override
        public void resume () {

        }

        @Override
        public void hide () {
            this.music.stop();
        }

        @Override
        public void dispose () {
            this.music.dispose();
            this.flag.dispose();
            this.hitSound.dispose();
            this.loseSound.dispose();
            this.winSound.dispose();
        }

        public Golf getGame () {
            return game;
        }

        public Course getCourse () {
            return course;
        }

        public void setCourse (Course course){
            this.course = course;
        }

        public Ball getBall () {
            return ball;
        }

        public void setBall (Ball ball){
            this.ball = ball;
        }

        public Texture getFlag () {
            return flag;
        }

        public void setFlag (Texture flag){
            this.flag = flag;
        }

        public OrthographicCamera getCam () {
            return cam;
        }

        public void setCam (OrthographicCamera cam){
            this.cam = cam;
        }

        public Music getMusic () {
            return music;
        }

        public void setMusic (Music music){
            this.music = music;
        }

        public int getGoalSize () {
            return goalSize;
        }

        public void setGoalSize ( int goalSize){
            this.goalSize = goalSize;
        }


        public double[][] getHeights () {
            return heights;
        }

        public void setHeights ( double[][] heights){
            this.heights = heights;
        }

        public double getMaximum () {
            return maximum;
        }

        public void setMaximum ( double maximum){
            this.maximum = maximum;
        }

        public double getMinimum () {
            return minimum;
        }

        public void setMinimum ( double minimum){
            this.minimum = minimum;
        }

        public Color[][] getColors () {
            return colors;
        }

        public void setColors (Color[][]colors){
            this.colors = colors;
        }

        public Physics getEngine () {
            return engine;
        }

        public double getScaleX () {
            return scaleX;
        }

        public void setScaleX ( double scaleX){
            this.scaleX = scaleX;
        }

        public double getScaleY () {
            return scaleY;
        }

        public void setScaleY ( double scaleY){
            this.scaleY = scaleY;
        }

        public double getXoffset () {
            return xoffset;
        }

        public void setXoffset ( double xoffset){
            this.xoffset = xoffset;
        }

        public double getYoffset () {
            return yoffset;
        }

        public void setYoffset ( double yoffset){
            this.yoffset = yoffset;
        }

        public double getBallX () {
            return ballX;
        }

        public void setBallX ( double ballX){
            this.ballX = ballX;
        }


        public double getBallY () {
            return ballY;
        }

        public void setBallY ( double ballY){
            this.ballY = ballY;
        }

        public int getWallCount() {
        	return wallCount;
        }

        public void setWallCount(int number) {
    	wallCount = number;
    }
        
        public boolean getuser1Turn() {
        	return user1turn;
        }
        
       


}
