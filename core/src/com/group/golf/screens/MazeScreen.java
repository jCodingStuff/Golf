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
import com.group.golf.math.Computable;
import com.group.golf.math.Function;

/**
 * Created by kim on 11.06.2018.
 */

public class MazeScreen implements Screen {

        final Golf game;
        Stage stage;
        Physics engine;
        Ball ball;

        TextButton playAlone;
        TextButton team;
        TextButton bot;
        TextButton back;

        Music menuMusic;
        OrthographicCamera cam;
        Texture background;

        /**
         * Create a new main menu screen
         * @param game the Golf instance
         */
        public MazeScreen(final Golf game) {
            this.game = game;
            stage = new Stage();
            Gdx.input.setInputProcessor(stage);
            Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
            playAlone = new TextButton("Play", skin);
            team = new TextButton("Team Play", skin);
            back = new TextButton("Back", skin);
            bot = new TextButton("Bot", skin);

            playAlone.setPosition(300, 400);
            team.setPosition(600, 400);
            back.setPosition(100, 300);
            playAlone.setSize(200, 60);
            team.setSize(200, 60);
            back.setSize(100, 60);
            bot.setPosition(600, 300);
            bot.setSize(200, 60);

            stage.addActor(playAlone);
            stage.addActor(team);
            stage.addActor(back);
            stage.addActor(bot);

            // Setup cam
            this.cam = new OrthographicCamera();
            this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

            // Setup music
            this.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("mariokart8_mainmenu.mp3"));
            this.menuMusic.setVolume(0.2f);
            this.menuMusic.setLooping(true);

            // Setup background image
            this.background = new Texture(Gdx.files.internal("minigolf_background.jpg"));

            class PlayListener extends ChangeListener {
                final Golf game;
                private Screen screen;

                public PlayListener(final Golf game, Screen screen) {
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
                    back.setTouchable(Touchable.disabled);
                    team.setTouchable(Touchable.disabled);
                    playAlone.setTouchable(Touchable.disabled);
                    bot.setTouchable(Touchable.disabled);

                    Ball ball = new Ball(40);

                    this.game.setScreen(new CourseMazeScreen(this.game, course, ball));

                    this.screen.dispose();
                }

            }
            playAlone.addListener(new PlayListener(game, this));

            class TeamListener extends ChangeListener {
                final Golf game;
                private Screen screen;

                public TeamListener(final Golf game, Screen screen) {
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
                    back.setTouchable(Touchable.disabled);
                    playAlone.setTouchable(Touchable.disabled);
                    bot.setTouchable(Touchable.disabled);

                    Ball ball = new Ball(40);

                    this.game.setScreen(new CourseScreen(this.game, course, ball));

                    this.screen.dispose();
                }

            }
            team.addListener(new TeamListener(game, this));


            class BotListener extends ChangeListener {
                final Golf game;
                private Screen screen;

                public BotListener(final Golf game, Screen screen) {
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
                    back.setTouchable(Touchable.disabled);
                    team.setTouchable(Touchable.disabled);
                    playAlone.setTouchable(Touchable.disabled);

                    Ball ball = new Ball(40);

                    this.game.setScreen(new BotScreen(this.game, course, ball));
                    this.screen.dispose();
                }

            }
            bot.addListener(new BotListener(game, this));


            // listener for back button
            class BackListener extends ChangeListener {
                final Golf game;
                private Screen screen;

                public BackListener(final Golf game, Screen screen) {
                    this.game = game;
                    this.screen = screen;
                }

                @Override
                public void changed(ChangeEvent event, Actor actor) {

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

