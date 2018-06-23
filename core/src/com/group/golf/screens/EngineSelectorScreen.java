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
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.modes.GameMode;

public class EngineSelectorScreen implements Screen {

    private final Golf game;
    private final Course course;
    private final GameMode gameMode;
    private final GameMode wallMode;

    private Stage stage;
    private Music menuMusic;
    private OrthographicCamera cam;
    private Texture background;

    // Buttons
    private TextButton back;
    private TextButton euler;
    private TextButton verlet;
    private TextButton rungeKutta;
    private TextButton predictorCorrector;

    public EngineSelectorScreen(final Golf game, final Course course, final GameMode gameMode, final GameMode wallMode) {
        this.game = game;
        this.course = course;
        this.gameMode = gameMode;
        this.wallMode = wallMode;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Buttons
        back = new TextButton("Back", skin);
        euler = new TextButton("Euler", skin);
        verlet = new TextButton("Verlet", skin);
        rungeKutta = new TextButton("Runge-Kutta", skin);
        predictorCorrector = new TextButton("Predictor-Corrector", skin);

        back.setPosition(100, 300);
        euler.setPosition(300, 300);
        verlet.setPosition(600, 300);
        rungeKutta.setPosition(300, 400);
        predictorCorrector.setPosition(600, 400);

        back.setSize(100, 60);
        euler.setSize(200, 60);
        verlet.setSize(200, 60);
        rungeKutta.setSize(200, 60);
        predictorCorrector.setSize(200, 60);

        stage.addActor(back);
        stage.addActor(euler);
        stage.addActor(verlet);
        stage.addActor(rungeKutta);
        stage.addActor(predictorCorrector);

        // Button Listeners
        class BackListener extends ChangeListener {
            private Screen screen;
            public BackListener(Screen screen) {
                this.screen = screen;
            }
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((EngineSelectorScreen) this.screen).disableButtons();
                game.setScreen(new CourseSelectorScreen(game));
                this.screen.dispose();
            }
        }
        back.addListener(new BackListener(this));

        class EulerListener extends ChangeListener {
            private Screen screen;
            public EulerListener(Screen screen) {
                this.screen = screen;
            }
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((EngineSelectorScreen) this.screen).disableButtons();
                gameMode.setUpPhysics("Euler");
                game.setScreen(new CourseScreen(game, course, gameMode, wallMode));
                this.screen.dispose();
            }
        }
        euler.addListener(new EulerListener(this));

        class VerletListener extends ChangeListener {
            private Screen screen;
            public VerletListener(Screen screen) {
                this.screen = screen;
            }
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((EngineSelectorScreen) this.screen).disableButtons();
                gameMode.setUpPhysics("Verlet");
                game.setScreen(new CourseScreen(game, course, gameMode, wallMode));
                this.screen.dispose();
            }
        }
        verlet.addListener(new VerletListener(this));

        class RK4Listener extends ChangeListener {
            private Screen screen;
            public RK4Listener(Screen screen) {
                this.screen = screen;
            }
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((EngineSelectorScreen) this.screen).disableButtons();
                gameMode.setUpPhysics("RK4");
                game.setScreen(new CourseScreen(game, course, gameMode, wallMode));
                this.screen.dispose();
            }
        }
        rungeKutta.addListener(new RK4Listener(this));

        class PCListener extends ChangeListener {
            private Screen screen;
            public PCListener(Screen screen) {
                this.screen = screen;
            }
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((EngineSelectorScreen) this.screen).disableButtons();
                gameMode.setUpPhysics("PredictorCorrector");
                game.setScreen(new CourseScreen(game, course, gameMode, wallMode));
                this.screen.dispose();
            }
        }
        predictorCorrector.addListener(new PCListener(this));

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

    private void disableButtons() {
        predictorCorrector.setTouchable(Touchable.disabled);
        verlet.setTouchable(Touchable.disabled);
        rungeKutta.setTouchable(Touchable.disabled);
        back.setTouchable(Touchable.disabled);
        euler.setTouchable(Touchable.disabled);
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
