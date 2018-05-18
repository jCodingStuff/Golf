package com.group.golf.ai;

import com.badlogic.gdx.Gdx;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.genetics.Individual;
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

    private Ball virtualBall;
    private Physics virtualEngine;
    private Collision virtualCollision;

    private int counter = 0;

    private static final int POPULATION_SIZE = 100;
    private static final int DNA_LENGHT = 5;
    private static final double MAX_FORCE = 5000;

    private static final double GENERATION_LIMIT = 5;
    private static final double MUTATION_RATE = 0.01;

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
        this.virtualBall.setPosition(this.course.getStart()[0], this.course.getStart()[1]);

        this.virtualEngine = new Physics(course, this.virtualBall);
    }

    @Override
    public void makeMove() {
        if (this.counter <= this.winner.getLastMove()) {
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
        boolean reached = false;
        this.initPopulation();
        int gCounter = 0;
        while (true) {
            int generations = gCounter + 1;
            System.out.println("Generations: " + generations);
            this.computeScore();
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
            Individual child = this.averageCrossOver(indA.getGenes(), indB.getGenes());
            this.swapMutate(child);
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



    // MUTATION AREA
    /**
     * Mutate an individual swapping
     * @param ind the individual to mutate
     */
    private void swapMutate(Individual ind) {
        if (Math.random() < MUTATION_RATE) {
            int indexA = (int) (Math.random()*DNA_LENGHT);
            int indexB = (int) (Math.random()*DNA_LENGHT);
            JVector2[] genes = ind.getGenes();
            JVector2 tmp = genes[indexA];
            genes[indexA] = genes[indexB];
            genes[indexB] = tmp;
        }
    }


    // CROSSOVER AREA
    /**
     * SimpleCrossover over 2 genes chains
     * @param genesA the first chain
     * @param genesB the second chain
     * @return the individual combining both chains
     */
    private Individual simpleCrossOver(JVector2[] genesA, JVector2[] genesB) {
        JVector2[] newGenes = new JVector2[DNA_LENGHT];
        JVector2[] landings = new JVector2[DNA_LENGHT + 1];
        landings[0] = new JVector2(this.course.getStart()[0], this.course.getStart()[1]);

        int middle = (int) (Math.random()*DNA_LENGHT);

        // Simple crossover
        for (int i = 0; i <= middle; i++) {
            newGenes[i] = new JVector2(genesA[i]);
        }
        for (int i = middle + 1; i < DNA_LENGHT; i++) {
            newGenes[i] = new JVector2(genesB[i]);
        }

        this.fillLandings(newGenes, landings);
        return new Individual(newGenes, landings);
    }

    /**
     * AverageCrossover over 2 genes chains
     * @param genesA the first chain
     * @param genesB the second chain
     * @return the individual combining both chains
     */
    private Individual averageCrossOver(JVector2[] genesA, JVector2[] genesB) {
        JVector2[] newGenes = new JVector2[DNA_LENGHT];
        JVector2[] landings = new JVector2[DNA_LENGHT + 1];
        landings[0] = new JVector2(this.course.getStart()[0], this.course.getStart()[1]);

        // Average crossover
        for (int i = 0; i < newGenes.length; i++) {
            double aX = genesA[i].getX(); double aY = genesA[i].getY();
            double bX = genesB[i].getX(); double bY = genesB[i].getY();
            double x = MathLib.average(Math.abs(aX), Math.abs(bX));
            if (Math.random() < 0.5) x = -x;
            double y = MathLib.average(Math.abs(aY), Math.abs(bY));
            if (Math.random() < 0.5) y = -y;
            JVector2 gene = new JVector2(x, y);
            newGenes[i] = gene;
        }
        this.fillLandings(newGenes, landings);
        return new Individual(newGenes, landings);
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
     * Compute score for each individual of the population
     */
    private void computeScore() {
        double[] goal = this.course.getGoal();
        for (int i = 0; i < this.population.length; i++) {
            JVector2[] landings = this.population[i].getLandings();
            double closestDist = Double.MAX_VALUE;
            int closestIndex = 0;
            for (int j = 1; j < landings.length; j++) {
                double dist = JVector2.dist(landings[j].getX(), landings[j].getY(), goal[0], goal[1]);
                if (dist < closestDist) {
                    closestDist = dist;
                    closestIndex = j;
                }
            }
            this.population[i].setLastMove(closestIndex - 1);
            double score;
            if (closestIndex == 0) {
                score = 0;
            }
            else {
                score = (1/closestIndex) + (1/closestDist);
            }
            this.population[i].setScore(score);
        }
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
        double[] goal = this.course.getGoal();
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
            this.virtualBall.setPosition(lastSpot.getX(), lastSpot.getY());
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
    private void fillLandings(JVector2[] forces, JVector2[] landings) {
        this.virtualBall.reset();
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
            this.virtualEngine.movement(Gdx.graphics.getDeltaTime(), false);
            this.virtualBall.limit(this.course.getVmax());
            this.virtualCollision.checkForWalls();
            if (this.virtualCollision.ballInWater()) {
                this.virtualBall.reset();
                this.virtualBall.setPosition(last.getX(), last.getY());
            }
        }
    }

}
