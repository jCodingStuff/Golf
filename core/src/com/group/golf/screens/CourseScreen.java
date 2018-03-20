package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.group.golf.Course;
import com.group.golf.Golf;
import jdk.nashorn.internal.objects.Global;

public class CourseScreen implements Screen {

    final Golf game;
    Course course;
    OrthographicCamera cam;
    Music music;

    // Graphing things
    float scale;
    float xoffset;
    float yoffset;
    double[][] heights;
    double maximum;
    double minimum;
    Color[][] colors;

    public CourseScreen(final Golf game, Course course) {
        this.game = game;
        this.course = course;

        // Setup Cam
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        // Setup Music
        this.music = Gdx.audio.newMusic(Gdx.files.internal("mario64_ost"));
        this.music.setVolume(0.5f);
        this.music.setLooping(true);

        // Setup Course
        this.setUpCourse();
    }

    private void setUpCourse() {
        // Set up the scale, each pixel of the screen represents 0.01 units
        this.scale = 0.01f;

        // The center of the screen is the middle point between the start and the goal
        float x1 = this.course.getStart()[0];
        float x2 = this.course.getGoal()[0];
        float xUnits = (float) (Golf.VIRTUAL_WIDTH / 100.0);
        this.xoffset = (float) ((x1 + x2 - xUnits) / 2.0);
        float y1 = this.course.getStart()[1];
        float y2 = this.course.getGoal()[1];
        float yUnits = (float) (Golf.VIRTUAL_HEIGHT / 100.0);
        this.yoffset = (float) ((y1 + y2 - yUnits) / 2.0);

        // Setup the heights matrix
        this.heights = new double[Golf.VIRTUAL_WIDTH][Golf.VIRTUAL_HEIGHT];
        this.maximum = Double.MIN_VALUE;
        this.minimum = Double.MAX_VALUE;
        for (int x = 0; x < this.heights.length; x++) {
            for (int y = 0; y < this.heights[x].length; y++) {
                double value = this.course.getFunction().getZ(xoffset + x*this.scale, yoffset + y*this.scale);
                if (value > this.maximum) this.maximum = value;
                else if (value < this.minimum) this.minimum = value;
                this.heights[x][y] = value;
            }
        }
    }

    @Override
    public void show() {
        this.music.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.cam.update();
        this.game.batch.setProjectionMatrix(this.cam.combined);
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
