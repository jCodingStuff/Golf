package com.group.golf.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.genetics.Individual;
import com.group.golf.math.JVector2;
import com.group.golf.math.MathLib;
import sun.management.snmp.jvmmib.EnumJvmClassesVerboseLevel;

import java.util.List;

/**
 * A bot that uses a genetic algorithm
 * @author Julian Marrades
 * @version 0.1, 15-05-2018
 */
public class GeneticBot implements Bot {

    private final Course course;
    private Physics engine;
    private Collision collision;

    private Ball virtualBall;
    private Physics virtualEngine;
    private Collision virtualCollision;

    private int counter = 0;

    private static final int POPULATION_SIZE = 100;
    private static final int DNA_LENGHT = 10;
    private static final double MAX_FORCE = 100;

    private static final double GENERATION_LIMIT = 10000;

    private Individual[] population;
    private Individual winner;

    /**
     * Create a new instance of GeneticBot
     * @param course the course
     * @param ball the original ball
     */
    public GeneticBot(Course course, Ball ball) {
        this.course = course;

        this.virtualBall = new Ball(ball);
        this.resetVirtualBallToStart();

        this.virtualEngine = new Physics(course, this.virtualBall);
    }

    private void resetVirtualBallToStart() {
        this.virtualBall.setPosition(this.course.getStart()[0], this.course.getStart()[1]);
    }

    @Override
    public void makeMove() {
        if (this.counter < this.winner.getGenes().length) {
            JVector2 currentShot = this.winner.getGenes()[this.counter];
            this.engine.hit(currentShot.getX(), currentShot.getY());
            this.counter++;
        }
    }

    @Override
    public void setPhysics(Physics physics) {
        this.engine = physics;
    }

    @Override
    public void setCollision(Collision collision) {
        this.collision = collision;
        this.virtualCollision = new Collision(collision);
        this.virtualCollision.setBall(this.virtualBall);
        this.startEvolution();
    }

    /**
     * Generate the path using genetic algorithm
     */
    private void startEvolution() {
        this.initPopulation();
        int counter = 0;
        while (!this.goalReached() && this.counter < GENERATION_LIMIT) {

        }
    }

    /**
     * Create the first generation of the population
     */
    private void initPopulation() {
        this.population = new Individual[POPULATION_SIZE];
        for (int i = 0; i < this.population.length; i++) {
            this.population[i] = this.generateIndividual();
        }
    }

    /**
     * Generate a random individual
     * @return a random individual for the first population
     */
    private Individual generateIndividual() {
        JVector2[] genes = new JVector2[DNA_LENGHT];
        JVector2[] landings = new JVector2[DNA_LENGHT + 1];
        landings[0] = new JVector2(this.course.getStart()[0], this.course.getStart()[1]);
        for (int i = 0; i < DNA_LENGHT; i++) {
            double forceX = MathLib.randomDouble(-MAX_FORCE, MAX_FORCE);
            double forceY = MathLib.randomDouble(-MAX_FORCE, MAX_FORCE);
            genes[i] = new JVector2(forceX, forceY);
        }
        this.fillLandings(genes, landings);
        return new Individual(genes, landings);
    }

    /**
     * Check if some individual has reached the goal without going through water
     * @return
     */
    private boolean goalReached() {
        boolean reached = false;
        double[] goal = this.course.getGoal();
        for (int i = 0; i < this.population.length && !reached; i++) {
            JVector2 lastSpot = this.population[i].getLastSpot();
            this.virtualBall.setPosition(lastSpot.getX(), lastSpot.getY());
            if (this.virtualCollision.isGoalAchieved()) {
                reached = true;
                this.winner = this.population[i];
            }
        }
        return reached;
    }


    private void fillLandings(JVector2[] forces, JVector2[] landings) {
        this.virtualBall.setPosition(landings[0].getX(), landings[0].getY());
        for (int i = 1; i < landings.length; i++) {
            this.simulateShot(forces[i-1], landings[i-1]);
            landings[i] = new JVector2(this.virtualBall.getX(), this.virtualBall.getY());
        }
    }

    /**
     * Simulate a shot
     */
    private void simulateShot(JVector2 force, JVector2 last) {
        this.virtualEngine.hit(force.getX(), force.getY());
        while (this.virtualBall.isMoving()) {
            this.virtualEngine.movement(Gdx.graphics.getDeltaTime());
            this.virtualBall.limit(this.course.getVmax());
            if (this.virtualCollision.ballInWater()) {
                this.virtualBall.reset();
                this.virtualBall.setPosition(last.getX(), last.getY());
            }
        }
    }

}
