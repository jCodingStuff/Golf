package com.group.golf.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
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
 * @author Kaspar Kallast
 * @author Julian Marrades
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
        List<Rectangle> walls = new ArrayList<Rectangle>();
        if (!name.equals("") && !name.equalsIgnoreCase("Enter a file name")
                && Gdx.files.local(path).exists()) {
            FileHandle file = Gdx.files.local(path);
            String text = file.readString();
            Scanner in = new Scanner(text);

            String formula = in.nextLine();
            Function function = new Function(formula);

            float g = Float.parseFloat(in.nextLine());
            float tolerance = Float.parseFloat(in.nextLine());
            float mu = Float.parseFloat(in.nextLine());

            String startText = in.nextLine();
            StringTokenizer tokStart = new StringTokenizer(startText);
            float startX = Float.parseFloat(tokStart.nextToken());
            float startY = Float.parseFloat(tokStart.nextToken());
            float[] start = new float[]{startX, startY};

            String goalText = in.nextLine();
            StringTokenizer tokGoal = new StringTokenizer(goalText);
            float goalX = Float.parseFloat(tokGoal.nextToken());
            float goalY = Float.parseFloat(tokGoal.nextToken());
            float[] goal = new float[]{goalX, goalY};

            float vmax = Float.parseFloat(in.nextLine());
            float mass = Float.parseFloat(in.nextLine());

            while (in.hasNextLine()) {
                String newRectangleData = in.nextLine();
                StringTokenizer tokWall = new StringTokenizer(newRectangleData);
                float rectX = Float.parseFloat(tokWall.nextToken());
                float rectY = Float.parseFloat(tokWall.nextToken());
                float width = Float.parseFloat(tokWall.nextToken());
                float height = Float.parseFloat(tokWall.nextToken());
                walls.add(new Rectangle(rectX, rectY, width, height));
            }

            in.close();

            Ball ball = new Ball(mass);
            Computable[][] functions = new Computable[1][1];
            functions[0][0] = function;
            Course course = new Course(functions, g, mu, vmax, start, goal, tolerance);
            if (!walls.isEmpty()) {
                course.setWalls(walls);
                System.out.println("Adding walls:");
                for (Rectangle wall : walls) System.out.println(wall);
            }

            this.game.setScreen(new ModeScreen(this.game, course, ball));
            this.screen.dispose();
        }
    }
}
