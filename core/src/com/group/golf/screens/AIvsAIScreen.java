package com.group.golf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.StringBuilder;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.ai.*;
import com.group.golf.math.JVector2;
import com.group.golf.modes.GameMode;
import com.group.golf.modes.UndefinedBotMode;
import com.group.golf.modes.UndefinedPlayerMode;
import com.group.golf.modes.WallCreationMode;

/**
 * A screen to enter the AI vs AI mode
 * @author Julian Marrades
 */
public class AIvsAIScreen implements Screen {

    private final Golf game;

    private Stage stage;

    private final Course course;
    private final Ball ball;

    private final SelectBox<Label> bot1;
    private final SelectBox<Label> bot2;

    private final TextButton back;
    private final TextButton play;
    private Music menuMusic;
    private OrthographicCamera cam;
    private Texture background;

    /**
     * Create a new AIvsAIScreen instance
     * @param game the game instance
     * @param course the course that will be played
     * @param ball the ball holding properties
     */
    public AIvsAIScreen(final Golf game, final Course course, final Ball ball) {
        this.game = game;
        this.course = course;
        this.ball = ball;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        // SelectBoxes
        this.bot1 = this.generateBox(skin);
        this.bot2 = this.generateBox(skin);

        back = new TextButton("Back", skin);
        play = new TextButton("Play", skin);

        back.setPosition(100, 300);
        play.setPosition(800, 300);
        bot1.setPosition(300, 300);
        bot2.setPosition(540, 300);

        back.setSize(100, 60);
        play.setSize(100, 60);
        bot1.setSize(160, 60);
        bot2.setSize(160, 60);

        stage.addActor(back);
        stage.addActor(play);
        stage.addActor(bot1);
        stage.addActor(bot2);

        // Button Listeners
        class BackListener extends ChangeListener {
            private Screen screen;

            public BackListener(Screen screen) {
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                back.setTouchable(Touchable.disabled);
                play.setTouchable(Touchable.disabled);
                bot1.setTouchable(Touchable.disabled);
                bot2.setTouchable(Touchable.disabled);
                game.setScreen(new CourseSelectorScreen(game));
                this.screen.dispose();
            }
        }
        back.addListener(new BackListener(this));

        class PlayListener extends ChangeListener {
            private Screen screen;

            public PlayListener(Screen screen) {
                this.screen = screen;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                StringBuilder bot1Builder = bot1.getSelected().getText();
                StringBuilder bot2Builder = bot2.getSelected().getText();
                back.setTouchable(Touchable.disabled);
                play.setTouchable(Touchable.disabled);
                bot1.setTouchable(Touchable.disabled);
                bot2.setTouchable(Touchable.disabled);

                String bot1Str = bot1Builder.toString();
                String bot2Str = bot2Builder.toString();
                String[] strs = new String[] {bot1Str, bot2Str};

                Ball[] balls = new Ball[]{ball, new Ball(ball)};
                Bot[] bots = new Bot[2];

                for (int i = 0; i < strs.length; i++) {
                    int num = i + 1;
                    if (strs[i].equalsIgnoreCase("GeneticBot")) {
                        bots[i] = new GeneticBot(course, balls[i]);
                        System.out.println("Setting bot " + num + " as GeneticBot");
                    } else if (strs[i].equalsIgnoreCase("PledgeBot")) {
                        bots[i] = new PledgeBot(course, balls[i]);
                        System.out.println("Setting bot " + num + " as PledgeBot");
                    } else if (strs[i].equalsIgnoreCase("RandomBot")) {
                        bots[i] = new RandomBot(course, balls[i]);
                        System.out.println("Setting bot " + num + " as RandomBot");
                    } else if (strs[i].equalsIgnoreCase("DumBot")) {
                        bots[i] = new DumBot(course, balls[i]);
                        System.out.println("Setting bot " + num + " as DumBot");
                    } else {
                        bots[i] = new DumBot(course, balls[i]);
                        System.out.println("Setting bot " + num + " as DumBot by DEFAULT...");
                    }
                }

                GameMode gameMode = new UndefinedBotMode(game, bots, course, balls);
                GameMode wallMode = new WallCreationMode(game, course, balls);

                game.setScreen(new EngineSelectorScreen(game, course, gameMode, wallMode));
                this.screen.dispose();
            }
        }
        play.addListener(new PlayListener(this));

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
     * Generate a selectBox for ai selection
     * @param skin the skin to apply to the box
     * @return the selectBox
     */
    private SelectBox<Label> generateBox(Skin skin) {
        Label[] blob = new Label[4];
        blob[0] = new Label("GeneticBot", skin);
        blob[1] = new Label("PledgeBot", skin);
        blob[2] = new Label("RandomBot", skin);
        blob[3] = new Label("DumBot", skin);
        final SelectBox<Label> sb = new SelectBox<Label>(skin);
        sb.setItems(blob);
        return sb;
    }

    /**
     * Start playing the background musics
     */
    @Override
    public void show() {
        this.menuMusic.play();
    }

    /**
     * Render UI
     * @param delta the time between last and current frame
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
     * Stop playing music when hide
     */
    @Override
    public void hide() {
        this.menuMusic.stop();
    }

    /**
     * Dispose sound and sprites
     */
    @Override
    public void dispose() {
        this.menuMusic.dispose();
        this.background.dispose();
    }
}
