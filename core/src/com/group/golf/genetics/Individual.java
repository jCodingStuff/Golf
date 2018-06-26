package com.group.golf.genetics;

import com.group.golf.math.JVector2;

import java.util.Arrays;

/**
 * A class to represent an individual for the genetic algorithm used by GeneticBot
 * @author Julian Marrades
 */
public class Individual {

    private JVector2[] genes;
    private double score;
    private int lastMove;
    private JVector2[] landings;

    /**
     * Create a new instance of Individual
     * @param genes the set of forces
     * @param landings the set of landing coordinates after each force
     */
    public Individual(JVector2[] genes, JVector2[] landings) {
        this.genes = genes;
        this.landings = landings;
    }

    /**
     * Create a new instance of Individual using another one as a template
     * @param other the Individual template
     */
    public Individual(Individual other) {
        this.genes = duplicateData(other.genes);
        this.landings = duplicateData(other.landings);
        this.score = other.score;
        this.lastMove = other.lastMove;
    }

    /**
     * Get access to the set of forces
     * @return the set of forces
     */
    public JVector2[] getGenes() {
        return genes;
    }

    /**
     * Set a new list of forces
     * @param genes the new list of forces
     */
    public void setGenes(JVector2[] genes) {
        this.genes = genes;
    }

    /**
     * Get access to the fitness of the Individual
     * @return the fitness score
     */
    public double getScore() {
        return score;
    }

    /**
     * Set a new fitness to the Individual
     * @param score the new fitness score
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Get the index of the move that reaches the goal (or gets the closest to it)
     * @return the index of the last move
     */
    public int getLastMove() {
        return lastMove;
    }

    /**
     * Set the index of the move that reaches the goal (or gets the closest to it)
     * @param lastMove the new index of the last move
     */
    public void setLastMove(int lastMove) {
        this.lastMove = lastMove;
    }

    /**
     * Get access to the list of landing positions
     * @return the list of landings
     */
    public JVector2[] getLandings() {
        return landings;
    }

    /**
     * Set a new list of landing positions
     * @param landings the new landing spots
     */
    public void setLandings(JVector2[] landings) {
        this.landings = landings;
    }

    /**
     * Create a deep copy of an instance of JVector2
     * @param data the JVector2 instance to clone
     * @return the clone of data
     */
    private static JVector2[] duplicateData(JVector2[] data) {
        JVector2[] result = new JVector2[data.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new JVector2(data[i]);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Individual{" +
                "\n - genes=" + Arrays.toString(genes) +
                "\n - score = " + score +
                "\n - lastMove (index) = " + lastMove +
                "\n - landings=" + Arrays.toString(landings) +
                '}';
    }
}
