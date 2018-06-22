package com.group.golf.Physics;


import com.group.golf.Ball;
import com.group.golf.Course;

//https://www.haroldserrano.com/blog/visualizing-the-runge-kutta-method

public class RK4 extends Physics {

    public RK4(Course course) {
        super(course);
        errorBound = 0.019f;
    }

    //method to be overwritten by each subclass
    //RK4
    public void movement(Ball ball, float delta) {
        derivative k1,k2,k3,k4;

        k1 = new derivative(ball,delta);

        k2 = new derivative(ball,k1,delta);
        k3 = new derivative(ball,k1,k2,delta);
        k4 = new derivative(ball,k1,k2,k3,delta);

        float[] newCoordinates = new float[] {ball.getX() + (k1.velocityX + 3 * k2.velocityX + 3 * k3.velocityX + k4.velocityX)/8,
                ball.getY() + (k1.velocityY + 3 * k2.velocityY + 3 * k3.velocityY + k4.velocityY)/8};

        float[] newVelocities = new float[]{ball.getVelocityX() + (k1.accelerationX + 3 * k2.accelerationX + 3 * k3.accelerationX + k4.accelerationX)/8,
                ball.getVelocityY() + (k1.accelerationY + 3 * k2.accelerationY + 3 * k3.accelerationY + k4.accelerationY)/8};



        ball.setVelocities(newVelocities);
        ball.setCoords(newCoordinates);

        if (isRepeting(ball,newCoordinates)){
            ball.reset();
            repeatChecker = new float[0][2];
        }


        super.checkCollision(ball);
        ball.limit(super.getCourse().getVmax());


//        System.out.println("RK4 Velocity x:     " + newVelocities[0] + "   Velocity y:     " + newVelocities[1]);

    }

    class derivative {
        float velocityX;
        float velocityY;

        float accelerationX;
        float accelerationY;

        public derivative(Ball ball,float delta) {
            float[] tempCoords = new float[]{ball.getX(),ball.getY()};
            float[] tempVel = new float[]{ball.getVelocityX(),ball.getVelocityY()};
            float[] accelerations = acceleration(tempCoords,tempVel);
            setValues(ball,tempVel,accelerations,delta);
        }

        public derivative(Ball ball, derivative k1, float delta) {
            float[] tempCoords = new float[]{ball.getX() + k1.velocityX/3, ball.getY() + k1.velocityY/3};
            float[] tempVel = new float[]{ball.getVelocityX() + k1.accelerationX/3,ball.getVelocityY() + k1.accelerationY/3};
            float[] accelerations = acceleration(tempCoords, tempVel);
            setValues(ball,tempVel,accelerations,delta);
        }

        public derivative(Ball ball, derivative k1, derivative k2, float delta) {
            float[] tempCoords = new float[]{(ball.getX() - k1.velocityX/3 + k2.velocityX),
                    (ball.getY() - k1.velocityY/3 + k2.velocityY)};
            float[] tempVel = new float[]{ball.getVelocityX() - k1.accelerationX/3 + k2.accelerationX,
                    ball.getVelocityY() - k1.accelerationX/3 + k2.accelerationY};
            float[] accelerations = acceleration(tempCoords,tempVel);
            setValues(ball,tempVel,accelerations,delta);
        }

        public derivative(Ball ball, derivative k1, derivative k2, derivative k3, float delta) {
            float[] tempCoords = new float[]{ball.getX() + k1.velocityX - k2.velocityX + k3.velocityX,
                    ball.getY() + k1.velocityY - k2.velocityY + k3.velocityY};
            float[] tempVel = new float[]{ball.getVelocityX() + k1.accelerationX - k2.accelerationX + k3.accelerationX,
                    ball.getVelocityY() + k1.accelerationY - k2.accelerationY + k3.accelerationY};
            float[] accelerations = acceleration(tempCoords,tempVel);
            setValues(ball,tempVel,accelerations,delta);
        }

        private void setValues(Ball ball,float[] tempVel, float[] accelerations, float delta) {
            velocityX = delta * tempVel[0];
            velocityY = delta * tempVel[1];
            accelerationX = delta * accelerations[0];
            accelerationY = delta * accelerations[1];
        }
    }
}
