package com.group.golf.ai;

import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.ai.Bot;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by kim on 04.05.2018.
 */

public class RandomBot implements Bot{


    private Ball ball;
    private Physics engine;
    private Random rand;
    double MAXFORCE = 100.0;
    int numGuesses = 10;
    double forceX = 0;
    double forceY = 0;

/**
 * Create a new RandomBot instance
 * @param engine the Physics instance
 * @param ball the BALL instance
 */

    public RandomBot(Physics engine, Ball ball){
        this.ball = ball;
        this.engine = engine;
        this.rand = new Random();
    }
    public void setCollision(Collision collision){

    }
    public void setPhysics(Physics physics){

    }
    private double GetRandomForce(double maximum){
        return this.rand.nextDouble() * maximum;
    }

    /**
     * generates random options and searches for the best out of 10 guesses
     * @param best the Best choice sofar
     */

    private double GetBestRandomChoice(double best) {
        double closest = Double.POSITIVE_INFINITY;
        double choice;
        for (int i = 0; i < numGuesses; i++){
            choice = this.rand.nextDouble() * MAXFORCE;
            // choice closer than best
            if (Math.abs(choice - best) < Math.abs(closest - best)) closest = choice;
        }
        return closest;
    }


    @Override
    public void makeMove() {
        while(!checkPath()){
            forceX = GetBestRandomChoice(this.engine.getCourse().getGoal()[0]);
            forceY = GetBestRandomChoice(this.engine.getCourse().getGoal()[1]);
        }
        this.engine.hit(forceX, forceY);
    }

    private boolean checkPath(){
        for(double c = 0.1; c <= 1.0; c += 0.1){
            if(this.engine.getCourse().getHeight(forceX*Math.pow(c, 2), forceY*Math.pow(c, 2)) < 0)
                return false;
        }
        return true;
    }
}