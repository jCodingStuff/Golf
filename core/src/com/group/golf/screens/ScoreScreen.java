package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.group.golf.Golf;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by kim on 12.06.2018.
 */

class ScoreScreen implements Screen {

    final Golf game;

    Stage stage;
    Music menuMusic;
    OrthographicCamera cam;
    Texture background;
    TextButton back;

    FreeTypeFontGenerator subTitleGenerator;
    FreeTypeFontGenerator.FreeTypeFontParameter subTitleParameter;
    BitmapFont subTitleFont;

    public ScoreScreen(Golf game) {
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.game = game;
        stage = new Stage();
        back = new TextButton("Back", skin);

        back.setPosition(100, 300);
        back.setSize(100, 60);

        Gdx.input.setInputProcessor(stage);

        // Setup cam
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        // Setup music
        this.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("mariokart8_mainmenu.mp3"));
        this.menuMusic.setVolume(0.2f);
        this.menuMusic.setLooping(true);

        // Setup background image
        this.background = new Texture(Gdx.files.internal("minigolf_background.jpg"));

        this.subTitleGenerator = new FreeTypeFontGenerator(Gdx.files.internal("MoonGetFont.otf"));
        this.subTitleParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        this.subTitleParameter.size = 50;
        this.subTitleParameter.shadowColor = Color.BLACK;
        this.subTitleParameter.shadowOffsetX = 3;
        this.subTitleParameter.shadowOffsetY = 3;
        this.subTitleFont = this.subTitleGenerator.generateFont(this.subTitleParameter);

        class BackListener extends ChangeListener {
            final Golf game;
            private Screen screen;
            public BackListener(final Golf game, Screen screen){
                this.game = game;
                this.screen = screen;
            }
            public void changed (ChangeEvent event, Actor actor) {
                this.game.setScreen(new CourseSelectorScreen(this.game));
                this.screen.dispose();
            }
        }
        back.addListener(new BackListener(game, this));

        stage.addActor(back);
    }


    @Override
    public void show() {
        this.menuMusic.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.196f, 0.804f, 0.196f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.cam.update();
        this.game.batch.setProjectionMatrix(this.cam.combined);

        this.game.batch.begin();
        this.game.batch.draw(this.background, 0, 0, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);
        this.subTitleFont.draw(this.game.batch, "Highscores", 350, 600);

        try{
            int lineCounter = 600;
            FileHandle file = Gdx.files.internal("scores.txt");
            String text = file.readString();
            String[] splitStr = text.split("\\s+");

            for (int i=0; i<5; i++) {
                for (int j = 0; j < text.length(); j++) {
                    lineCounter = lineCounter - 100;
                    this.subTitleFont.draw(this.game.batch, splitStr[j], 450, lineCounter);
                }
            }
        } catch (Exception e){ System.out.println("File Not Found."); }

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
        this.menuMusic.stop();
    }

    @Override
    public void dispose() {
        this.menuMusic.dispose();
        this.background.dispose();
    }
}
