package com.group.golf.ai;


import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.badlogic.gdx.math.Vector2;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.math.Point3D;



public class botMartijn implements Bot {


        private final Course course;
        private final Ball ball;
        private Physics engine;
        private Collision collision;
        private double bestAngle;
        private double bestForce;

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

            for(this.bestAngle = 0; bestAngle < 360; bestAngle++) {

            }
//        currentScore += this.method1 * methodConstant1;
//        currentScore += this.method2 * methodConstant2;
//        currentScore += this.method3 * methodConstant3;
//        currentScore += this.method4 * methodConstant4;

            if (currentScore > bestScore) {
                bestScore = currentScore;
//            bestAngle = angle;
//            bestForce = force;
            }
        }

        public double waterScore(int x, int y){


            return 0;
        }
}

