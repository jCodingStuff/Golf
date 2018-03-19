package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.group.golf.Golf;

import javax.swing.*;

public class IntroScreen implements Screen {

    final Golf game;

    OrthographicCamera cam;
    Music introMusic;
    Texture introImg;

    public IntroScreen(final Golf game) {
        this.game = game;

        this.cam = new OrthographicCamera();
        cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        this.introMusic = Gdx.audio.newMusic(Gdx.files.internal("mariokartds_titlescreen.mp3"));
        this.introMusic.setVolume(0.2f);
        this.introMusic.setLooping(true);

        this.introImg = new Texture(Gdx.files.internal("introImg.png"));
    }

    @Override
    public void show() {
        this.introMusic.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.196f, 0.804f, 0.196f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.cam.update();
        this.game.batch.setProjectionMatrix(this.cam.combined);

        this.game.batch.begin();
        this.game.batch.draw(this.introImg, 0, 0, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);
        this.game.font.draw(this.game.batch, "Crazy Golf", 10, 50);
        this.game.font.draw(this.game.batch, "Click anywhere to continue", 10, 25);
        this.game.batch.end();

        if (Gdx.input.isTouched()) {
            // this.dispose();
        }
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
        this.introMusic.stop();
    }

    @Override
    public void dispose() {
        this.introMusic.dispose();
        this.introImg.dispose();
    }
}
