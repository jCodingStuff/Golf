package com.group.golf.listeners;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.math.Function;
import com.group.golf.screens.CourseScreen;
import com.group.golf.screens.ImportMovesScreen;

import java.util.StringTokenizer;

public class Mode2Listener extends ChangeListener {

    Golf game;
    Screen screen;
    TextField txtFunction;
    TextField txtStartPos;
    TextField txtGoalPos;
    TextField txtRadius;
    TextField txtVMax;
    TextField txtFriction;
    TextField txtGravity;
    TextField txtBallMass;

    public Mode2Listener(Golf game, Screen screen, TextField txtFunction, TextField txtStartPos, TextField txtGoalPos,
                         TextField txtRadius, TextField txtVMax, TextField txtFriction, TextField txtGravity,
                         TextField txtBallMass) {
        this.game = game;
        this.screen = screen;
        this.txtFunction = txtFunction;
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
        String formula = this.txtFunction.getText();
        double g = Double.parseDouble(this.txtGravity.getText());
        double mu = Double.parseDouble(this.txtFriction.getText());
        double vmax = Double.parseDouble(this.txtVMax.getText());
        double tolerance = Double.parseDouble(this.txtRadius.getText());

        // Get arrays for start and goal
        StringTokenizer tokenizerStart = new StringTokenizer(this.txtStartPos.getText());
        double startX = Double.parseDouble(tokenizerStart.nextToken());
        double startY = Double.parseDouble(tokenizerStart.nextToken());
        double[] start = new double[]{startX, startY};
        StringTokenizer tokenizerGoal = new StringTokenizer(this.txtGoalPos.getText());
        double goalX = Double.parseDouble(tokenizerGoal.nextToken());
        double goalY = Double.parseDouble(tokenizerGoal.nextToken());
        double[] goal = new double[]{goalX, goalY};
        Ball ball = new Ball(Double.parseDouble(this.txtBallMass.getText()));
        Function function = new Function(formula);
        Course course = new Course(function, g, mu, vmax, start, goal, tolerance);
        this.game.setScreen(new ImportMovesScreen(this.game, course, ball));
        this.screen.dispose();
    }
}