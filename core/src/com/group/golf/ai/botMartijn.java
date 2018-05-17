package com.group.golf.ai;


import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.badlogic.gdx.math.Vector2;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.math.Line2D;
import com.group.golf.math.Point3D;

import javax.sound.sampled.Line;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class botMartijn implements Bot {

    //Constants for the method Score
        private static final double WATER_SCORE = 0.0;
        private static final double DISTANCE_SCORE = 0.0;
        private static final double METHOD3 = 0.0;
        private static final double METHOD4 = 0.0;


        private final Course course;
        private final Ball ball;
        private Physics engine;
        private Collision collision;
        private double bestAngle;
        private double bestForce;
        private Point3D ballCor;  //initialize
        private Point3D maxRangeCor;
        private double maxForce = 0; // figure out what this has to be

        //private Point3D ballPoint;
        private double prevScore;

        public botMartijn(Course course, Ball ball) {
            this.course = course;
            this.ball = ball;
        }

        @Override
        public void setPhysics(Physics physics) {
            this.engine = physics;
        }

        @Override
        public void setCollision(Collision collision) {
            this.collision = collision;
        }

        @Override
        public void makeMove() {
            System.out.print("BOT");
            double bestScore = Double.NEGATIVE_INFINITY;

            double currentScore = 0.0;

            for(double angle = 0; angle <= 360; angle++) {
                for (double force = 0; force < maxForce; force += 3) {// try which number works best

                currentScore += this.waterScore(this.ballCor, this.maxRangeCor) * WATER_SCORE;
                currentScore -= this.distanceScore(maxRangeCor) * DISTANCE_SCORE;
//              currentScore += this.method3 * METHOD3; maybe add a score for the next move that can be done. add the previous score to the new score
//              currentScore += this.method4 * METHOD4;


                //update the bestScore.
                if (currentScore > bestScore) {
                    bestScore = currentScore;
                    bestAngle = angle;
                    bestForce = force;
                    this.prevScore = currentScore;
                    }
                }
            }


            //make move with the correct angle and force.
            double forceX = this.bestForce * Math.cos(this.bestAngle);
            double forceY = this.bestForce * Math.sin(this.bestAngle);
            engine.hit(forceX,forceY);
        }

        private double waterScore(Point3D ballPoint, Point3D goal){
            if (this.collision.isWaterBetween(ballPoint, goal))
                return 0;

            else {
                //figure out what you want to do with this.
                Line2D line = new Line2D(ballPoint, goal);
                return 0;
            }

        }

        private double distanceScore(Point3D endBallpoint){
           double[] goal = course.getGoal();
           double[] end  = {endBallpoint.getX(),endBallpoint.getY()};
           double distance  = Math.sqrt(Math.pow(goal[0] - end[0], 2) + Math.pow(goal[1] - end[1],2));
           return distance;
        }

        private double previousScore(){


            return 0;
        }

        private double[] Point3Dto2D(Point3D point){
            double[] p = new double[1];
            p[0] = point.getX();
            p[1] = point.getY();
            return p;
        }


}

