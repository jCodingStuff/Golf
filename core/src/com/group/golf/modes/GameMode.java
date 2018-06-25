package com.group.golf.modes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.group.golf.Course;
import com.group.golf.Physics.*;


abstract public class GameMode {

    protected float[] offsets;
    protected float[] scales;

    protected Physics engine;
    protected Course course;

    public abstract void render(Batch batch);

    public abstract boolean move(OrthographicCamera cam);

//    public abstract void water();

    public abstract void extraChecks();

    public void setOffsets(float[] offsets) {
        this.offsets = offsets;
    }

    public void setScales(float[] scales) {
        this.scales = scales;
    }

    public abstract void dispose();

    public void setUpPhysics(String diffMethod) {
        if (diffMethod.equalsIgnoreCase("Euler")) {
            this.engine = new Euler(this.course);
            System.out.println("Setting up Euler physics");
        } else if (diffMethod.equalsIgnoreCase("Verlet")) {
            this.engine = new Verlet(this.course);
            System.out.println("Setting up Verlet physics");
        } else if (diffMethod.equalsIgnoreCase("RK4")) {
            this.engine = new RK4(this.course);
            System.out.println("Setting up RK4 physics");
        } else if (diffMethod.equalsIgnoreCase("PredictorCorrector")) {
            this.engine = new PredictorCorrector(this.course);
            System.out.println("Setting up Predictor-Corrector physics");
        } else {
            this.engine = new RK4(this.course);
            System.out.println("Setting up RK4 physics by DEFAULT...");
        }
    }

}
