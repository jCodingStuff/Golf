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
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.math.Computable;
import com.group.golf.math.Function;
import com.group.golf.screens.CourseScreen;
import com.group.golf.screens.ModeScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Listener for the ImportScreen to import a course
 */
public class ImportListener extends ChangeListener {

    final Golf game;
    private TextField txtField;
    Screen screen;

    /**
     * Create a new ImportListener
     * @param game the Golf instance
     * @param txtField the TextField containing the name of the file
     * @param screen the screen where this listener was created
     */
    public ImportListener(final Golf game, TextField txtField, Screen screen) {
        this.game = game;
        this.txtField = txtField;
        this.screen = screen;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        String name = this.txtField.getText();
        String path = "courses/" + name + ".txt";
        if (!name.equals("") && !name.equalsIgnoreCase("Enter a file name")
                && Gdx.files.local(path).exists()) {
            FileHandle file = Gdx.files.local(path);
            String text = file.readString();
            Scanner in = new Scanner(text);

            String formula = in.nextLine();
            Function function = new Function(formula);

            double g = Double.parseDouble(in.nextLine());
            double tolerance = Double.parseDouble(in.nextLine());
            double mu = Double.parseDouble(in.nextLine());

            String startText = in.nextLine();
            StringTokenizer tokStart = new StringTokenizer(startText);
            double startX = Double.parseDouble(tokStart.nextToken());
            double startY = Double.parseDouble(tokStart.nextToken());
            double[] start = new double[]{startX, startY};

            String goalText = in.nextLine();
            StringTokenizer tokGoal = new StringTokenizer(goalText);
            double goalX = Double.parseDouble(tokGoal.nextToken());
            double goalY = Double.parseDouble(tokGoal.nextToken());
            double[] goal = new double[]{goalX, goalY};

            double vmax = Double.parseDouble(in.nextLine());
            double mass = Double.parseDouble(in.nextLine());

            in.close();

            Ball ball = new Ball(mass);
            Computable[][] functions = new Computable[1][1];
            functions[0][0] = function;
            Course course = new Course(functions, g, mu, vmax, start, goal, tolerance);

            this.game.setScreen(new ModeScreen(this.game, course, ball));
            this.screen.dispose();
        }
    }
}
