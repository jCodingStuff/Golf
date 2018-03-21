package com.group.golf.listeners;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.group.golf.Golf;

public class ImportListener extends ChangeListener {

    final Golf game;
    private TextField txtField;

    public ImportListener(final Golf game, TextField txtField) {
        this.game = game;
        this.txtField = txtField;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {

    }
}
