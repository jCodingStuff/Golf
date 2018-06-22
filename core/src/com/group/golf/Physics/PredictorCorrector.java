package com.group.golf.Physics;

import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Physics.Physics;

public class PredictorCorrector extends Physics {
    //third-stage Adams-Moulton method corrector with two - stage Adams-Bashforth predictor bootstrapped by RK4


    private state prevState_1;
    private state prevState_2;
    private float[] tempCoords;

    public PredictorCorrector(Course course) {
        super(course);
    }


    int counter = 1;

    public void movement(Ball ball, float delta) {

        //Bootstrapping RK4

        if (counter == 1) {
            tempCoords = hitCoord;
            prevState_2 = new state(ball.getX(),ball.getY(),ball.getVelocityX(),ball.getVelocityY());
            Physics rk4 = new RK4(getCourse());
            rk4.movement(ball,delta);
            counter++;
        } else if (counter==2) {
            prevState_1 = new state(ball.getX(), ball.getY(), ball.getVelocityX(), ball.getVelocityY());
            Physics rk4 = new RK4(getCourse());
            rk4.movement(ball, delta);
            counter++;
        } else {

            hitCoord = tempCoords;
            //Predictor three stage Adams - Bashforth
            float[] tempCoords = new float[]{ball.getX(),ball.getY()};
            float[] tempVel = new float[]{ball.getVelocityX(),ball.getVelocityY()};
            float[] acceleration = acceleration(tempCoords,tempVel);
            derivative currentFunction = new derivative(tempVel[0],tempVel[1],
                    acceleration[0],acceleration[1]);

            tempCoords = new float[]{prevState_1.positionX,prevState_1.positionY};
            tempVel = new float[]{prevState_1.velocityX,prevState_1.velocityY};
            acceleration = acceleration(tempCoords,tempVel);
            derivative previousFunction_1 = new derivative(tempVel[0],tempVel[1],
                    acceleration[0], acceleration[1]);

            tempCoords = new float[]{prevState_2.positionX,prevState_2.positionY};
            tempVel = new float[]{prevState_2.velocityX,prevState_2.velocityY};
            acceleration = acceleration(tempCoords,tempVel);
            derivative previousFunction_2 = new derivative(tempVel[0],tempVel[1],
                    acceleration[0], acceleration[1]);


            float[] predictorCoord = new float[]{ball.getX() + (delta/12) * (23 * currentFunction.velocityX - 16 * previousFunction_1.velocityX + 5 * previousFunction_2.velocityX),
                    ball.getY() + (delta/12) * (23 * currentFunction.velocityY - 16 * previousFunction_1.velocityY + 5 * previousFunction_2.velocityY)};
            float[] predictorVel = new float[]{ball.getVelocityX() + (delta/12) * (23 * currentFunction.accelerationX - 16 * previousFunction_1.accelerationX + 5 * previousFunction_2.accelerationX),
                    ball.getVelocityY() + (delta/12) * (23 * currentFunction.accelerationY - 16 * previousFunction_1.accelerationY + 5 * previousFunction_2.accelerationY)};

            //Corrector three stage implicit Adams - Moulton

            acceleration = acceleration(predictorCoord,predictorVel);
            derivative predictedFunction = new derivative(predictorVel[0],predictorVel[1],
                    acceleration[0],acceleration[1]);

            float[] correctedCoord = new float[]{ball.getX() + (delta/24) * (9 * predictedFunction.velocityX + 19 * currentFunction.velocityX - 5 * previousFunction_1.velocityX + previousFunction_2.velocityX),
                    ball.getY() + (delta/24) * (9 * predictedFunction.velocityY + 19 * currentFunction.velocityY - 5 * previousFunction_1.velocityY + previousFunction_2.velocityY)};
            float[] correctedVel = new float[]{ball.getVelocityX() + (delta/24) * (9 * predictedFunction.accelerationX + 19 * currentFunction.accelerationX - 5 * previousFunction_1.accelerationX + previousFunction_2.accelerationX),
                    ball.getVelocityY() + (delta/24) * (9 * predictedFunction.accelerationY + 19 * currentFunction.accelerationY - 5 * previousFunction_1.accelerationY + previousFunction_2.accelerationY)};


            prevState_2 = prevState_1;
            prevState_1 = new state(ball.getX(),ball.getY(),ball.getVelocityX(),ball.getVelocityY());

            float error = 0.1f;
            if (Math.abs(correctedVel[0] - ball.getVelocityX()) < error && Math.abs(correctedVel[1] - ball.getVelocityY()) < error) {
                ball.reset();
            } else {
                ball.setVelocities(correctedVel);
            }

            ball.setCoords(correctedCoord);

            super.checkCollision(ball);
            ball.limit(super.getCourse().getVmax());

//            System.out.println("Corrected velocities x: " + correctedVel[0] + "  y: " + correctedVel[1] );

        }
    }



    class state {
        float positionX;
        float positionY;

        float velocityX;
        float velocityY;

        public state(float positionX, float posititionY, float velocityX, float velocityY) {
            this.positionX = positionX;
            this.positionY = posititionY;

            this.velocityX = velocityX;
            this.velocityY = velocityY;
        }
    }

    class derivative {
        float velocityX;
        float velocityY;

        float accelerationX;
        float accelerationY;

        public derivative(float velocityX, float velocityY, float accelerationX, float accelerationY) {
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.accelerationX = accelerationX;
            this.accelerationY = accelerationY;
        }

        public derivative(derivative prevK, float delta) {
            this.velocityX = prevK.velocityX * delta;
            this.velocityY = prevK.velocityY * delta;
            this.accelerationX = prevK.accelerationX * delta;
            this.accelerationY = prevK.accelerationY * delta;
        }
    }
}
