package com.group.golf.modes;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface GameMode {

    void render(Batch batch);

    void move();

    void water();

}
