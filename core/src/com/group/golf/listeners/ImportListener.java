package com.group.golf.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.group.golf.Golf;
import com.group.golf.screens.CourseScreen;

public class ImportListener extends ChangeListener {

    Golf game;
    private TextField txtField;
    Screen screen;

    public ImportListener(Golf game, TextField txtField, Screen screen) {
        this.game = game;
        this.txtField = txtField;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        String name = this.txtField.getText();
        String path = "courses/" + name + ".json";
        if (name != "" && Gdx.files.local(path).exists()) {
            Json json = new Json();
            FileHandle file = Gdx.files.local(path);
            String text = file.readString();
            CourseScreen newCourse = json.fromJson(CourseScreen.class, text);
            newCourse.setGame(this.game);
            this.game.setScreen(newCourse);
            this.screen.dispose();
        }
    }
}
