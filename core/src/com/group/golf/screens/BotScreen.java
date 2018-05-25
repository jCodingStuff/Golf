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
import com.group.golf.Physics.Physics;
import com.group.golf.ai.Bot;
import com.group.golf.ai.GeneticBot;
import com.group.golf.ai.RandomBot;
import com.group.golf.math.Computable;
import com.group.golf.math.Function;

/**
 * Created by lilly on 5/22/18.
 */

public class BotScreen implements Screen {
    final Golf game;
    Stage stage;
    Physics engine;
    Ball ball;

    TextButton genetic;
    TextButton random;
    TextButton martijn;
    TextButton dum;
    TextButton back;
    Music menuMusic;
    OrthographicCamera cam;
    Texture background;

        public BotScreen(final Golf game) {
            this.game = game;
            stage = new Stage();
            Gdx.input.setInputProcessor(stage);
            Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
            genetic = new TextButton("Genetic Bot", skin);
            random = new TextButton("Random Bot", skin);
            martijn = new TextButton("Bot Martijn", skin);
            dum = new TextButton("Dumb Bot", skin);
            back = new TextButton("Back", skin);

            genetic.setPosition(300, 400);
            random.setPosition(300, 300);
            martijn.setPosition(600, 400);
            dum.setPosition(600, 300);
            back.setPosition(100, 300);

            genetic.setSize(200, 60);
            random.setSize(200, 60);
            martijn.setSize(200, 60);
            dum.setSize(200, 60);
            back.setSize(100, 60);

            stage.addActor(genetic);
            stage.addActor(random);
            stage.addActor(martijn);
            stage.addActor(dum);
            stage.addActor(back);

            // Setup cam
            this.cam = new OrthographicCamera();
            this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

            // Setup music
            this.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("mariokart8_mainmenu.mp3"));
            this.menuMusic.setVolume(0.2f);
            this.menuMusic.setLooping(true);

            // Setup background image
            this.background = new Texture(Gdx.files.internal("minigolf_background.jpg"));


            class GeneticBotListener extends ChangeListener {
                final Golf game;
                private Screen screen;

                public GeneticBotListener(final Golf game, Screen screen) {
                    this.game = game;
                    this.screen = screen;
                }

                @Override
                public void changed(ChangeEvent event, Actor actor) {

                    String formula = "0.1 * x + 0.3 * x ^ 2 + 0.2 * y";
                    double[] start = new double[]{4, 3};
                    double[] goal = new double[]{0, 1};
                    Function function = new Function(formula);
                    Computable[][] functions = new Computable[1][1];
                    functions[0][0] = function;
                    Course course = new Course(functions, 9.81, 0.95, 80, start, goal, 0.5);
                    genetic.setTouchable(Touchable.disabled);
                    random.setTouchable(Touchable.disabled);
                    martijn.setTouchable(Touchable.disabled);
                    dum.setTouchable(Touchable.disabled);
                    back.setTouchable(Touchable.disabled);
                    Bot genBot = new GeneticBot(course, ball);
                    this.game.setScreen(new CourseScreen(this.game, course, new Ball(40), genBot));
                    this.screen.dispose();
                }

            }
            genetic.addListener(new GeneticBotListener(game, this));

            class MartijnListener extends ChangeListener {
                final Golf game;
                private Screen screen;

                public MartijnListener(final Golf game, Screen screen) {
                    this.game = game;
                    this.screen = screen;
                }

                @Override
                public void changed(ChangeEvent event, Actor actor) {

                    String formula = "0.1 * x + 0.3 * x ^ 2 + 0.2 * y";
                    double[] start = new double[]{4, 3};
                    double[] goal = new double[]{0, 1};
                    Function function = new Function(formula);
                    Computable[][] functions = new Computable[1][1];
                    functions[0][0] = function;
                    Course course = new Course(functions, 9.81, 0.95, 80, start, goal, 0.5);
                    genetic.setTouchable(Touchable.disabled);
                    random.setTouchable(Touchable.disabled);
                    martijn.setTouchable(Touchable.disabled);
                    dum.setTouchable(Touchable.disabled);
                    back.setTouchable(Touchable.disabled);
                    Bot genBot = new GeneticBot(course, ball);
                    this.game.setScreen(new CourseScreen(this.game, course, new Ball(40), genBot));
                    this.screen.dispose();
                }

            }
            martijn.addListener(new MartijnListener(game, this));
            class DumBotListener extends ChangeListener {
                final Golf game;
                private Screen screen;

                public DumBotListener(final Golf game, Screen screen) {
                    this.game = game;
                    this.screen = screen;
                }

                @Override
                public void changed(ChangeEvent event, Actor actor) {

                    String formula = "0.1 * x + 0.3 * x ^ 2 + 0.2 * y";
                    double[] start = new double[]{4, 3};
                    double[] goal = new double[]{0, 1};
                    Function function = new Function(formula);
                    Computable[][] functions = new Computable[1][1];
                    functions[0][0] = function;
                    Course course = new Course(functions, 9.81, 0.95, 80, start, goal, 0.5);
                    genetic.setTouchable(Touchable.disabled);
                    random.setTouchable(Touchable.disabled);
                    martijn.setTouchable(Touchable.disabled);
                    dum.setTouchable(Touchable.disabled);
                    back.setTouchable(Touchable.disabled);
                    Bot ranBot = new RandomBot(course, engine, ball);
                    this.game.setScreen(new CourseScreen(this.game, course, new Ball(40), ranBot));
                    this.screen.dispose();
                }

            }
            dum.addListener(new DumBotListener(game, this));
            class BackListener extends ChangeListener {
                final Golf game;
                private Screen screen;

                public BackListener(final Golf game, Screen screen) {
                    this.game = game;
                    this.screen = screen;
                }

                @Override
                public void changed(ChangeEvent event, Actor actor) {

                    String formula = "0.1 * x + 0.3 * x ^ 2 + 0.2 * y";
                    double[] start = new double[]{4, 3};
                    double[] goal = new double[]{0, 1};
                    Function function = new Function(formula);
                    Computable[][] functions = new Computable[1][1];
                    functions[0][0] = function;
                    Course course = new Course(functions, 9.81, 0.95, 80, start, goal, 0.5);
                    genetic.setTouchable(Touchable.disabled);
                    random.setTouchable(Touchable.disabled);
                    martijn.setTouchable(Touchable.disabled);
                    dum.setTouchable(Touchable.disabled);
                    back.setTouchable(Touchable.disabled);
                    Bot ranBot = new RandomBot(course, engine, ball);
                    this.game.setScreen(new CourseScreen(this.game, course, new Ball(40), ranBot));
                    this.screen.dispose();
                }

            }
            back.addListener(new BackListener(game, this));

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
