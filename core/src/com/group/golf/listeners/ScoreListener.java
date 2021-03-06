package com.group.golf.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.group.golf.Golf;

/**
 * Class that handles highscores for cooperative mode
 * @author Kim Roggenbuck
 * @author Lillian Wush
 */
public class ScoreListener {
    FileHandle scoreFile;


    public ScoreListener() {

   this.scoreFile =
            (Gdx.files.local("scores.txt"));
//        new FileHandle("scores.txt");



    }

    /**
     * Add a score the list of highscores
     * @param name name of the player/s
     * @param score final score of the game
     */
    public void addScore(String name, int score) {

//        try {
//            FileReader f = new FileReader("scores.txt");
//            BufferedReader b = new BufferedReader(f);
//            String line = b.readLine();
            String reader = scoreFile.readString();
            String[] scores = reader.split(" ");
            String modScore = Integer.toString(score);

            int position = 1;
            for (int i = 1; i < scores.length; i += 2) {
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
            System.out.println(output);
        scoreFile.writeString(output,false);

    }

}
