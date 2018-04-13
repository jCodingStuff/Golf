package com.group.golf.listeners;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.group.golf.Golf;
import com.group.golf.math.Function;
import com.group.golf.screens.CourseScreen;
import com.group.golf.screens.CourseSelectorScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import java.io.FileWriter;
import com.badlogic.gdx.files.FileHandle;

/**
 * A listener to export a course into a file
 */
public class ExportListener extends ChangeListener {
	
	Screen screen;
    final Golf game;
    Course course1;
    Ball ball1;
    TextField txt;

	/**
	 * Create a new ExportListener instance
	 * @param game the Golf instance
	 * @param screen the Screen instance
	 * @param course1 the Course instance
	 * @param ball1 the Ball instance
	 * @param txt the TextField containing the name for the course to export
	 */
	public ExportListener(final Golf game, Screen screen, Course course1, Ball ball1, TextField txt) {
		this.game = game;
		this.screen = screen;
		this.ball1 = ball1;
		this.course1 = course1;
		this.txt = txt;
	}
	
	@Override
	public void changed(ChangeEvent event, Actor actor) {
		String name = this.txt.getText();
		String path = "courses/"+ name +".txt";
		if (!name.equals("") && !name.equalsIgnoreCase("Enter a file name")
				&& !Gdx.files.local(path).exists()) {
			String courseText = ((Function)this.course1.getFunction()).getName();
			courseText += "\n" + this.course1.getG();
			courseText += "\n" + this.course1.getTolerance();
			courseText += "\n" + this.course1.getMu();
			courseText += "\n" + this.course1.getStart()[0] + " " + this.course1.getStart()[1];
			courseText += "\n" + this.course1.getGoal()[0] + " " + this.course1.getGoal()[1];
			courseText += "\n" + this.course1.getVmax();
			courseText += "\n" + this.ball1.getMass();
			
			FileHandle file = Gdx.files.local(path);
			file.writeString(courseText, true); // True means append, false means overwrite.
			
			this.game.setScreen(new CourseSelectorScreen(this.game));
			this.screen.dispose();
		}
	}
}

