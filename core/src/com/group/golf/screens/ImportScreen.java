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
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.group.golf.Golf;
import com.group.golf.listeners.ImportBackListener;
import com.group.golf.listeners.ImportListener;


/**
 * Screen to import a course
 * @author Kim Roggenbuck
 * @author Lillian Wush
 */

class ImportScreen implements Screen {
    private Stage stage;
    TextField txtf;
    final Golf game;
    Music music;
    TextButton back;
    OrthographicCamera cam;
    TextButton importButton;
    Texture background;

    /**
     * Create a new ImportScreen instance
     * @param game the Golf instance
     */
    public ImportScreen(final Golf game){

        this.game = game;
        this.stage = new Stage();

        this.background = new Texture(Gdx.files.internal("minigolf_background.jpg"));

        // Setup cam
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        // initializing textfield for filename
        txtf = new TextField("Enter a file name", skin);
        txtf.setSize(200, 60);
        txtf.setPosition(400, 300);

        // listener for textfield
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

        // initializing buttons
        this.back = new TextButton("Back", skin);
        this.back.setPosition(100, 300);
        this.back.setSize(100, 60);
        this.back.addListener(new ImportBackListener(this.game, this));

        this.importButton = new TextButton("Import", skin);
        this.importButton.setPosition(800, 300);
        this.importButton.setSize(100, 60);
        this.importButton.addListener(new ImportListener(this.game, this.txtf, this));

        stage.addActor(txtf);
        stage.addActor(back);
        stage.addActor(this.importButton);

        // Set the stage as InputProcessor
        Gdx.input.setInputProcessor(this.stage);

        // Set up music
        this.music = Gdx.audio.newMusic(Gdx.files.internal("mariokart8_rainbowroad.mp3"));
        this.music.setVolume(0.2f);
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
     * @param delta time between last and current frame
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
     * Stop music if hide
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
