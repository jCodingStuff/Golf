package com.group.golf.listeners;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.logging.FileHandler;

public class HighScoreTester {
    ScoreListener score = new ScoreListener();
    public static void main(String[] args){
        FileHandle file = Gdx.files.local("scores.txt");
    }
}
