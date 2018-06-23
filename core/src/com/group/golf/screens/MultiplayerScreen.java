package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
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
        mode2 = new TextButton("Mode 2", skin);
        mode1 = new TextButton("Mode 1", skin);

        back.setPosition(100, 300);
        mode3.setPosition(400, 200);
        mode2.setPosition(400, 300);
        mode1.setPosition(400, 400);

        back.setSize(100, 60);
        mode3.setSize(200, 60);
        mode2.setSize(200, 60);
        mode1.setSize(200, 60);


        stage.addActor(back);
        stage.addActor(mode3);
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
                float[] start = new float[]{-3, 5};
                float[] goal = new float[]{0, 2};

                float[] start2 = new float[]{-3, 4};
                float[] goal2 = new float[]{0, 5};

                Function function = new Function(formula);
                Computable[][] functions = new Computable[1][1];
                functions[0][0] = function;
                Course course = new Course(functions, 9.81f, 0.95f, 15f, start, start2, goal, goal2, 0.5f);
                course.addWall(new Rectangle(491,221,69,280));
                course.addWall(new Rectangle(550,317,444,76));
                course.addWall(new Rectangle(365,6,60,442));

                mode3.setTouchable(Touchable.disabled);
                mode1.setTouchable(Touchable.disabled);
                mode2.setTouchable(Touchable.disabled);
                back.setTouchable(Touchable.disabled);

                Ball ball = new Ball(40);
                Ball ball2 = new Ball(40);
                Ball[] balls = new Ball[]{ball, ball2};

                GameMode gameMode = new TwoGoalsMode(this.game, course, balls, 2.5);
                GameMode wallMode = new WallCreationMode(this.game, course, balls);
                this.game.setScreen(new EngineSelectorScreen(this.game, course, gameMode, wallMode));


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
                String formula = " x + 10 + x ^ 2 * 0.4";
                float[] start = new float[]{-0.3f, 2};
                float[] goal = new float[]{5, 5};

                float[] start2 = new float[]{-0.3f, 3};
                float[] goal2 = new float[]{2, 4};

                Function function = new Function(formula);
                Computable[][] functions = new Computable[1][1];
                functions[0][0] = function;
                Course course = new Course(functions, 4.11f, 0.95f, 15f, start, start2, goal, goal2, 0.5f);
                course.addWall(new Rectangle(144,-6,54,364));
                course.addWall(new Rectangle(6,581,33,112));
                course.addWall(new Rectangle(146,607,48,60));
                course.addWall(new Rectangle(244,651,36,42));
                course.addWall(new Rectangle(71,658,39,39));
                course.addWall(new Rectangle(328,601,49,67));
                course.addWall(new Rectangle(411,658,37,35));
                course.addWall(new Rectangle(280,206,304,38));
                course.addWall(new Rectangle(508,237,74,435));
                course.addWall(new Rectangle(280,39,44,165));
                course.addWall(new Rectangle(370,0,49,180));
                course.addWall(new Rectangle(587,206,93,49));
                course.addWall(new Rectangle(575,209,31,47));
                course.addWall(new Rectangle(681,453,42,92));
                course.addWall(new Rectangle(79,562,426,52));
                course.addWall(new Rectangle(79,408,49,153));
                course.addWall(new Rectangle(22,412,62,34));
                course.addWall(new Rectangle(476,64,52,140));
                course.addWall(new Rectangle(624,319,36,86));
                course.addWall(new Rectangle( 625,557,35,73));
                mode3.setTouchable(Touchable.disabled);
                mode1.setTouchable(Touchable.disabled);
                mode2.setTouchable(Touchable.disabled);
                back.setTouchable(Touchable.disabled);

                Ball ball = new Ball(40);
                Ball ball2 = new Ball(40);
                Ball[] balls = new Ball[]{ball, ball2};

                GameMode gameMode = new TwoGoalsMode(this.game, course, balls, 2.5);
                GameMode wallMode = new WallCreationMode(this.game, course, balls);
                this.game.setScreen(new EngineSelectorScreen(this.game, course, gameMode, wallMode));

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
                float[] start = new float[]{-2, 3};
                float[] goal = new float[]{0, 1};

                float[] start2 = new float[]{-1, 3};
                float[] goal2 = new float[]{0, 3};

                Function function = new Function(formula);
                Computable[][] functions = new Computable[1][1];
                functions[0][0] = function;
                Course course = new Course(functions, 9.81f, 0.95f, 15f, start, start2, goal, goal2, 0.5f);
                course.addWall(new Rectangle(523, 384, 49, 246));
                course.addWall(new Rectangle(220, 616, 294, 35));
                course.addWall(new Rectangle(222, 414, 56, 202));
                course.addWall(new Rectangle(284, 419, 67, 49));
                course.addWall(new Rectangle(354, 421, 35, 87));
                course.addWall(new Rectangle(418, 417, 32, 94));
                course.addWall(new Rectangle( 421, 387, 101, 31));
                course.addWall(new Rectangle(521, 264, 57, 123));
                course.addWall(new Rectangle(324, 261, 199, 38));
                course.addWall(new Rectangle(337,301, 35, 32));
                course.addWall(new Rectangle(284, 215, 40, 101));
                course.addWall(new Rectangle(220, 370, 39, 48));
                course.addWall(new Rectangle(167, 286, 49, 97));
                course.addWall(new Rectangle(167, 197, 49, 87));
                course.addWall(new Rectangle(164, 138, 176, 55));
                course.addWall(new Rectangle(338, 146, 202, 41));
                course.addWall(new Rectangle(537, 134, 78, 49));
                course.addWall(new Rectangle(611, 133, 101, 45));
                course.addWall(new Rectangle(707, 130, 134, 49));
                course.addWall(new Rectangle(722, 181, 103, 159));
                course.addWall(new Rectangle( 574, 272, 70, 99));
                course.addWall(new Rectangle(644, 273, 33, 114));
                course.addWall(new Rectangle( 726, 343, 98, 203));
                course.addWall(new Rectangle(606, 425, 60, 62));
                course.addWall(new Rectangle(564, 562, 122, 71));
                course.addWall(new Rectangle(683, 548, 136, 89));
                mode3.setTouchable(Touchable.disabled);
                mode1.setTouchable(Touchable.disabled);
                mode2.setTouchable(Touchable.disabled);
                back.setTouchable(Touchable.disabled);

                Ball ball = new Ball(40);
                Ball ball2 = new Ball(40);
                Ball[] balls = new Ball[]{ball, ball2};

                GameMode gameMode = new TwoGoalsMode(this.game, course, balls, 2.5);
                GameMode wallMode = new WallCreationMode(this.game, course, balls);
                this.game.setScreen(new EngineSelectorScreen(this.game, course, gameMode, wallMode));

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
