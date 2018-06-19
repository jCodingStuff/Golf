package com.group.golf.ai;

import com.badlogic.gdx.Gdx;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.genetics.*;
import com.group.golf.math.JVector2;
import com.group.golf.math.MathLib;

/**
 * A bot that uses a genetic algorithm
 * @author Julian Marrades
 * @version 0.1, 15-05-2018
 */
public class GeneticBot implements Bot {

    private final Course course;
    private Physics engine;
    private Collision collision;
    private Ball realBall;

    private Ball virtualBall;
    private Physics virtualEngine;
    private Collision virtualCollision;

    private int counter = 0;

    private static final int POPULATION_SIZE = 100;
    private static final int DNA_LENGTH = 10;
    private static final double MAX_FORCE = 400;

    private static final int GENERATION_LIMIT = 5;
    private static final double MUTATION_RATE = 0.01;
    private static double error;

    private Individual[] population;
    private Individual winner;


    // Algorithms
    private CrossOver crossOver;
    private Mutation mutation;
    private ScoreComputer computer;

    /**
     * Create a new instance of GeneticBot
     * @param course the course
     * @param ball the original ball
     */
    public GeneticBot(Course course, Ball ball) {
        this.course = course;

        this.realBall = ball;
        this.virtualBall = new Ball(ball);
        this.virtualBall.setPosition(this.course.getStart()[0], this.course.getStart()[1]);

        this.crossOver = new AverageCrossOver(this);
        this.mutation = new AlterMutation();
        this.computer = new WallScoreComputer();
    }

    @Override
    public void makeMove() {
        if (this.counter <= this.winner.getLastMove()) {
            while (this.counter < this.winner.getLandings().length &&
                    this.winner.getLandings()[this.counter+1].equals(this.winner.getLandings()[this.counter])) {
                this.counter++;
            }
            JVector2 currentShot = this.winner.getGenes()[this.counter];
            System.out.println("Hitting force: " + currentShot.getX() + " " + currentShot.getY());
            this.engine.hit(realBall,(float)currentShot.getX(), (float)currentShot.getY());
            this.counter++;
        }
    }

    @Override
    public void setPhysics(Physics physics) {
        this.engine = physics;
        this.virtualEngine = new Physics(physics);
        this.virtualEngine.setBall(this.virtualBall);
    }

    @Override
    public void setCollision(Collision collision) {
        this.collision = collision;
        this.virtualCollision = new Collision(collision);
        this.virtualCollision.setBall(this.virtualEngine.getBall());
        this.virtualEngine.setCollision(this.virtualCollision);
        this.startEvolution();
    }

    /**
     * Generate the path using genetic algorithm
     */
    private void startEvolution() {
        System.out.println("Evolution starting!");
        boolean reached = false;
        this.initPopulation();
        int gCounter = 0;
        while (true) {
            int generations = gCounter + 1;
            System.out.println("Generations: " + generations);
            this.computer.compute(this.course, this.population);
            if (this.goalReached()) {
                reached = true;
                break;
            } else if (gCounter >= GENERATION_LIMIT) {
                break;
            }
            this.normalizeScore();
            this.nextGeneration();
            gCounter++;
        }
        if (!reached) System.out.println("Not able to reach goal...");
        else System.out.println("GOAAAAL!");
        System.out.println("Winner Info:");
        System.out.println(this.winner);

    }

    /**
     * Create the next generation of individuals
     */
    private void nextGeneration() {
        Individual[] newGeneration = new Individual[POPULATION_SIZE];
        for (int i = 0; i < newGeneration.length; i++) {
            Individual indA = this.pickOne();
            Individual indB = this.pickOne();
            Individual child = this.crossOver.crossOver(indA.getGenes(), indB.getGenes());
            this.mutation.mutate(child, MUTATION_RATE);
            newGeneration[i] = child;
        }
        this.population = newGeneration;
    }

    /**
     * Select one individual from population using roulette wheel
     * @return a copy of the selected individual
     */
    private Individual pickOne() {
        int index = 0;
        double r = Math.random();
        while (r > 0) {
            r -= this.population[index].getScore();
            index++;
        }
        index--;
        return new Individual(this.population[index]);
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
        JVector2[] genes = new JVector2[DNA_LENGTH];
        JVector2[] landings = new JVector2[DNA_LENGTH + 1];
        landings[0] = new JVector2(this.course.getStart()[0], this.course.getStart()[1]);
        for (int i = 0; i < DNA_LENGTH; i++) {
            double forceX = MathLib.randomDouble(-MAX_FORCE, MAX_FORCE);
            double forceY = MathLib.randomDouble(-MAX_FORCE, MAX_FORCE);
            genes[i] = new JVector2(forceX, forceY);
        }
        this.fillLandings(genes, landings);
        return new Individual(genes, landings);
    }

    /**
     * Normalize the score of the population such that in total there is only 1 point
     */
    private void normalizeScore() {
        double totalScore = 0;
        for (Individual individual : this.population) totalScore += individual.getScore();
        for (int i = 0; i < this.population.length; i++) {
            double indScore = this.population[i].getScore();
            this.population[i].setScore(indScore/totalScore);
        }
    }

    /**
     * Check if some individual has reached the goal without going through water
     * @return
     */
    private boolean goalReached() {
        boolean reached = false;
        double recordDistance = Double.MAX_VALUE;
        float[] goal = this.course.getGoal();
        for (int i = 0; i < this.population.length && !reached; i++) {

            // Get closest position to the goal for a given individual
            int index = this.population[i].getLastMove() + 1;
            JVector2 lastSpot = this.population[i].getLandings()[index];

            // Compute distance to goal and register it
            double dist = JVector2.dist(goal[0], goal[1], lastSpot.getX(), lastSpot.getY());
            if (dist < recordDistance) {
                this.winner = this.population[i];
                recordDistance = dist;
            }

            // Check if goal has been reached
            this.virtualBall.setPosition((float)lastSpot.getX(), (float)lastSpot.getY());
            if (this.virtualCollision.isGoalAchieved()) {
                System.out.println("Ending position: " + lastSpot.getX() + " " + lastSpot.getY());
                System.out.println("Goal: " + goal[0] + " " + goal[1]);
                reached = true;
            }

        }
        return reached;
    }

    /**
     * Fill landing spots
     * @param forces the array of forces to apply
     * @param landings the array to fill using the landing spots
     */
    public void fillLandings(JVector2[] forces, JVector2[] landings) {
        this.virtualBall.reset();
        this.virtualBall.setPosition((float)landings[0].getX(), (float)landings[0].getY());
        for (int i = 1; i < landings.length; i++) {
            // small random error for the force applied to the ball
            double min = 0.999;
            double max= 1.001;
            error = min+ Math.random() * (max-min);
            this.simulateShot(forces[i-1]);
            landings[i] = new JVector2(this.virtualBall.getX()*error, this.virtualBall.getY()*error);
        }
    }

    /**
     * Simulate a shot
     */
    private void simulateShot(JVector2 force) {

        // small random error for the force applied to the ball
        double min = 0.999;
        double max= 1.001;
        error = min+ Math.random() * (max-min);
        force.multiply(error);
        this.virtualEngine.hit(virtualBall,(float)force.getX(), (float)force.getY());
        while (this.virtualBall.isMoving()) {
            this.virtualEngine.movement(virtualBall,0.04f);
            if (this.virtualEngine.isWater()) {
                this.virtualBall.setX(this.engine.getHitCoord()[0]);
                this.virtualBall.setY(this.engine.getHitCoord()[1]);
            }
        }
    }


    // GETTER AND SETTER AREA
    public Course getCourse() {
        return course;
    }
}
