package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.group.golf.Golf;
import com.group.golf.listeners.ImportBackListener;


/**
 * Created by kim on 20.03.2018.
 */

class ImportScreen implements Screen {
    private Stage stage;
    TextField txtf;
    final Golf game;
    Music music;
    TextButton back;
    Label label;
    OrthographicCamera cam;
    TextButton importButton;


    public ImportScreen(final Golf game){
        this.game = game;
        this.stage = new Stage();

        // Setup cam
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        txtf = new TextField("", skin);
        txtf.setPosition(200, 400);
        this.back = new TextButton("Back", skin);
        this.back.setPosition(200, 200);
        this.back.addListener(new ImportBackListener(this.game, this));

        stage.addActor(txtf);
        stage.addActor(back);

        this.importButton = new TextButton("Import", skin);
        this.importButton.setPosition(270, 200);
        stage.addActor(this.importButton);

        this.label = new Label("Course Name", skin);
        this.label.setPosition(200, 500);
        stage.addActor(this.label);

        // Set the stage as InputProcessor
        Gdx.input.setInputProcessor(this.stage);

        // Set up music
        this.music = Gdx.audio.newMusic(Gdx.files.internal("mariokart8_rainbowroad.mp3"));
        this.music.setVolume(0.2f);
        this.music.setLooping(true);
    }
    @Override
    public void show() {
        this.music.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.5f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.cam.update();

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
    }
}
