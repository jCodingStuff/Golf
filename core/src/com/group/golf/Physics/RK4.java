package com.group.golf.Physics;


import com.group.golf.Ball;
import com.group.golf.Course;

//https://www.haroldserrano.com/blog/visualizing-the-runge-kutta-method

/**
 * A physics engine that uses Runge-Kutta 4th order method (ODE)
 * @author Alexandros Chimonas
 */
public class RK4 extends Physics {

    /**
     * Create a new instance of RK4
     * @param course the course being played
     */
    public RK4(Course course) {
        super(course);
        errorBound = 0.05f;
    }

    //method to be overwritten by each subclass
    //RK4
    @Override
    public void movement(float delta, boolean simulation) {
        Ball ball = super.getBall();
        derivative k1,k2,k3,k4;

        k1 = new derivative(ball,delta);
        k2 = new derivative(ball,k1,delta);
        k3 = new derivative(ball,k2,delta);
        k4 = new derivative(ball,k3,delta,true);

        float[] newCoordinates = new float[] {ball.getX() + (k1.velocityX + 2 * k2.velocityX + 2 * k3.velocityX + k4.velocityX)/6,
                ball.getY() + (k1.velocityY + 2 * k2.velocityY + 2 * k3.velocityY + k4.velocityY)/6};

        float[] newVelocities = new float[]{ball.getVelocityX() + (k1.accelerationX + 2 * k2.accelerationX + 2 * k3.accelerationX + k4.accelerationX)/6,
                ball.getVelocityY() + (k1.accelerationY + 2 * k2.accelerationY + 2 * k3.accelerationY + k4.accelerationY)/6};

        ball.setVelocities(newVelocities);
        ball.setCoords(newCoordinates);

       checkLowVelocity();

        super.checkCollision(simulation);
        ball.limit(super.getCourse().getVmax());

//        System.out.println("RK4 Velocity x:     " + newVelocities[0] + "   Velocity y:     " + newVelocities[1]);

    }

    /**
     * Bootstrap for other solvers
     * @param ball the ball to handle
     * @param delta the step-size
     * @param simulation true if it is a simulation, false otherwise
     */
    public void bootstrapMovement(Ball ball,float delta, boolean simulation) {
        super.setBall(ball);
        super.getCollision().setBall(ball);
        movement(delta,simulation);
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

        public derivative(Ball ball, derivative k, float delta) {
            float[] tempCoords = new float[]{ball.getX() + k.velocityX/2, ball.getY() + k.velocityY/2};
            float[] tempVel = new float[]{ball.getVelocityX() + k.accelerationX/2,ball.getVelocityY() + k.accelerationY/2};
            float[] accelerations = acceleration(tempCoords, tempVel);
            setValues(ball,tempVel,accelerations,delta);
        }

        public derivative(Ball ball, derivative k3, float delta,boolean flag) {
            float[] tempCoords = new float[]{ball.getX() + k3.velocityX,
                    ball.getY() + k3.velocityY};
            float[] tempVel = new float[]{ball.getVelocityX() + k3.accelerationX,
                    ball.getVelocityY() + k3.accelerationY};
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
