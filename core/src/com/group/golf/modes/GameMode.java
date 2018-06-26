package com.group.golf.modes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.group.golf.Course;
import com.group.golf.Physics.*;

/**
 * An abstract class to represent a gamemode in the golf game
 * @author Julian Marrades
 * @author Alexandros Chimonas
 */
abstract public class GameMode {

    protected float[] offsets;
    protected float[] scales;

    protected Physics engine;
    protected Course course;

    /**
     * Render the gamemome-dependant objects
     * @param batch the batch responsible for drawing sprites
     */
    public abstract void render(Batch batch);

    /**
     * Look for user input or other means of ball hitting
     * @param cam the camera being used
     * @return true if the move was successful, false otherwise (goal reached)
     */
    public abstract boolean move(OrthographicCamera cam);

//    public abstract void water();

    /**
     * Perform extra-checks
     */
    public abstract void extraChecks();

    /**
     * Set a new offsets for the gamemode
     * @param offsets the new offsets
     */
    public void setOffsets(float[] offsets) {
        this.offsets = offsets;
    }

    /**
     * Set a new scales for the gamemodes
     * @param scales the new scales
     */
    public void setScales(float[] scales) {
        this.scales = scales;
    }

    /**
     * Get rid of objects when the game has ended or quit
     */
    public abstract void dispose();

    /**
     * Setup the physics for the gamemode
     * @param diffMethod the desired ODE (Euler, Verlet, RK4 or PredictorCorretor)
     */
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
