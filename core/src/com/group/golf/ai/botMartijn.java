package com.group.golf.ai;


import com.badlogic.gdx.Gdx;
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
import com.group.golf.math.JVector2;


public class botMartijn implements Bot {

    //Constants for the method Score
        private static final double WATER_SCORE = 0.0; //needs adjusting by trial and error
        private static final double DISTANCE_SCORE = 0.0; //needs adjusting by trial and error
        private static final double PREVIOUS_SCORE = 0.0; //needs adjusting by trial and error
        private static final double METHOD4 = 0.0;


        private final Course course;
        private final Ball ball;
        private Physics engine;
        private Collision collision;

        private Ball virtualBall;
        private Physics virtualEngine;
        private Collision virtualCollision;

        private double bestAngle;
        private double bestForce;
        private Point3D ballCor;  //initialize
        private double maxForce; // open for adjustment
        private boolean hit = false;
        private ArrayList<Double> prevScore = new ArrayList<Double>();

        public botMartijn(Course course, Ball ball) {
            this.course = course;
            this.ball = ball;

            this.virtualBall = new Ball(ball);
            this.virtualBall.setPosition(this.course.getStart()[0], this.course.getStart()[1]);
            this.virtualEngine = new Physics(course, this.virtualBall);

            this.maxForce = this.ball.getMass() * this.course.getVmax();  //open for adjustment
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
                    double forceX = force * Math.cos(Math.toRadians(angle));
                    double forceY = force * Math.sin(Math.toRadians(angle));
                    JVector2 forceVec = new JVector2(forceX,forceY);
                    this.simulateShot(forceVec);

                    Point3D goal = new Point3D(this.virtualBall.getX(),this.virtualBall.getY()); // get the coordinates of the ball after hitting it.
                    this.ballCor = new Point3D(this.ball.getX(), this.ball.getY());
                    

                currentScore += this.waterScore(this.ballCor, goal) * WATER_SCORE;
                currentScore -= this.distanceScore(goal) * DISTANCE_SCORE;
                currentScore += this.previousScore() * PREVIOUS_SCORE;
//              currentScore += this.method4 * METHOD4;


                //update the bestScore.
                if (currentScore > bestScore) {
                    bestScore = currentScore;
                    bestAngle = angle;
                    bestForce = force;
                    this.prevScore.add(currentScore);
                    System.out.println(currentScore); //showing the score change.
                    }
                }
            }


            //make move with the correct angle and force.
            double forceX = this.bestForce * Math.cos(Math.toRadians(this.bestAngle));
            double forceY = this.bestForce * Math.sin(Math.toRadians(this.bestAngle));
            this.engine.hit(forceX,forceY);
        }

        //needs adjusting
        private double waterScore(Point3D ballPoint, Point3D goal){
            if (this.collision.isWaterBetween(ballPoint, goal))
                return Double.NEGATIVE_INFINITY;

            else {
                //figure out what you want to do with this.
                //find outer borders of the water, and see which coordinate is the closest to your goal coordinate;
                //connect a score to this.
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
            if(this.hit == true) {
                int size = this.prevScore.size() -1;
                return prevScore.get(size);
            }
            else  return 0;
        }

        private double[] Point3Dto2D(Point3D point){
            double[] p = new double[1];
            p[0] = point.getX();
            p[1] = point.getY();
            return p;
        }

        private void simulateShot(JVector2 force, JVector2 last) {
        this.virtualEngine.hit(force.getX(),force.getY());
        while (this.virtualBall.isMoving()) {
            this.virtualEngine.movement(Gdx.graphics.getDeltaTime(), false);
            this.virtualBall.limit(this.course.getVmax());
            this.virtualCollision.checkForWalls();
            if (this.virtualCollision.ballInWater()) {
                this.virtualBall.reset();
                this.virtualBall.setPosition(last.getX(), last.getY());
            }
        }
    }

        private void simulateShot(JVector2 force) {
        this.virtualEngine.hit(force.getX(),force.getY());
        while (this.virtualBall.isMoving()) {
            this.virtualEngine.movement(Gdx.graphics.getDeltaTime(), false);
            this.virtualBall.limit(this.course.getVmax());
            this.virtualCollision.checkForWalls();
        }
    }



}

