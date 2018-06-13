package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.group.golf.Golf;
import com.group.golf.listeners.Mode1Listener;
import com.group.golf.listeners.Mode2Listener;
import com.group.golf.listeners.ToExportListener;

/**
 * Created by kim on 13.04.2018.
 */

public class DecisionScreen implements Screen {
    final Golf game;

    Stage stage;
    OrthographicCamera cam;
    Music music;

    TextButton spline;
    TextButton splineWithout;
    TextButton function;
    TextButton btnBack;
    Texture background;

    /**
     * Create a new Design Screen
     *
     * @param game the Golf instace
     */
    public DecisionScreen(final Golf game) {

        this.game = game;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Set up music
        this.music = Gdx.audio.newMusic(Gdx.files.internal("mariokart8_rainbowroad.mp3"));
        this.music.setVolume(0.2f);
        this.music.setLooping(true);

        // Setup background image
        this.background = new Texture(Gdx.files.internal("minigolf_background.jpg"));

        // Setup cam
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        // initializing buttons / textfields
        spline = new TextButton("Spline with derivatives", skin);
        splineWithout = new TextButton("Spline without derivatives",skin);
        function = new TextButton("Function", skin);
        btnBack = new TextButton("Back", skin);

        //spline.addListener(new SplineListener(this.game, this));
        //function.addListener(new FunctionListener(this.game, this));
        //btnBack.addListener(new BackListener(this.game, this));

        spline.setPosition(400, 400);
        spline.setSize(200, 60);

        splineWithout.setPosition(400, 300);
        splineWithout.setSize(200, 60);

        function.setPosition(400, 200);
        function.setSize(200, 60);

        btnBack.setPosition(100, 300);
        btnBack.setSize(100, 60);

        stage.addActor(spline);
        stage.addActor(splineWithout);
        stage.addActor(function);
        stage.addActor(btnBack);

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
        btnBack.addListener(new BackListener(game, this));

        // listener for spline button
        class SplineListener extends ChangeListener {
            final Golf game;
            private Screen screen;

            public SplineListener(final Golf game, Screen screen) {
                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                this.game.setScreen(new SplineScreen(this.game));
                this.screen.dispose();
            }

        }
        spline.addListener(new SplineListener(game, this));

        class SplineWithoutListener extends ChangeListener {
            final Golf game;
            private Screen screen;

            public SplineWithoutListener(final Golf game, Screen screen) {
                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {

              this.game.setScreen(new SplineWithoutScreen(this.game));
                this.screen.dispose();
            }

        }
        splineWithout.addListener(new SplineWithoutListener(game, this));

        // listener for function button
        class FunctionListener extends ChangeListener {
            final Golf game;
            private Screen screen;

            public FunctionListener(final Golf game, Screen screen) {
                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                this.game.setScreen(new DesignScreen(this.game));
                this.screen.dispose();
            }

        }
        function.addListener(new FunctionListener(game, this));
    }
        @Override
        public void show() {
            this.music.play();
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
            this.music.stop();
        }
        @Override
        public void dispose() {
            this.music.dispose();
            this.background.dispose();
        }
}
