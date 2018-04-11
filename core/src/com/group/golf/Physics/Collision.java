package com.group.golf.Physics;

import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.math.Function;
import com.group.golf.screens.CourseSelectorScreen;

public class Collision {

    private final Ball ball;
    private final Course course;

    private double lastX;
    private double lastY;

    public Collision(Ball ball, Course course) {
        this.ball = ball;
        this.course = course;
        this.lastX = this.course.getStart()[0];
        this.lastX = this.course.getStart()[1];
    }

    public boolean isGoalAchieved() {
        double xToGoal = this.course.getGoal()[0] - this.ball.getX();
        double yToGoal = this.course.getGoal()[1] - this.ball.getY();
        double distToGoal = Math.sqrt(Math.pow(xToGoal, 2) + Math.pow(yToGoal, 2));
        return distToGoal < this.course.getTolerance();
    }

    public void checkForWalls(double ballX, double ballY) {
        if (ballX < 0 || ballX > Golf.VIRTUAL_WIDTH) {
            this.ball.setVelocityX(-this.ball.getVelocityX());

        }
        if (ballY < 0 || ballY > Golf.VIRTUAL_HEIGHT) {
            this.ball.setVelocityY(-this.ball.getVelocityY());
        }
    }

    public boolean ballInWater() {
        return this.course.getHeight(this.ball.getX(), this.ball.getY()) < 0;
    }

}
