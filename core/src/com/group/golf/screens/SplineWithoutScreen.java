package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.group.golf.Golf;

import com.group.golf.listeners.SplineSubmitListener;
import com.group.golf.listeners.SplineWithoutSubmitListener;
import com.group.golf.math.Computable;
import com.group.golf.math.Interpolator;
import com.group.golf.math.Point3D;
//import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import static java.lang.Integer.parseInt;

/**
 * Created by kim on 13.04.2018.
 */

public class SplineWithoutScreen implements Screen{
    final Golf game;

    Stage stage;
    OrthographicCamera cam;
    Music music;

    TextField xyz;
    TextField dx;
    TextField dy;
    TextField dxy;
    TextField xUnits;
    TextField yUnits;

    TextButton btnSubmitUnits;
    TextButton btnBack;
    TextButton btnDesign;
    TextButton btnSubmit;
    Texture background;


    Point3D[][] xyzMatrix;


    public SplineWithoutScreen(final Golf game){

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

        // initializing buttons / textfields
        xyz = new TextField("xyz", skin);

        xUnits = new TextField("x Units", skin);
        yUnits = new TextField("y Units", skin);
        btnBack = new TextButton("Back", skin);
        btnDesign = new TextButton("Design", skin);
        btnSubmit = new TextButton("Submit", skin);
        btnSubmitUnits = new TextButton("Submit Units", skin);

        xyz.setPosition(250, 300);
        xyz.setSize(200,60);


        xUnits.setPosition(250, 400);
        xUnits.setSize(200,60);
        yUnits.setPosition(550, 400);
        yUnits.setSize(200,60);

        btnBack.setPosition(100, 300);
        btnBack.setSize(100,60);
        btnDesign.setPosition(800, 200);
        btnDesign.setSize(100,60);
        btnSubmit.setPosition(800, 300);
        btnSubmit.setSize(100,60);
        btnSubmitUnits.setPosition(800, 400);
        btnSubmitUnits.setSize(100,60);

        stage.addActor(xyz);

        stage.addActor(xUnits);
        stage.addActor(yUnits);

        stage.addActor(btnSubmitUnits);
        stage.addActor(btnBack);

        stage.addActor(btnDesign);


        // listener for back button
        class BackListener extends ChangeListener {
            final Golf game;
            private Screen screen;
            public BackListener(final Golf game, Screen screen){
                this.game = game;
                this.screen = screen;
            }
            @Override
            public void changed (ChangeEvent event, Actor actor) {

                this.game.setScreen(new DecisionScreen(this.game));
                this.screen.dispose();
            }

        }
        btnBack.addListener(new BackListener(game, this));

        // listener for design button
        class DesignListener extends ChangeListener {
            final Golf game;
            private Screen screen;
            public DesignListener(final Golf game, Screen screen){
                this.game = game;
                this.screen = screen;
            }
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Computable[][] computable;
                computable = Interpolator.getInterpolators(xyzMatrix);

                this.game.setScreen(new SplineDesignScreen(this.game, computable));
                this.screen.dispose();
            }

        }
        btnDesign.addListener(new DesignListener(game, this));


        class SubmitUnitsListener extends ChangeListener {
            final Golf game;
            private Screen screen;
            public SubmitUnitsListener(final Golf game, Screen screen){
                this.game = game;
                this.screen = screen;
            }
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                int x  = parseInt(xUnits.getText())+1;
                int y = parseInt(yUnits.getText())+1;

                xyzMatrix = new Point3D[x][y];

                stage.addActor(btnSubmit);
                btnSubmit.addListener(new SplineWithoutSubmitListener(game, screen, xyz, xyzMatrix,  xUnits, yUnits));
                btnSubmitUnits.setTouchable(Touchable.disabled);
                btnSubmitUnits.setVisible(false);
//                xUnits.setVisible(false);
//                yUnits.setVisible(false);

            }

        }
        btnSubmitUnits.addListener(new SubmitUnitsListener(game, this));


        // listener for textfields
        xyz.addListener(setListener("xyz",xyz));

        xUnits.addListener(setListener("x Units",xUnits));
        yUnits.addListener(setListener("y Units",yUnits));



    }

    private FocusListener setListener(final String txt, final TextField tf){

        return(new FocusListener(){
            @Override
            public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
                if(focused == true) {
                    if (tf.getText().equals(txt))
                        tf.setText("");
                }
                else if(focused == false){
                    if(tf.getText().equals(""))
                        tf.setText(txt);

                }
            }

        });

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
        this.background.dispose();
    }

}