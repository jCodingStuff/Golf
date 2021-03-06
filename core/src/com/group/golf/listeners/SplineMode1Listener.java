package com.group.golf.listeners;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.math.Computable;

import com.group.golf.screens.CourseScreen;
import com.group.golf.screens.ModeScreen;


import java.util.StringTokenizer;

/**
 * Listener to run the game entering playmode Selection
 * @author Kim Roggenbuck
 * @author Lillian Wush
 */
public class SplineMode1Listener extends ChangeListener {

    final Golf game;
    Screen screen;
    Computable[][] functions;
    TextField txtStartPos;
    TextField txtGoalPos;
    TextField txtRadius;
    TextField txtVMax;
    TextField txtFriction;
    TextField txtGravity;
    TextField txtBallMass;

    /**
     * Create a new Mode1Listener
     * @param game the Golf instance
     * @param screen the Screen instance
     * @param computable the computable array of splines
     * @param txtStartPos the TextField containing the Start Coordinates
     * @param txtGoalPos the TextField containing the Goal Coordinates
     * @param txtRadius the TextField containing the tolerance
     * @param txtVMax the TextField containing the maximum velocity
     * @param txtFriction the TextField containing the friction coefficient
     * @param txtGravity the TextField containing the gravity
     * @param txtBallMass the TextF
     */
    public SplineMode1Listener(final Golf game, Screen screen, Computable[][] computable, TextField txtStartPos, TextField txtGoalPos,
                         TextField txtRadius, TextField txtVMax, TextField txtFriction, TextField txtGravity,
                         TextField txtBallMass) {
        this.game = game;
        this.screen = screen;
        this.functions = computable;
        this.txtStartPos = txtStartPos;
        this.txtGoalPos = txtGoalPos;
        this.txtRadius = txtRadius;
        this.txtVMax = txtVMax;
        this.txtFriction = txtFriction;
        this.txtGravity = txtGravity;
        this.txtBallMass = txtBallMass;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {

        float g = Float.parseFloat(this.txtGravity.getText());
        float mu = Float.parseFloat(this.txtFriction.getText());
        float vmax = Float.parseFloat(this.txtVMax.getText());
        float tolerance = Float.parseFloat(this.txtRadius.getText());

        // Get arrays for start and goal
        StringTokenizer tokenizerStart = new StringTokenizer(this.txtStartPos.getText());
        float startX = Float.parseFloat(tokenizerStart.nextToken());
        float startY = Float.parseFloat(tokenizerStart.nextToken());
        float[] start = new float[]{startX, startY};
        StringTokenizer tokenizerGoal = new StringTokenizer(this.txtGoalPos.getText());
        float goalX = Float.parseFloat(tokenizerGoal.nextToken());
        float goalY = Float.parseFloat(tokenizerGoal.nextToken());
        float[] goal = new float[]{goalX, goalY};
        Ball ball = new Ball(Float.parseFloat(this.txtBallMass.getText()));

        Course course = new Course(functions, g, mu, vmax, start, goal, tolerance);
        this.game.setScreen(new ModeScreen(this.game, course, ball));
        this.screen.dispose();
    }
}
