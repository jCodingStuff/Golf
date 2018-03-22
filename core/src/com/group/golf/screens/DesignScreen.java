package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.group.golf.Golf;
import com.group.golf.listeners.Mode1Listener;
import com.group.golf.listeners.Mode2Listener;
import com.group.golf.listeners.ToExportListener;

/**
 * Created by kim on 20.03.2018.
 * Screen to design courses
 */
class DesignScreen implements Screen {
    Golf game;

    Stage stage;
    OrthographicCamera cam;
    Music music;

    TextField txtFunction;
    TextField txtStartPos;
    TextField txtGoalPos;
    TextField txtRadius;
    TextField txtVMax;
    TextField txtFriction;
    TextField txtGravity;
    TextField txtBallMass;

    TextButton btnExport;
    TextButton btnMode1;
    TextButton btnMode2;
    TextButton btnBack;
    Texture background;

    /**
     * Create a new Design Screen
     * @param game the Golf instace
     */
    public DesignScreen(Golf game){
        this.game = game;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Set up music
        this.music = Gdx.audio.newMusic(Gdx.files.internal("mariokart8_rainbowroad.mp3"));
        this.music.setVolume(0.2f);
        this.music.setLooping(true);

        // Setup background image
        this.background = new Texture(Gdx.files.internal("minigolf_background.jpg"));

        // Setup cam
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        txtFunction = new TextField("Function", skin);

        txtStartPos = new TextField("Start Position", skin);
        txtGoalPos = new TextField("Goal Position", skin);
        txtRadius = new TextField("Radius", skin);
        txtGravity = new TextField("Gravity", skin);
        txtFriction = new TextField("Friction", skin);
        txtVMax = new TextField("Maximum velocity", skin);
        txtBallMass = new TextField("Mass of the ball",skin);


        btnExport = new TextButton("Export", skin);
        btnExport.addListener(new ToExportListener(this.game, this, txtFunction, txtStartPos, txtGoalPos, txtRadius,
                txtVMax, txtFriction, txtGravity, txtBallMass));
        
        btnBack = new TextButton("Back", skin);

        btnMode1 = new TextButton("Mode 1", skin);
        btnMode1.addListener(new Mode1Listener(this.game, this, txtFunction, txtStartPos, txtGoalPos, txtRadius,
                txtVMax, txtFriction, txtGravity, txtBallMass));
        btnMode2 = new TextButton("Mode 2", skin);
        btnMode2.addListener(new Mode2Listener(this.game, this, txtFunction, txtStartPos, txtGoalPos, txtRadius,
                txtVMax, txtFriction, txtGravity, txtBallMass));

        txtFriction.setPosition(250, 400);
        txtFriction.setSize(200,60);
        txtFriction.addListener(new FocusListener(){
            public void focusGained(FocusEvent e) {
                if(txtFriction.getText().equals("Friction "))
                    txtFriction.setText("");
            }

            public void focusLost(FocusEvent e) {
                if(txtFriction.getText().equals(""))
                    txtFriction.setText("Friction");
            }
        });

        txtFunction.setPosition(250, 300);
        txtFunction.setSize(200,60);

        txtVMax.setPosition(250, 200);
        txtVMax.setSize(200,60);

        txtStartPos.setPosition(550, 400);
        txtStartPos.setSize(200,60);



        txtGoalPos.setPosition(550, 300);
        txtGoalPos.setSize(200,60);

        txtGravity.setPosition(250, 500);
        txtGravity.setSize(200,60);

        txtRadius.setPosition(550, 500);
        txtRadius.setSize(200,60);

        txtBallMass.setPosition(550, 200);
        txtBallMass.setSize(200,60);

        btnExport.setPosition(800,300);
        btnExport.setSize(100,60);

        btnBack.setPosition(100,300);
        btnBack.setSize(100,60);

        btnMode1.setPosition(250, 100);
        btnMode1.setSize(200,60);
        btnMode2.setPosition(550, 100);
        btnMode2.setSize(200,60);

        stage.addActor(txtFriction);
        stage.addActor(txtFunction);
        stage.addActor(txtVMax);
        stage.addActor(btnMode1);
        stage.addActor(btnMode2);
        stage.addActor(txtGravity);
        stage.addActor(txtRadius);
        stage.addActor(txtStartPos);
        stage.addActor(txtGoalPos);
        stage.addActor(txtBallMass);
        stage.addActor(btnExport);
        stage.addActor(btnBack);


        //Gdx.input.setInputProcessor(this.stage);

        class BackListener extends ChangeListener {
            final Golf game;
            private Screen screen;
            public BackListener(final Golf game, Screen screen){
                this.game = game;
                this.screen = screen;
            }
            @Override
            public void changed (ChangeEvent event, Actor actor) {

                this.game.setScreen(new CourseSelectorScreen(this.game));
                this.screen.dispose();
            }

        }
        btnBack.addListener(new BackListener(game, this));

        /*class FrictionListener extends FocusListener {
            final Golf game;
            private Screen screen;
            public BackListener(final Golf game, Screen screen){
                this.game = game;
                this.screen = screen;
            }
            @Override
            public void changed (FocusEvent event, Actor actor) {

                this.game.setScreen(new CourseSelectorScreen(this.game));
                this.screen.dispose();
            }

        }
        btnBack.addListener(new BackListener(game, this));*/
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
