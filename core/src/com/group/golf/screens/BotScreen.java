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
import com.group.golf.ai.*;
import com.group.golf.math.Computable;
import com.group.golf.math.Function;
import com.group.golf.modes.GameMode;
import com.group.golf.modes.UndefinedBotMode;
import com.group.golf.modes.WallCreationMode;

/**
 * Created by lilly on 5/22/18.
 */

public class BotScreen implements Screen {
    final Golf game;

    Stage stage;

    private Course course;
    private Ball ball;

    TextButton genetic;
    TextButton random;
    TextButton martijn;
    TextButton dum;
    TextButton back;
    Music menuMusic;
    OrthographicCamera cam;
    Texture background;

        public BotScreen(final Golf game, Course course, Ball ball) {
            this.game = game;
            this.course = course;
            this.ball = ball;
            stage = new Stage();
            Gdx.input.setInputProcessor(stage);
            Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
            genetic = new TextButton(" GeneticBot", skin);
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
            //stage.addActor(martijn);
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
                private Course course;
                private Ball ball;

                public GeneticBotListener(final Golf game, Screen screen, Course course, Ball ball) {
                    this.game = game;
                    this.screen = screen;
                    this.course = course;
                    this.ball = ball;
                }

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    genetic.setTouchable(Touchable.disabled);
                    random.setTouchable(Touchable.disabled);
                    martijn.setTouchable(Touchable.disabled);
                    dum.setTouchable(Touchable.disabled);
                    back.setTouchable(Touchable.disabled);
                    Bot genBot = new GeneticBot(course, ball);
                    Bot[] bots = new Bot[]{genBot};
                    Ball[] balls = new Ball[]{this.ball};
                    GameMode gameMode = new UndefinedBotMode(this.game, bots, course, balls);
                    GameMode wallMode = new WallCreationMode(this.game, course, balls);
                    this.game.setScreen(new CourseScreen(this.game, this.course, gameMode, wallMode));
                    this.screen.dispose();
                }

            }
            genetic.addListener(new GeneticBotListener(game, this, this.course, this.ball));

            class RandomBotListener extends ChangeListener {
                final Golf game;
                private Screen screen;
                private Course course;
                private Ball ball;

                public RandomBotListener(final Golf game, Screen screen, Course course, Ball ball) {
                    this.game = game;
                    this.screen = screen;
                    this.course = course;
                    this.ball = ball;
                }

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    genetic.setTouchable(Touchable.disabled);
                    random.setTouchable(Touchable.disabled);
                    martijn.setTouchable(Touchable.disabled);
                    dum.setTouchable(Touchable.disabled);
                    back.setTouchable(Touchable.disabled);
                    Bot ranBot = new RandomBot(course, ball);
                    Bot[] bots = new Bot[]{ranBot};
                    Ball[] balls = new Ball[]{this.ball};
                    GameMode gameMode = new UndefinedBotMode(this.game, bots, course, balls);
                    GameMode wallMode = new WallCreationMode(this.game, course, balls);
                    this.game.setScreen(new CourseScreen(this.game, this.course, gameMode, wallMode));
                    this.screen.dispose();
                }

            }
            random.addListener(new RandomBotListener(game, this, this.course, this.ball));

            class MartijnListener extends ChangeListener {
                final Golf game;
                private Screen screen;
                private Course course;
                private Ball ball;

                public MartijnListener(final Golf game, Screen screen, Course course, Ball ball) {
                    this.game = game;
                    this.screen = screen;
                    this.course = course;
                    this.ball = ball;
                }

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    genetic.setTouchable(Touchable.disabled);
                    random.setTouchable(Touchable.disabled);
                    martijn.setTouchable(Touchable.disabled);
                    dum.setTouchable(Touchable.disabled);
                    back.setTouchable(Touchable.disabled);
                    Bot martBot = new botMartijn(course, ball);
                    Bot[] bots = new Bot[]{martBot};
                    Ball[] balls = new Ball[]{this.ball};
                    GameMode gameMode = new UndefinedBotMode(this.game, bots, course, balls);
                    GameMode wallMode = new WallCreationMode(this.game, course, balls);
                    this.game.setScreen(new CourseScreen(this.game, this.course, gameMode, wallMode));
                    this.screen.dispose();
                }

            }
            martijn.addListener(new MartijnListener(game, this, this.course, this.ball));
            class DumBotListener extends ChangeListener {
                final Golf game;
                private Screen screen;
                private Course course;
                private Ball ball;

                public DumBotListener(final Golf game, Screen screen, Course course, Ball ball) {
                    this.game = game;
                    this.screen = screen;
                    this.course = course;
                    this.ball = ball;
                }

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    genetic.setTouchable(Touchable.disabled);
                    random.setTouchable(Touchable.disabled);
                    martijn.setTouchable(Touchable.disabled);
                    dum.setTouchable(Touchable.disabled);
                    back.setTouchable(Touchable.disabled);
                    Bot dumBot = new DumBot(course, ball);
                    Bot[] bots = new Bot[]{dumBot};
                    Ball[] balls = new Ball[]{this.ball};
                    GameMode gameMode = new UndefinedBotMode(this.game, bots, course, balls);
                    GameMode wallMode = new WallCreationMode(this.game, course, balls);
                    this.game.setScreen(new CourseScreen(this.game, this.course, gameMode, wallMode));
                    this.screen.dispose();
                }

            }
            dum.addListener(new DumBotListener(game, this, this.course, this.ball));
            class BackListener extends ChangeListener {
                final Golf game;
                private Screen screen;

                public BackListener(final Golf game, Screen screen) {
                    this.game = game;
                    this.screen = screen;
                }

                @Override
                public void changed(ChangeEvent event, Actor actor) {


                    genetic.setTouchable(Touchable.disabled);
                    random.setTouchable(Touchable.disabled);
                    martijn.setTouchable(Touchable.disabled);
                    dum.setTouchable(Touchable.disabled);
                    back.setTouchable(Touchable.disabled);

                    this.game.setScreen(new CourseSelectorScreen(this.game));
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
