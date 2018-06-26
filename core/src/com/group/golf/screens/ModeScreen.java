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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.ai.Bot;
import com.group.golf.ai.DumBot;
import com.group.golf.ai.GeneticBot;
import com.group.golf.modes.*;

import java.util.Scanner;

/**
 * Screen class to select the desired gamemode
 * @author Julian Marrades
 * @author Kaspar Kallast
 * @author Kim Roggenbuck
 * @author Lillian Wush
 */
public class ModeScreen implements Screen {

    private final Golf game;
    private Stage stage;

    private final Course course;
    private final Ball ball;

    private TextButton singlePlayer;
    private TextButton multiplayer;
    private TextButton ai;
    private TextButton playerVSai;
    private TextButton aiVSai;
    private TextButton back;
    private Music menuMusic;
    private OrthographicCamera cam;
    private Texture background;

    /**
     * Create a new ModeScreen instance
     * @param game the game instance
     * @param course the course that will be played
     * @param ball the ball holding the properties
     */
    public ModeScreen(final Golf game, final Course course, final Ball ball) {
        this.game = game;
        this.course = course;
        this.ball = ball;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        back = new TextButton("Back", skin);
        singlePlayer = new TextButton("SinglePlayer", skin);
        multiplayer = new TextButton("Multiplayer", skin);
        ai = new TextButton("Bot", skin);
        playerVSai = new TextButton("Player vs Bot", skin);
        aiVSai = new TextButton("Bot vs Bot", skin);

        back.setPosition(100, 300);
        singlePlayer.setPosition(300, 300);
        ai.setPosition(600, 300);
        playerVSai.setPosition(300, 400);
        multiplayer.setPosition(600, 400);
        aiVSai.setPosition(300, 200);

        back.setSize(100, 60);
        singlePlayer.setSize(200, 60);
        ai.setSize(200, 60);
        playerVSai.setSize(200, 60);
        multiplayer.setSize(200, 60);
        aiVSai.setSize(200, 60);


        stage.addActor(back);
        stage.addActor(singlePlayer);
        stage.addActor(multiplayer);
        stage.addActor(ai);
        stage.addActor(playerVSai);
        stage.addActor(aiVSai);

        // Setup button listeners
        class BackListener extends ChangeListener {
            final Golf game;
            private Screen screen;

            public BackListener(final Golf game, Screen screen) {
                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((ModeScreen) this.screen).disableButtons();
                this.game.setScreen(new CourseSelectorScreen(this.game));
                this.screen.dispose();
            }
        }
        back.addListener(new BackListener(game, this));

        class BotListener extends ChangeListener {
            final Golf game;
            private Screen screen;

            public BotListener(final Golf game, Screen screen) {
                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((ModeScreen) this.screen).disableButtons();
                this.game.setScreen(new BotScreen(this.game, course, ball, false));
                this.screen.dispose();
            }
        }
        ai.addListener(new BotListener(game, this));

        class SingleListener extends ChangeListener {
            final Golf game;
            private Screen screen;

            public SingleListener(final Golf game, Screen screen) {
                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((ModeScreen) this.screen).disableButtons();
                Ball[] balls = new Ball[]{ball};
                GameMode gameMode = new UndefinedPlayerMode(this.game, course, balls);
                GameMode wallMode = new WallCreationMode(this.game, course, balls);
                this.game.setScreen(new EngineSelectorScreen(this.game, course, gameMode, wallMode));
                this.screen.dispose();
            }
        }
        singlePlayer.addListener(new SingleListener(this.game, this));

        class PVAIListener extends ChangeListener {
            final Golf game;
            private Screen screen;

            public PVAIListener(final Golf game, Screen screen) {
                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((ModeScreen) this.screen).disableButtons();
                this.game.setScreen(new BotScreen(this.game, course, ball, true));
                this.screen.dispose();
            }
        }
        playerVSai.addListener(new PVAIListener(this.game, this));

        class MultiplayerListener extends ChangeListener {
            final Golf game;
            private Screen screen;

            public MultiplayerListener(final Golf game, Screen screen) {
                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.print("Number of players: ");
                Scanner in = new Scanner(System.in);
                int numberOfPlayers = in.nextInt();
                if (numberOfPlayers > 1) {
                    Ball[] balls = new Ball[numberOfPlayers];
                    balls[0] = ball;
                    if (numberOfPlayers > 1) {
                        for (int i = 1; i < balls.length; i++) {
                            balls[i] = new Ball(ball);
                        }
                        GameMode gameMode = new UndefinedPlayerMode(this.game, course, balls);
                        GameMode wallMode = new WallCreationMode(this.game, course, balls);this.screen.dispose();
                        ((ModeScreen) this.screen).disableButtons();
                        this.game.setScreen(new EngineSelectorScreen(this.game, course, gameMode, wallMode));
                    }
                } else {
                    System.out.println("PLAY SINGLEPLAYER MODE!");
                }
            }
        }
        multiplayer.addListener(new MultiplayerListener(this.game, this));

        class AIvsAIListener extends ChangeListener {
            final Golf game;
            private Screen screen;

            public AIvsAIListener(final Golf game, Screen screen) {
                this.game = game;
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((ModeScreen) this.screen).disableButtons();
                this.game.setScreen(new AIvsAIScreen(this.game, course, ball));
                this.screen.dispose();
            }
        }
        aiVSai.addListener(new AIvsAIListener(this.game, this));

        // Setup cam
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(false, Golf.VIRTUAL_WIDTH, Golf.VIRTUAL_HEIGHT);

        // Setup music
        this.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("mariokart8_mainmenu.mp3"));
        this.menuMusic.setVolume(0.2f);
        this.menuMusic.setLooping(true);

        // Setup background image
        this.background = new Texture(Gdx.files.internal("minigolf_background.jpg"));
    }

    /**
     * Disable all buttons
     */
    private void disableButtons() {
        singlePlayer.setTouchable(Touchable.disabled);
        playerVSai.setTouchable(Touchable.disabled);
        ai.setTouchable(Touchable.disabled);
        aiVSai.setTouchable(Touchable.disabled);
        playerVSai.setTouchable(Touchable.disabled);
        back.setTouchable(Touchable.disabled);
        multiplayer.setTouchable(Touchable.disabled);
    }

    /**
     * Start playing background music
     */
    @Override
    public void show() {
        this.menuMusic.play();
    }

    /**
     * Render UI
     * @param delta time between last and current frame
     */
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

    /**
     * Stop music when hide
     */
    @Override
    public void hide() {
        this.menuMusic.stop();
    }

    /**
     * Dispose sprite and sound
     */
    @Override
    public void dispose() {
        this.menuMusic.dispose();
        this.background.dispose();
    }

}
