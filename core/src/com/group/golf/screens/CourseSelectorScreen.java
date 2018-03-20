package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.math.Function;

public class CourseSelectorScreen implements Screen {

    final Golf game;
    Stage stage;

    TextButton play;
    TextButton importbtn;
    TextButton design;
    Music menuMusic;
    OrthographicCamera cam;

    public CourseSelectorScreen(final Golf game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        play = new TextButton("play", skin);
        importbtn = new TextButton("import", skin);
        design = new TextButton("design", skin);
        play.setPosition(300, 400);
        importbtn.setPosition(300, 300);
        design.setPosition(300, 200);
        play.setSize(200, 60);
        importbtn.setSize(200, 60);
        design.setSize(200, 60);

        stage.addActor(play);
        stage.addActor(importbtn);
        stage.addActor(design);

        // Setup cam
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        // Setup music
        this.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("mariokart8_mainmenu.mp3"));
        this.menuMusic.setVolume(0.2f);
        this.menuMusic.setLooping(true);

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
                double[] start = new double[]{0, 0};
                double[] goal = new double[]{4, 3};
                Function function = new Function(formula);
                Course course = new Course(function, 9.81, 0.5, 3, start, goal, 0.02);
                this.game.setScreen(new CourseScreen(this.game, course));
                this.screen.dispose();
            }

        }

        play.addListener(new PlayListener(game, this));
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
        //this.game.font.draw(this.game.batch, "MAIN MENU", 400, 300);
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
    }
}
