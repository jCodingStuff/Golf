package com.group.golf.listeners;

public class HighScoreTester {
    ScoreListener score = new ScoreListener();
    public static void main(String[] args){
        ScoreListener score = new ScoreListener();
        score.addScore("Johnny", 10);
    }
}
