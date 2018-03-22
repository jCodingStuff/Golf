package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.listeners.MovesPlayListener;

public class ImportMovesScreen implements Screen {

    Golf game;
    Stage stage;
    TextField txt;
    Ball ball;
    Course course;
    Label label;
    TextButton back;
    TextButton playBt;
    Music music;
    OrthographicCamera cam;
    Texture background;

    public ImportMovesScreen(Golf game, Course course, Ball ball) {
        this.game = game;
        this.stage = new Stage();
        this.ball = ball;
        this.course = course;
        Gdx.input.setInputProcessor(stage);

        // Setup background image
        this.background = new Texture(Gdx.files.internal("minigolf_background.jpg"));

        // Setup music
        this.music = Gdx.audio.newMusic(Gdx.files.internal("mariokart8_rainbowroad.mp3"));
        this.music.setVolume(0.2f);
        this.music.setLooping(true);

        // Setup cam
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        this.label = new Label("File Name", skin);
        this.label.setPosition(200, 500);
        this.txt = new TextField("", skin);
        this.txt.setPosition(200, 350);
        this.back = new TextButton("Back", skin);
        this.back.setPosition(200, 200);

        class MovesBackListener extends ChangeListener {

            Golf game;
            Screen screen;
            Course course;
            Ball ball;

            public MovesBackListener(Golf game, Screen screen, Course course, Ball ball) {
                this.game = game;
                this.screen = screen;
                this.course = course;
                this.ball = ball;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DesignScreen scr = new DesignScreen(this.game);
                scr.txtFunction.setText(this.course.getFunction().formula);
                scr.txtStartPos.setText(this.course.getStart()[0] + " " + this.course.getStart()[1]);
                scr.txtGoalPos.setText(this.course.getGoal()[0] + " " + this.course.getGoal()[1]);
                scr.txtRadius.setText("" + this.course.getTolerance());
                scr.txtVMax.setText("" + this.course.getVmax());
                scr.txtFriction.setText("" + this.course.getMu());
                scr.txtGravity.setText("" + this.course.getG());
                scr.txtBallMass.setText("" + this.ball.getMass());
                this.game.setScreen(scr);
                this.screen.dispose();
            }
        }
        this.back.addListener(new MovesBackListener(this.game, this, this.course, this.ball));


        this.playBt = new TextButton("Play", skin);
        this.playBt.setPosition(270, 200);
        this.playBt.addListener(new MovesPlayListener(this.game, this.course, this.ball, this, this.txt));

        this.stage.addActor(this.label);
        this.stage.addActor(this.txt);
        this.stage.addActor(this.back);
        this.stage.addActor(this.playBt);
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
    }
}
