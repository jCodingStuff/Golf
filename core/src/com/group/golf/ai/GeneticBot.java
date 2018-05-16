package com.group.golf.ai;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.genetics.Individual;
import com.group.golf.math.JVector2;
import com.group.golf.math.MathLib;

import java.util.List;

/**
 * A bot that uses a genetic algorithm
 * @author Julian Marrades
 * @version 0.1, 15-05-2018
 */
public class GeneticBot implements Bot {

    private final Course course;
    private final Ball ball;
    private Physics engine;
    private Collision collision;

    private Physics virtualEngine;
    private Collision virtualCollision;

    private JVector2[] path;
    private int counter = 0;

    private static final int POPULATION_SIZE = 100;
    private static final int DNA_LENGHT = 10;
    private static final double MAX_FORCE = 100;

    private static final double GENERATION_LIMIT = 10000;

    private Individual[] population;

    /**
     * Create a new instance of GeneticBot
     * @param course the course
     * @param ball the ball
     */
    public GeneticBot(Course course, Ball ball) {
        this.course = course;
        this.ball = ball;

        this.virtualEngine = new Physics(course, ball);
        this.virtualCollision = new Collision(ball, course);

        this.startEvolution();
    }

    @Override
    public void makeMove() {
        if (this.counter < this.path.length) {
            JVector2 currentShot = this.path[this.counter];
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
        for (int i = 0; i < DNA_LENGHT; i++) {
            double forceX = MathLib.randomDouble(-MAX_FORCE, MAX_FORCE);
            double forceY = MathLib.randomDouble(-MAX_FORCE, MAX_FORCE);
            genes[i] = new JVector2(forceX, forceY);
        }
        return new Individual(genes);
    }

    /**
     * Check if some individual has reached the goal without going through water
     * @return
     */
    private boolean goalReached() {
        boolean reached = false;
        return reached;
    }

}
