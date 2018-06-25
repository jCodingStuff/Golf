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
        errorBound = 0.027f;
    }


    int counter = 1;


    public void movement(float delta, boolean simulation) {
        Ball ball = super.getBall();

        //Bootstrapping RK4

        if (counter == 1) {
            tempCoords = hitCoord;
            prevState_2 = new state(ball.getX(),ball.getY(),ball.getVelocityX(),ball.getVelocityY());
            Physics rk4 = new RK4(getCourse());
            ((RK4) rk4).bootstrapMovement(ball,delta,simulation);
            counter++;
        } else if (counter==2) {
            prevState_1 = new state(ball.getX(), ball.getY(), ball.getVelocityX(), ball.getVelocityY());
            Physics rk4 = new RK4(getCourse());
            ((RK4) rk4).bootstrapMovement(ball,delta,simulation);
            counter++;
            hitCoord = tempCoords;

        } else {

            //Predictor three stage Adams - Bashforth
            derivative currentFunction = new derivative(ball);

            derivative previousFunction_1 = new derivative(prevState_1);

            derivative previousFunction_2 = new derivative(prevState_2);


            float[] predictorCoord = new float[]{ball.getX() + (delta/12) * (23 * currentFunction.velocityX - 16 * previousFunction_1.velocityX + 5 * previousFunction_2.velocityX),
                    ball.getY() + (delta/12) * (23 * currentFunction.velocityY - 16 * previousFunction_1.velocityY + 5 * previousFunction_2.velocityY)};
            float[] predictorVel = new float[]{ball.getVelocityX() + (delta/12) * (23 * currentFunction.accelerationX - 16 * previousFunction_1.accelerationX + 5 * previousFunction_2.accelerationX),
                    ball.getVelocityY() + (delta/12) * (23 * currentFunction.accelerationY - 16 * previousFunction_1.accelerationY + 5 * previousFunction_2.accelerationY)};


            //Corrector three stage implicit Adams - Moulton
            derivative predictedFunction = new derivative(predictorCoord,predictorVel);

            float[] correctedCoord = new float[]{ball.getX() + (delta/24) * (9 * predictedFunction.velocityX + 19 * currentFunction.velocityX - 5 * previousFunction_1.velocityX + previousFunction_2.velocityX),
                    ball.getY() + (delta/24) * (9 * predictedFunction.velocityY + 19 * currentFunction.velocityY - 5 * previousFunction_1.velocityY + previousFunction_2.velocityY)};
            float[] correctedVel = new float[]{ball.getVelocityX() + (delta/24) * (9 * predictedFunction.accelerationX + 19 * currentFunction.accelerationX - 5 * previousFunction_1.accelerationX + previousFunction_2.accelerationX),
                    ball.getVelocityY() + (delta/24) * (9 * predictedFunction.accelerationY + 19 * currentFunction.accelerationY - 5 * previousFunction_1.accelerationY + previousFunction_2.accelerationY)};


            prevState_2 = prevState_1;
            prevState_1 = new state(ball.getX(),ball.getY(),ball.getVelocityX(),ball.getVelocityY());


            ball.setVelocities(correctedVel);
            ball.setCoords(correctedCoord);

            checkLowVelocity();



            super.checkCollision(simulation);
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

        public derivative(state state) {
            float[] tempCoords = new float[]{state.positionX,state.positionY};
            float[] tempVel = new float[]{state.velocityX,state.velocityY};
            float[] acceleration = acceleration(tempCoords,tempVel);
            this.velocityX = tempVel[0];
            this.velocityY = tempVel[1];
            this.accelerationX = acceleration[0];
            this.accelerationY = acceleration[1];
        }

        public derivative(Ball ball) {
            float[] tempCoords = new float[]{ball.getX(),ball.getY()};
            float[] tempVel = new float[]{ball.getVelocityX(),ball.getVelocityY()};
            float[] acceleration = acceleration(tempCoords,tempVel);
            this.velocityX = tempVel[0];
            this.velocityY = tempVel[1];
            this.accelerationX = acceleration[0];
            this.accelerationY = acceleration[1];
        }

        public derivative(float[] coords, float[] velocities) {
            float[] acceleration = acceleration(coords,velocities);
            this.velocityX = velocities[0];
            this.velocityY = velocities[1];
            this.accelerationX = acceleration[0];
            this.accelerationY = acceleration[1];
        }
    }
}
