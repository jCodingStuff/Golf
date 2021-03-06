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
 * A bot to solve simple courses
 * @author Kim Roggenbuck
 * @author Lillian Wush
 */

public class RandomBot implements Bot{


    private Ball ball;
    private Physics engine;
    private final Course course;
    private Random rand;

    float MAXFORCE = 4;

    float forceX = 0;
    float forceY = 0;

    int counter = 0;

    /**
     * Create a new RandomBot instance
     * @param course the current course in the game
     * @param ball the ball which has to be operated
     */
    public RandomBot(Course course, Ball ball){
        this.ball = ball;
        this.rand = new Random();
        this.course = course;
    }


    @Override
    public void setPhysics(Physics physics){
            this.engine = physics;
    }

    /**
     * Get a random force withing a maximum
     * @param maximum the maximum limit
     * @return a random number between 0 and maximum
     */
    private double GetRandomForce(double maximum){
        return this.rand.nextDouble() * maximum;
    }

    /**
     * generates random options and searches for the best out of 10 guesses
     * @param cur
     * @param goal the Best choice so far
     */
    private float GetBestRandomChoice(double cur, double goal) {

        float choice = (float)(Math.random()* MAXFORCE);


        System.out.println(choice);
        if(goal-cur<0)
            choice = choice*-1;
       

        return choice;
    }


    /**
     * Get the best choice and perform it
     */
    @Override
    public void makeMove() {

        float[] goal = this.course.getGoal();
        System.out.println(Arrays.toString(goal));
        forceX = GetBestRandomChoice(this.ball.getX(),goal[0]);
        forceY = GetBestRandomChoice(this.ball.getY(),goal[1]);
       /* while(!checkPath()){
            forceX = GetBestRandomChoice(this.ball.getX(),goal[0]);
            //System.out.println(forceX);
            forceY = GetBestRandomChoice(this.ball.getY(),goal[1]);
        }
*/
        this.engine.hit(ball, forceX, forceY);

        counter += 1;
        System.out.print(counter);
    }

    /**
     * Check if a certain path does not pass over water
     * @return true if is does not intersect with water, false otherwise
     */
    private boolean checkPath(){
        for(double c = 0.1; c <= 1.0; c += 0.1){
            if(this.engine.getCourse().getHeight(forceX*(float)Math.pow(c, 2), forceY*(float)Math.pow(c, 2)) < 0)
                return false;
        }
        return true;
    }
}