package com.group.golf.modes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;

public interface GameMode {

    void render(Batch batch);

    boolean move(OrthographicCamera cam);

    void water();

    void extraChecks();

    void setOffsets(float[] offsets);

    void setScales(float[] scales);

    void dispose();

}
