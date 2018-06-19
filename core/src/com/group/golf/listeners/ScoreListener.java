package com.group.golf.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class ScoreListener {
    FileHandle scoreFile;

    public ScoreListener() {
    scoreFile =
            (Gdx.files.internal("uiskin.json"));
//        new FileHandle("scores.txt");
    }

    public void addScore(String name, int score) {
        scoreFile.getClass();

        String reader = scoreFile.readString();
        String[] scores = reader.split(" ");

        String modScore = Integer.toString(score);

        int position = 1;
        for (int i = 1; i < scores.length; i+=2) {
            position = i;
            if (modScore.compareTo(scores[i]) == -1) {
                break;
            }
        }

        String output = "";
        for (int i = 0; i < scores.length; i++) {
            output += scores[i] + " ";
             if (i == position)
                output += name + " " + modScore + " ";
        }

        scoreFile.writeString(output,false);

    }

}
