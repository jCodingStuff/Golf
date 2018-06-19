package com.group.golf.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Json;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.math.Computable;
import com.group.golf.math.Function;
import com.group.golf.screens.CourseScreen;
import com.group.golf.screens.ExportScreen;
import com.group.golf.screens.ImportMovesScreen;
import java.util.List;

public class ToExportListener extends ChangeListener {
	
	final Golf game;
    Screen screen;
    TextField txtFunction;
    TextField txtStartPos;
    TextField txtGoalPos;
    TextField txtRadius;
    TextField txtVMax;
    TextField txtFriction;
    TextField txtGravity;
    TextField txtBallMass;
   
    
    
    public ToExportListener(final Golf game, Screen screen, TextField txtFunction, TextField txtStartPos,
			TextField txtGoalPos, TextField txtRadius, TextField txtVMax, TextField txtFriction, TextField txtGravity,
			TextField txtBallMass) {
		super();
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
        Function function = new Function(formula);
        Computable[][] functions = new Computable[1][1];
        functions[0][0] = function;
        Course course = new Course(functions, g, mu, vmax, start, goal, tolerance);
        this.game.setScreen(new ExportScreen(this.game, course, ball));
        this.screen.dispose();
    }

}
