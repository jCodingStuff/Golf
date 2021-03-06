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
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.listeners.ExportListener;
import com.group.golf.listeners.ImportBackListener;
import com.group.golf.listeners.ImportListener;
import com.badlogic.gdx.Screen;
import com.group.golf.math.Function;

/**
 * A screen to export a course into a file.
 * @author Kaspar Kallast
 */
public class ExportScreen implements Screen {

	private Stage stage;
    TextField txtf;
    final Golf game;
    Music music;
    TextButton back;
    //Label label;
    OrthographicCamera cam;
    TextButton exportButton;
    Texture background;
    Course course1;
    Ball ball1;

    /**
     * Create a new ExportScreen instance.
     * @param game The golf instance.
     * @param course1 The course1 instance.
     * @param ball1 The ball1 instance.
     */
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

        txtf = new TextField("Enter a file name", skin);
        txtf.setPosition(400, 300);
        txtf.setSize(200, 60);

        txtf.addListener(new FocusListener(){
            @Override
            public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
                if(focused == true) {
                    if (txtf.getText().equals("Enter a file name"))
                        txtf.setText("");

                }
                else if(focused == false){
                    if(txtf.getText().equals(""))
                        txtf.setText("Enter a file name");
                }
            }
        });

        this.back = new TextButton("Back", skin);
        this.back.setPosition(100, 300);
        this.back.setSize(100, 60);

        class ExportBackListener extends ChangeListener {
            final Golf game;
            Screen screen;
            Course course;
            Ball ball;
            public ExportBackListener(final Golf game, Screen screen, Course course, Ball ball) {
                this.game = game;
                this.screen = screen;
                this.course = course;
                this.ball = ball;
            }
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DesignScreen scr = new DesignScreen(this.game);
                scr.txtFunction.setText(((Function)this.course.getFunction()).getName());
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

        this.exportButton = new TextButton("Export", skin);
        this.exportButton.setPosition(800, 300);
        this.exportButton.setSize(100, 60);
        this.exportButton.addListener(new ExportListener(this.game, this, this.course1, this.ball1, this.txtf));

        stage.addActor(txtf);
        stage.addActor(back);
        stage.addActor(this.exportButton);


        // Set the stage as InputProcessor
        Gdx.input.setInputProcessor(this.stage);

        // Set up music
        this.music = Gdx.audio.newMusic(Gdx.files.internal("exportmusic.mp3"));
        this.music.setVolume(1.5f);
        this.music.setLooping(true);
    }

    /**
     * Start playing background music
     */
    @Override
    public void show() {
        this.music.play();
    }

    /**
     * Render UI
     * @param delta the time between last and current frame
     */
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

    /**
     * Stop music when hide
     */
    @Override
    public void hide() {
        this.music.stop();
    }

    /**
     * Dispose sound and sprite
     */
    @Override
    public void dispose() {
        this.music.dispose();
        this.background.dispose();
    }
}
