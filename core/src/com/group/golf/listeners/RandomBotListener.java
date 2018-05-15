package com.group.golf.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.Physics.Physics;
import com.group.golf.ai.RandomBot;
import com.group.golf.screens.CourseScreen;

/**
 * Created by kim on 07.05.2018.
 */

/**
 * Screen to load bot
 */
public class RandomBotListener extends ChangeListener {

    final Golf game;
    Physics engine;
    Course course;
    Ball ball;
    Screen screen;
    TextField txt;

    /**
     * Create a new RandomBotListener
     * @param game the Golf instance
     * @param screen the screen where this listener was created
     */
    public RandomBotListener(final Golf game,Screen screen) {
        this.game = game;
        this.ball = ball;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
            this.game.setScreen(new CourseScreen(this.game, this.course, this.ball));
            RandomBot bot = new RandomBot(engine, ball);
            this.screen.dispose();
        }
    }
