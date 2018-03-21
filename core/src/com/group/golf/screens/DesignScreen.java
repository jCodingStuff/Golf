package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.group.golf.Golf;

/**
 * Created by kim on 20.03.2018.
 */


class DesignScreen implements Screen {
    final Golf game;

    Stage stage;
    OrthographicCamera cam;

    TextField txtFunction;
    TextField txtStartPos;
    TextField txtGoalPos;
    TextField txtRadius;
    TextField txtVMax;
    TextField txtFriction;
    TextField txtGravity;
    TextButton btnStart;
    TextButton btnExport;
    TextButton btnMode1;
    TextButton btnMode2;
    ButtonGroup mode;
    public DesignScreen(final Golf game){
        this.game = game;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

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

        btnStart = new TextButton("Start", skin);
        btnExport = new TextButton("Export", skin);

        btnMode1 = new TextButton("Mode 1", skin);
        btnMode2 = new TextButton("Mode 2", skin);

        txtFriction.setPosition(300, 400);
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

        txtFunction.setPosition(300, 300);
        txtFunction.setSize(200,60);

        txtVMax.setPosition(300, 200);
        txtVMax.setSize(200,60);

        txtStartPos.setPosition(300, 100);
        txtStartPos.setSize(200,60);

        txtGoalPos.setPosition(300, 0);
        txtGoalPos.setSize(200,60);

        txtGravity.setPosition(300, 400);
        txtGravity.setSize(200,60);

        txtRadius.setPosition(300, 400);
        txtRadius.setSize(200,60);

        btnMode1.setPosition(600, 400);
        btnMode1.setSize(200,60);
        btnMode2.setPosition(600, 300);
        btnMode2.setSize(200,60);

        mode = new ButtonGroup(btnMode1, btnMode2);
        mode.setMaxCheckCount(1);
        mode.setMinCheckCount(1);
        mode.setUncheckLast(true);

        stage.addActor(txtFriction);
        stage.addActor(txtFunction);
        stage.addActor(txtVMax);
        stage.addActor(btnMode1);
        stage.addActor(btnMode2);


        Gdx.input.setInputProcessor(this.stage);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.196f, 0.804f, 0.196f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.cam.update();
        this.game.batch.setProjectionMatrix(this.cam.combined);

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

    }
    @Override
    public void dispose() {

    }
}
