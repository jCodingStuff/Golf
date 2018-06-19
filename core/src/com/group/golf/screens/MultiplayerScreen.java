package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.ai.Bot;
import com.group.golf.ai.DumBot;
import com.group.golf.math.Computable;
import com.group.golf.math.Function;
import com.group.golf.modes.GameMode;
import com.group.golf.modes.PlayerVSBotMode;
import com.group.golf.modes.TwoGoalsMode;
import com.group.golf.modes.UndefinedPlayerMode;
import com.group.golf.modes.WallCreationMode;

public class MultiplayerScreen implements Screen{
    private final Golf game;
    private Stage stage;

//    private final Course course;
//    private final Ball ball;

    private TextButton mode3;
    private TextButton mode4;
    private TextButton mode2;
    private TextButton mode1;

    private TextButton back;
    private Music menuMusic;
    private OrthographicCamera cam;
    private Texture background;

    public MultiplayerScreen(final Golf game) {
        this.game = game;
//        this.course = course;
//        this.ball = ball;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        back = new TextButton("Back", skin);
        mode3 = new TextButton("Mode 3", skin);
        mode4 = new TextButton("Mode 4", skin);
        mode2 = new TextButton("Mode 2", skin);
        mode1 = new TextButton("Mode 1", skin);

        back.setPosition(100, 300);
        mode3.setPosition(300, 300);
        mode2.setPosition(600, 400);
        mode1.setPosition(300, 400);

        back.setSize(100, 60);
        mode3.setSize(200, 60);
        mode2.setSize(200, 60);
        mode1.setSize(200, 60);


        stage.addActor(back);
        stage.addActor(mode3);
        stage.addActor(mode4);
        stage.addActor(mode2);
        stage.addActor(mode1);
        

        // Setup button listeners
        class BackListener extends ChangeListener {
            final Golf game;
            private Screen screen;

            public BackListener(final Golf game, Screen screen) {
                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mode3.setTouchable(Touchable.disabled);
                mode1.setTouchable(Touchable.disabled);
                mode2.setTouchable(Touchable.disabled);

                mode1.setTouchable(Touchable.disabled);
                back.setTouchable(Touchable.disabled);
                this.game.setScreen(new CourseSelectorScreen(this.game));
                this.screen.dispose();
            }
        }
       back.addListener(new BackListener(game, this));

        class Mode1Listener extends ChangeListener {
            final Golf game;
            private Screen screen;
            public Mode1Listener(final Golf game, Screen screen){
                this.game = game;
                this.screen = screen;
            }
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                String formula = "0.1 * x + 0.3 * x ^ 2 + 0.2 * y";
                double[] start = new double[]{-2, 3};
                double[] goal = new double[]{0, 1};

                double[] start2 = new double[]{-1, 3};
                double[] goal2 = new double[]{0, 2};

                Function function = new Function(formula);
                Computable[][] functions = new Computable[1][1];
                functions[0][0] = function;
                Course course = new Course(functions, 9.81, 0.95, 80, start, start2, goal, goal2, 0.5);
                mode3.setTouchable(Touchable.disabled);
                mode1.setTouchable(Touchable.disabled);
                mode2.setTouchable(Touchable.disabled);

             
                back.setTouchable(Touchable.disabled);

                Ball ball = new Ball(40);
                Ball ball2 = new Ball(40);
                Ball[] balls = new Ball[]{ball, ball2};

                GameMode gameMode = new TwoGoalsMode(this.game, course, balls);
                GameMode wallMode = new WallCreationMode(this.game, course, balls);
                this.game.setScreen(new CourseScreen(this.game, course, gameMode, wallMode));

                this.screen.dispose();
            }
        }
        mode1.addListener(new Mode1Listener(this.game, this));

        class Mode2Listener extends ChangeListener {
            final Golf game;
            private Screen screen;

            public Mode2Listener(final Golf game, Screen screen) {
                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String formula = "0.1 * x + 0.3 * x ^ 2 + 0.2 * y";
                double[] start = new double[]{-2, 3};
                double[] goal = new double[]{0, 1};

                double[] start2 = new double[]{-1, 3};
                double[] goal2 = new double[]{0, 2};

                Function function = new Function(formula);
                Computable[][] functions = new Computable[1][1];
                functions[0][0] = function;
                Course course = new Course(functions, 9.81, 0.95, 80, start, start2, goal, goal2, 0.5);
                mode3.setTouchable(Touchable.disabled);
                mode1.setTouchable(Touchable.disabled);
                mode2.setTouchable(Touchable.disabled);

                mode1.setTouchable(Touchable.disabled);
                back.setTouchable(Touchable.disabled);

                Ball ball = new Ball(40);
                Ball ball2 = new Ball(40);
                Ball[] balls = new Ball[]{ball, ball2};

                GameMode gameMode = new TwoGoalsMode(this.game, course, balls);
                GameMode wallMode = new WallCreationMode(this.game, course, balls);
                this.game.setScreen(new CourseScreen(this.game, course, gameMode, wallMode));

                this.screen.dispose();
            }
        }
        mode2.addListener(new Mode2Listener(game, this));

        class Mode3Listener extends ChangeListener {
            final Golf game;
            private Screen screen;
            public Mode3Listener(final Golf game, Screen screen){
                this.game = game;
                this.screen = screen;
            }
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                String formula = "0.1 * x + 0.3 * x ^ 2 + 0.2 * y";
                double[] start = new double[]{-2, 3};
                double[] goal = new double[]{0, 1};

                double[] start2 = new double[]{-1, 3};
                double[] goal2 = new double[]{0, 2};

                Function function = new Function(formula);
                Computable[][] functions = new Computable[1][1];
                functions[0][0] = function;
                Course course = new Course(functions, 9.81, 0.95, 80, start, start2, goal, goal2, 0.5);
                mode3.setTouchable(Touchable.disabled);
                mode1.setTouchable(Touchable.disabled);
                mode2.setTouchable(Touchable.disabled);

                mode1.setTouchable(Touchable.disabled);
                back.setTouchable(Touchable.disabled);

                Ball ball = new Ball(40);
                Ball ball2 = new Ball(40);
                Ball[] balls = new Ball[]{ball, ball2};

                GameMode gameMode = new TwoGoalsMode(this.game, course, balls);
                GameMode wallMode = new WallCreationMode(this.game, course, balls);
                this.game.setScreen(new CourseScreen(this.game, course, gameMode, wallMode));

                this.screen.dispose();
            }
        }
      mode3.addListener(new Mode3Listener(this.game, this));



        // Setup cam
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        // Setup music
        this.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("mariokart8_mainmenu.mp3"));
        this.menuMusic.setVolume(0.2f);
        this.menuMusic.setLooping(true);

        // Setup background image
        this.background = new Texture(Gdx.files.internal("minigolf_background.jpg"));
    }

    @Override
    public void show() {
        this.menuMusic.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.196f, 0.804f, 0.196f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.cam.update();
        this.game.batch.setProjectionMatrix(this.cam.combined);

        this.game.batch.begin();
        this.game.batch.draw(this.background, 0, 0, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);
        this.game.batch.end();

        stage.act(delta);
        stage.draw();
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
        this.menuMusic.stop();
    }

    @Override
    public void dispose() {
        this.menuMusic.dispose();
        this.background.dispose();
    }

}
