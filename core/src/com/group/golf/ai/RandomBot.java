package com.group.golf.ai;

import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.ai.Bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 * Created by kim on 04.05.2018.
 */

public class RandomBot implements Bot{


    private Ball ball;
    private Physics engine;
    private Collision collision;
    private final Course course;
    private Random rand;
    double MAXFORCE = 100.0;
    int numGuesses = 2;
    double forceX = 0;
    double forceY = 0;
    int counter = 0;

/**
 * Create a new RandomBot instance
 * @param ball the BALL instance
 */

    public RandomBot(Course course, Ball ball){
        this.ball = ball;
        this.rand = new Random();
        this.course = course;
    }
    @Override
    public void setCollision(Collision collision){
        this.collision = collision;
    }
    @Override
    public void setPhysics(Physics physics){
            this.engine = physics;
    }
    private double GetRandomForce(double maximum){
        return this.rand.nextDouble() * maximum;
    }

    /**
     * generates random options and searches for the best out of 10 guesses
     * @param goal the Best choice sofar
     */

    private double GetBestRandomChoice(double goal) {
        double closest = Double.POSITIVE_INFINITY;
        double choice;
        for (int i = 0; i < numGuesses; i++){
            choice = this.rand.nextDouble() * MAXFORCE;

            // choice closer than goal
            if (Math.abs(choice - goal) < Math.abs(closest - goal))
                closest = choice;
        }
        System.out.println(closest);
        return closest;
    }


    @Override
    public void makeMove() {

        double[] goal = this.course.getGoal();
        System.out.println(Arrays.toString(goal));
        forceX = GetBestRandomChoice(goal[0]);
        forceY = GetBestRandomChoice(goal[1]);
        while(!checkPath()){
            forceX = GetBestRandomChoice(goal[0]);
            //System.out.println(forceX);
            forceY = GetBestRandomChoice(goal[1]);
        }
        this.engine.hit(forceX, -forceY);
        counter += 1;
        System.out.print(counter);
    }

    private boolean checkPath(){
        for(double c = 0.1; c <= 1.0; c += 0.1){
            if(this.engine.getCourse().getHeight(forceX*Math.pow(c, 2), forceY*Math.pow(c, 2)) < 0)
                return false;
        }
        return true;
    }
}