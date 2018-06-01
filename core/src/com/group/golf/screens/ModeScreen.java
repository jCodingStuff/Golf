package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;

public class ModeScreen implements Screen {

    private final Golf game;
    private Stage stage;

    private Course course;
    private Ball ball;

    private TextButton singlePlayer;
    private TextButton playerVSPlayer;
    private TextButton ai;
    private TextButton playerVSai;
    private TextButton aiVSai;
    private TextButton back;
    private Music menuMusic;
    private OrthographicCamera cam;
    private Texture background;

    public ModeScreen(final Golf game, Course course, Ball ball) {
        this.game = game;
        this.course = course;
        this.ball = ball;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        back = new TextButton("Back", skin);

        back.setPosition(100, 300);

        back.setSize(100, 60);

        stage.addActor(back);
        stage.addActor(singlePlayer);
        stage.addActor(playerVSPlayer);
        stage.addActor(ai);
        stage.addActor(playerVSai);
        stage.addActor(aiVSai);

        // Setup cam
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        // Setup music
        this.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("mariokart8_mainmenu.mp3"));
        this.menuMusic.setVolume(0.2f);
        this.menuMusic.setLooping(true);

        // Setup background image
        this.background = new Texture(Gdx.files.internal("minigolf_background.jpg"));
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
