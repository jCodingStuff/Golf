package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;


/**
 * Created by kim on 20.03.2018.
 */

class ImportScreen implements Screen {
    private Stage stage;
    TextField txtf;


    public ImportScreen(){
        this.stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        txtf = new TextField("Search for file:", skin);
        stage.addActor(txtf);
        // Set the stage as InputProcessor
        Gdx.input.setInputProcessor(this.stage);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
