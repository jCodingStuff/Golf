package com.group.golf.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.group.golf.Ball;
import com.group.golf.Course;
import com.group.golf.Golf;
import com.group.golf.Physics.Collision;
import com.group.golf.Physics.Physics;
import com.group.golf.genetics.*;
import com.group.golf.math.JVector2;
import com.group.golf.math.MathLib;

/**
 * A bot that uses a genetic algorithm
 * @author Julian Marrades
 */
public class GeneticBot implements Bot {

    private final Course course;
    private Physics engine;

    private Ball realBall;

    private Ball virtualBall;

    private static final float MIN_ERROR = 0.5f, MAX_ERROR = 1.5f;



    private int counter;

    private static final int POPULATION_SIZE = 200;
    private static final int DNA_LENGTH = 15;

    private static final int GENERATION_LIMIT = 700;
    private static final double MUTATION_RATE = 0.01;
    private static float error;

    private Individual[] population;
    private Individual winner;
    private float componentVMax;


    // Algorithms
    private CrossOver crossOver;
    private Mutation mutation;
    private ScoreComputer computer;
    private GoalChecker checker;

    /**
     * Create a new instance of GeneticBot
     * @param course the course
     * @param ball the original ball
     */
    public GeneticBot(Course course, Ball ball) {
        this.counter = 0;
        this.course = course;
        this.componentVMax = this.course.getVmax();

        this.realBall = ball;
        this.virtualBall = new Ball(ball);
        this.virtualBall.setPosition(this.course.getStart()[0], this.course.getStart()[1]);

        this.crossOver = new AverageCrossOver(this);
        this.mutation = new AlterMutation();
        this.computer = new WallScoreComputer();
        this.checker = new MazeChecker(this);
    }

    /**
     * Perform the moves of the winner of the evolution
     */
    @Override
    public void makeMove() {
        if (this.counter < this.winner.getGenes().length) {
            while (this.counter < this.winner.getLandings().length &&
                    this.winner.getLandings()[this.counter+1].equals(this.winner.getLandings()[this.counter])) {
                this.counter++;
            }
            JVector2 currentShot = this.winner.getGenes()[this.counter];
            System.out.println("Hitting force: " + currentShot.getX() + " " + currentShot.getY());
            this.engine.hit(realBall, currentShot.getX(), currentShot.getY());
            this.counter++;
        }
    }

    /**
     * Set the physics instance and start the evolution
     * @param physics the new physics
     */
    @Override
    public void setPhysics(Physics physics) {
        this.engine = physics;
        this.startEvolution();
//        this.virtualEngine = new Physics(physics);
//        this.virtualEngine.setBall(this.virtualBall);
    }

//    @Override
//    public void setCollision(Collision collision) {
//        this.collision = collision;
//        this.virtualCollision = new Collision(collision);
//        this.virtualCollision.setBall(this.virtualEngine.getBall());
//        this.virtualEngine.setCollision(this.virtualCollision);
//        this.startEvolution();
//    }

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
            if (this.checker.goalReached(this.course.getGoal(), this.population, this.virtualBall,
                    this.engine.getCollision())) {
//                System.out.println("Generations: " + generations);
                reached = true;
                break;
            } else if (gCounter >= GENERATION_LIMIT) {
//                System.out.println("Generations: " + generations);
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
            float forceX = MathLib.randomFloat(-this.componentVMax, this.componentVMax);
            float forceY = MathLib.randomFloat(-this.componentVMax, this.componentVMax);
            // Small error to make it challenging
            genes[i] = new JVector2(forceX, forceY);
            genes[i].multiply(this.getRandomError());
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
     * Fill landing spots
     * @param forces the array of forces to apply
     * @param landings the array to fill using the landing spots
     */
    public void fillLandings(JVector2[] forces, JVector2[] landings) {
        this.virtualBall.reset();
        this.virtualBall.setPosition(landings[0].getX(), landings[0].getY());
        for (int i = 1; i < landings.length; i++) {
            error = 1;
            this.simulateShot(forces[i-1]);
            landings[i] = new JVector2(this.virtualBall.getX(), this.virtualBall.getY());
//            landings[i].multiply(error);
        }
    }

    /**
     * Simulate a shot
     * @param force the force to apply
     */
    private void simulateShot(JVector2 force) {
        this.engine.hit(virtualBall,force.getX(), force.getY());
        while (this.virtualBall.isMoving()) {
            this.engine.movement(Golf.DELTA, true);
//            if (this.engine.isWater()) {
//                this.virtualBall.setX(this.engine.getHitCoord()[0]);
//                this.virtualBall.setY(this.engine.getHitCoord()[1]);
//            }
        }
    }


    // GETTER AND SETTER AREA

    /**
     * Get access to the course being played
     * @return the current course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Get access to the actual winner
     * @return the individual instance who has the best score (or achieved the goal) at the very moment
     */
    public Individual getWinner() {
        return winner;
    }

    /**
     * Set a new winner
     * @param winner the new individual instance representing the winner
     */
    public void setWinner(Individual winner) {
        this.winner = winner;
    }

    public float getRandomError() {
        return MathLib.randomFloat(MIN_ERROR, MAX_ERROR);
    }
}
