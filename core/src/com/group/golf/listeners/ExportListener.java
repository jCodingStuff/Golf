package com.group.golf.listeners;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.group.golf.Golf;
import com.group.golf.screens.CourseScreen;
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

public class ExportListener extends ChangeListener {
	
	Screen screen;
    Golf game;
    Course course1;
    Ball ball1;
    TextField txt;
	
	public ExportListener(Golf game, Screen screen, Course course1, Ball ball1, TextField txt) {
		this.game = game;
		this.ball1 = ball1;
		this.course1 = course1;
		this.txt =txt;
	}
	
	@Override
	public void changed(ChangeEvent event, Actor actor) {
		// TODO Auto-generated method stub
		String name = this.txt.getText();
		String path = "courses/"+ name +".txt";
		if (name != "" && !Gdx.files.local(path).exists()) {
			String courseText = this.course1.getFunction().getName();
			courseText += "\n" + this.course1.getG();
			courseText += "\n" + this.course1.getTolerance();
			courseText += "\n" + this.course1.getMu();
			courseText += "\n" + this.course1.getStart()[0] + " " + this.course1.getStart()[1];
			courseText += "\n" + this.course1.getGoal()[0] + " " + this.course1.getGoal()[1];
			courseText += "\n" + this.course1.getVmax();
			courseText += "\n" + this.ball1.getMass();
			
			FileHandle file = Gdx.files.local(path);
			file.writeString(courseText, true); // True means append, false means overwrite.'
		}
	}
}

