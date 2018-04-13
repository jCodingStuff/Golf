package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.group.golf.Golf;

/**
 * Created by kim on 13.04.2018.
 */

public class SplineScreen implements Screen{
    final Golf game;

    Stage stage;
    OrthographicCamera cam;
    Music music;

    TextField point1;
    TextField point2;
    TextField point3;
    TextField point4;
    TextField point5;
    TextField point6;

    TextButton btnBack;
    TextButton btnDesign;
    Texture background;
    String txt;


    public SplineScreen(final Golf game){

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
        point1 = new TextField(" ", skin);
        point2 = new TextField(" ", skin);
        point3 = new TextField(" ", skin);
        point4 = new TextField(" ", skin);
        point5 = new TextField(" ", skin);
        point6 = new TextField(" ", skin);
        btnBack = new TextButton("Back", skin);
        btnDesign = new TextButton("Design", skin);

        point1.setPosition(250, 400);
        point1.setSize(200,60);
        point2.setPosition(250, 400);
        point2.setSize(200,60);
        point3.setPosition(250, 400);
        point3.setSize(200,60);
        point4.setPosition(250, 400);
        point4.setSize(200,60);
        point5.setPosition(250, 400);
        point5.setSize(200,60);
        point6.setPosition(250, 400);
        point6.setSize(200,60);
        btnBack.setPosition(100, 300);
        btnBack.setSize(100,60);
        btnDesign.setPosition(800, 300);
        btnDesign.setSize(100,60);

        stage.addActor(point1);
        stage.addActor(point2);
        stage.addActor(point3);
        stage.addActor(point4);
        stage.addActor(point5);
        stage.addActor(point6);
        stage.addActor(btnBack);
        stage.addActor(btnDesign);

        //Gdx.input.setInputProcessor(this.stage);

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

                this.game.setScreen(new SplineDesignScreen(this.game));
                this.screen.dispose();
            }

        }
        btnDesign.addListener(new DesignListener(game, this));

        /*// listener for textfields
        point1.addListener(setListener(" ",point1));
        point2.addListener(setListener(" ",point2));
        point3.addListener(setListener(" ",point3));
        point4.addListener(setListener(" ",point4));
        point5.addListener(setListener(" ",point5));
        point6.addListener(setListener(" ",point6));

    }*/
    /*
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

*/
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