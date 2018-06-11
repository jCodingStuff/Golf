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
import com.group.golf.ai.GeneticBot;
import com.group.golf.ai.RandomBot;

import com.group.golf.ai.Bot;
import com.group.golf.ai.DumBot;

import com.group.golf.math.Computable;
import com.group.golf.math.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * The main menu screen for the Crazy Golf game
 */
public class CourseSelectorScreen implements Screen {

    final Golf game;
    Stage stage;
    Physics engine;
    Ball ball;

    TextButton play;
    TextButton importbtn;
    TextButton design;
    TextButton maze;

    Music menuMusic;
    OrthographicCamera cam;
    Texture background;

    /**
     * Create a new main menu screen
     * @param game the Golf instance
     */
    public CourseSelectorScreen(final Golf game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        play = new TextButton("Play", skin);
        importbtn = new TextButton("Import", skin);
        design = new TextButton("Design", skin);
        maze = new TextButton("Maze", skin);

        play.setPosition(300, 400);
        importbtn.setPosition(300, 300);
        design.setPosition(600, 300);
        maze.setPosition(600,400);
        play.setSize(200, 60);
        importbtn.setSize(200, 60);
        design.setSize(200, 60);
        maze.setSize(200,60);

        stage.addActor(play);
        stage.addActor(importbtn);
        stage.addActor(design);
        stage.addActor(maze);

        // Setup cam
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        // Setup music
        this.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("mariokart8_mainmenu.mp3"));
        this.menuMusic.setVolume(0.2f);
        this.menuMusic.setLooping(true);

        // Setup background image
        this.background = new Texture(Gdx.files.internal("minigolf_background.jpg"));

        class PlayListener extends ChangeListener{
            final Golf game;
            private Screen screen;
            public PlayListener(final Golf game, Screen screen){
                this.game = game;
                this.screen = screen;
            }
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                String formula = "0.1 * x + 0.3 * x ^ 2 + 0.2 * y";
                double[] start = new double[]{4, 3};
                double[] goal = new double[]{0, 1};
                Function function = new Function(formula);
                Computable[][] functions = new Computable[1][1];
                functions[0][0] = function;
                Course course = new Course(functions, 9.81, 0.95, 80, start, goal, 0.5);
                design.setTouchable(Touchable.disabled);
                importbtn.setTouchable(Touchable.disabled);
                play.setTouchable(Touchable.disabled);

                Ball ball = new Ball(40);

                this.game.setScreen(new ModeScreen(this.game, course, ball));

                this.screen.dispose();
            }

        }
        play.addListener(new PlayListener(game, this));

        class ImportListener extends ChangeListener {
            final Golf game;
            private Screen screen;

            public ImportListener(final Golf game, Screen screen) {
                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                this.game.setScreen(new ImportScreen(this.game));
                this.screen.dispose();
            }
        }
        importbtn.addListener(new ImportListener(this.game, this));

        class DesignListener extends ChangeListener {
            final Golf game;
            private Screen screen;
            public DesignListener(final Golf game, Screen screen){
                this.game = game;
                this.screen = screen;
            }
            public void changed (ChangeEvent event, Actor actor) {
                this.game.setScreen(new DecisionScreen(this.game));
                this.screen.dispose();
            }
        }
        design.addListener(new DesignListener(game, this));

      
        class MazeListener extends ChangeListener {
            final Golf game;
            private Screen screen;
            public MazeListener(final Golf game, Screen screen){
                this.game = game;
                this.screen = screen;
            }
            public void changed (ChangeEvent event, Actor actor) {
                this.game.setScreen(new MazeScreen(this.game));
                this.screen.dispose();
            }
        }
        maze.addListener(new MazeListener(game, this));


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
