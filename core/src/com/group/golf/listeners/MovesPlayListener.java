package com.group.golf.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.modes.FileMode;
import com.group.golf.modes.GameMode;
import com.group.golf.screens.CourseScreen;

/**
 * Screen to load moves file
 */
public class MovesPlayListener extends ChangeListener {

    final Golf game;
    Course course;
    Ball ball;
    Screen screen;
    TextField txt;

    /**
     * Create a new MovesPlayListener
     * @param game the Golf instance
     * @param course the Course instance
     * @param ball the Ball instance
     * @param screen the screen where this listener was created
     * @param txt the TextField containing the name of the file
     */
    public MovesPlayListener(final Golf game, Course course, Ball ball, Screen screen, TextField txt) {
        this.game = game;
        this.course = course;
        this.ball = ball;
        this.screen = screen;
        this.txt = txt;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        String name = this.txt.getText();
        String path = "moves/" + name + ".txt";
        if (!name.equals("") && !name.equalsIgnoreCase("Enter a file name")
                && Gdx.files.local(path).exists()) {
            FileHandle file = Gdx.files.local(path);
            String moves = file.readString();
            GameMode gameMode = new FileMode(this.game, this.course, this.ball, moves);
            this.game.setScreen(new CourseScreen(this.game, this.course, gameMode));
            this.screen.dispose();
        }
    }
}
