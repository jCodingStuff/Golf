package com.group.golf.listeners;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.group.golf.Golf;
import com.group.golf.screens.CourseScreen;
import com.group.golf.screens.CourseSelectorScreen;

/**
 * Listener to go backwards from the import Screen
 */
public class ImportBackListener extends ChangeListener {

    Golf game;
    private Screen screen;

    /**
     * Create a new ImportBackListener
     * @param game the Golf instance
     * @param screen the Screen where this listener was created
     */
    public ImportBackListener(Golf game, Screen screen) {
        this.game = game;
        this.screen = screen;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        this.game.setScreen(new CourseSelectorScreen(this.game));
        this.screen.dispose();
    }
}
