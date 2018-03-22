package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.listeners.ImportBackListener;
import com.group.golf.listeners.ImportListener;
import com.badlogic.gdx.Screen;
import com.group.golf.math.Function;

public class ExportScreen implements Screen {

	private Stage stage;
    TextField txtf;
    Golf game;
    Music music;
    TextButton back;
    Label label;
    OrthographicCamera cam;
    TextButton exportButton;
    Texture background;
    Course course1;
    Ball ball1;


    public ExportScreen(Golf game, Course course1, Ball ball1){
        this.game = game;
        this.stage = new Stage();

        this.background = new Texture(Gdx.files.internal("minigolf_background.jpg"));

        this.course1 = course1;
        this.ball1 = ball1;

        // Setup cam
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        txtf = new TextField("", skin);
        txtf.setPosition(200, 400);
        this.back = new TextButton("Back", skin);
        this.back.setPosition(200, 200);
        class ExportBackListener extends ChangeListener {
            Golf game;
            Screen screen;
            Course course;
            Ball ball;
            public ExportBackListener(Golf game, Screen screen, Course course, Ball ball) {
                this.game = game;
                this.screen = screen;
                this.course = course;
                this.ball = ball;
            }
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DesignScreen scr = new DesignScreen(this.game);
                scr.txtFunction.setText(this.course.getFunction().getName());
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
        this.back.addListener(new ExportBackListener(this.game, this, this.course1, this.ball1));

        stage.addActor(txtf);
        stage.addActor(back);

        this.exportButton = new TextButton("Export", skin);
        this.exportButton.setPosition(270, 200);
        this.exportButton.addListener(new ImportListener(this.game, this.txtf, this));
        stage.addActor(this.exportButton);

        this.label = new Label("Course Name", skin);
        this.label.setPosition(200, 500);
        stage.addActor(this.label);

        // Set the stage as InputProcessor
        Gdx.input.setInputProcessor(this.stage);

        // Set up music
        this.music = Gdx.audio.newMusic(Gdx.files.internal("exportmusic.mp3"));
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
