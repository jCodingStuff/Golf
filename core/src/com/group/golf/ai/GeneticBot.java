package com.group.golf.ai;

import com.badlogic.gdx.math.Vector3;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.genetics.Individual;

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

    private List<double[]> path;
    private int counter = 0;

    private static final int POPULATION_SIZE = 100;
    private static final int DNA_LENGHT = 10;

    private Individual[] population;

    /**
     * Create a new instance of GeneticBot
     * @param course the course
     * @param ball the ball
     */
    public GeneticBot(Course course, Ball ball) {
        this.course = course;
        this.ball = ball;
    }

    @Override
    public void makeMove() {
        if (this.counter < this.path.size()) {
            double[] currentShot = this.path.get(this.counter);
            this.engine.hit(currentShot[0], currentShot[1]);
            this.counter++;
        }
    }

    @Override
    public void setPhysics(Physics physics) {
        this.engine = physics;
        if (this.startAllowed()) this.startEvolution();
    }

    @Override
    public void setCollision(Collision collision) {
        this.collision = collision;
        if (this.startAllowed()) this.startEvolution();
    }

    /**
     * Check if the bot is ready to work
     * @return true if engine and collision are not null, but path is
     */
    private boolean startAllowed() {
        return this.engine != null && this.collision != null && this.path == null;
    }

    /**
     * Generate the path using genetic algorithm
     */
    private void startEvolution() {
        this.initPopulation();
        // While not done, evolve
        // When done, get genes from best individual
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
        double[][] moves = new double[DNA_LENGHT][2];
    }


}
