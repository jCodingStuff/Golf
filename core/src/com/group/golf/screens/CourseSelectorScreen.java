package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.math.Function;

public class CourseSelectorScreen implements Screen {

    final Golf game;

    Music menuMusic;
    OrthographicCamera cam;

    public CourseSelectorScreen(final Golf game) {
        this.game = game;

        // Setup cam
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        // Setup music
        this.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("mariokart8_mainmenu.mp3"));
        this.menuMusic.setVolume(0.2f);
        this.menuMusic.setLooping(true);
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
        this.game.font.draw(this.game.batch, "MAIN MENU", 400, 300);
        this.game.batch.end();

        if (Gdx.input.isTouched()) { // Launch default course
            String formula = "0.1 * x + 0.3 * x ^ 2 + 0.2 * y";
            double[] start = new double[]{2, 4};
            double[] goal = new double[]{4, 3};
            Function function = new Function(formula);
            Course course = new Course(function, 9.81, 0.5, 3, start, goal, 0.02);
            this.game.setScreen(new CourseScreen(this.game, course, new Ball(1)));
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

    @Override
    public void hide() {
        this.menuMusic.stop();
    }

    @Override
    public void dispose() {
        this.menuMusic.dispose();
    }
}
