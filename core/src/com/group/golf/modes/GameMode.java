package com.group.golf.modes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.group.golf.Course;
import com.group.golf.Physics.Euler;
import com.group.golf.Physics.Physics;
import com.group.golf.Physics.PredictorCorrector;
import com.group.golf.Physics.RK4;

abstract public class GameMode {

    protected float[] offsets;
    protected float[] scales;

    protected Physics engine;
    protected Course course;

    public abstract void render(Batch batch);

    public abstract boolean move(OrthographicCamera cam);

    public abstract void water();

    public abstract void extraChecks();

    public void setOffsets(float[] offsets) {
        this.offsets = offsets;
    }

    public void setScales(float[] scales) {
        this.scales = scales;
    }

    public abstract void dispose();

    protected void setUpPhysics(String diffMethod) {
        engine = new RK4(course);
//        if (diffMethod == "RK4")
//            engine = new RK4(course);
//        else
//            engine = new Euler(course);


    }

}
