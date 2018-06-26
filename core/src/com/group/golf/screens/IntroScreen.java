package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.group.golf.Golf;

/**
 * The introductory screen for the game
 * @author Julian Marrades
 */
public class IntroScreen implements Screen {

    final Golf game;

    OrthographicCamera cam;
    Music introMusic;
    Texture introImg;

    FreeTypeFontGenerator titleGenerator;
    FreeTypeFontGenerator.FreeTypeFontParameter titleParameter;
    FreeTypeFontGenerator subTitleGenerator;
    FreeTypeFontGenerator.FreeTypeFontParameter subTitleParameter;
    BitmapFont titleFont;
    BitmapFont subTitleFont;

    /**
     * Create a new intro screen
     * @param game the Golf instance
     */
    public IntroScreen(final Golf game) {
        this.game = game;

        // Setup cam
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        // Setup music
        this.introMusic = Gdx.audio.newMusic(Gdx.files.internal("mariokartds_titlescreen.mp3"));
        this.introMusic.setVolume(0.2f);
        this.introMusic.setLooping(true);

        // Setup background image
        this.introImg = new Texture(Gdx.files.internal("introImg.png"));

        // Setup text
        this.titleGenerator = new FreeTypeFontGenerator(Gdx.files.internal("GolfToolsFont.ttf"));
        this.titleParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        this.titleParameter.size = 110;
        //this.titleParameter.borderWidth = 3;
        this.titleParameter.shadowColor = Color.BLACK;
        this.titleParameter.shadowOffsetX = 3;
        this.titleParameter.shadowOffsetY = 3;
        this.titleFont = this.titleGenerator.generateFont(this.titleParameter);

        this.subTitleGenerator = new FreeTypeFontGenerator(Gdx.files.internal("MoonGetFont.otf"));
        this.subTitleParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        this.subTitleParameter.size = 30;
        this.subTitleParameter.shadowColor = Color.BLACK;
        this.subTitleParameter.shadowOffsetX = 3;
        this.subTitleParameter.shadowOffsetY = 3;
        this.subTitleFont = this.subTitleGenerator.generateFont(this.subTitleParameter);
    }

    /**
     * Start playing background music
     */
    @Override
    public void show() {
        this.introMusic.play();
    }

    /**
     * Render UI
     * @param delta time between last and current frame
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.196f, 0.804f, 0.196f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.cam.update();
        this.game.batch.setProjectionMatrix(this.cam.combined);

        this.game.batch.begin();
        this.game.batch.draw(this.introImg, 0, 0, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);
        this.titleFont.draw(this.game.batch, "Crazy Golf", 75, 600);
        this.subTitleFont.draw(this.game.batch, "Click anywhere to continue", 230, 100);
        this.game.batch.end();

        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            this.game.setScreen(new CourseSelectorScreen(this.game));
            this.dispose();
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

    /**
     * Stop music if hide
     */
    @Override
    public void hide() {
        this.introMusic.stop();
    }

    /**
     * Dispose music, sprites and fonts
     */
    @Override
    public void dispose() {
        this.introMusic.dispose();
        this.introImg.dispose();
        this.titleGenerator.dispose();
        this.subTitleGenerator.dispose();
        this.titleFont.dispose();
        this.subTitleFont.dispose();
    }
}
