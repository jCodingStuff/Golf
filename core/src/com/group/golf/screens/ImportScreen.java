package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.group.golf.Golf;


/**
 * Created by kim on 20.03.2018.
 */

class ImportScreen implements Screen {
    private Stage stage;
    TextField txtf;
    final Golf game;
    Music music;


    public ImportScreen(final Golf game){
        this.game = game;
        this.stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        txtf = new TextField("Name of the course:", skin);
        stage.addActor(txtf);
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
