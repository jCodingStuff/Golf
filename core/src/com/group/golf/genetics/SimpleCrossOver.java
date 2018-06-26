package com.group.golf.genetics;

import com.group.golf.ai.GeneticBot;
import com.group.golf.math.JVector2;

/**
 * Simple crossover algorithm
 * @auther Julian Marrades
 */
public class SimpleCrossOver implements CrossOver {

    private GeneticBot bot;

    /**
     * Generate a new instance of SimpleCrossover
     * @param bot the bot that operates this instance
     */
    public SimpleCrossOver(GeneticBot bot) {
        this.bot = bot;
    }

    /**
     * Get a random index of the dna chain. Get the forces from 0 to that index from genesA and the rest from genesB
     * @param genesA the first chain
     * @param genesB the second chain
     * @return the new individual containing the combination of the dna chains
     */
    @Override
    public Individual crossOver(JVector2[] genesA, JVector2[] genesB) {
        JVector2[] newGenes = new JVector2[genesA.length];
        JVector2[] landings = new JVector2[genesA.length + 1];
        landings[0] = new JVector2(this.bot.getCourse().getStart()[0], this.bot.getCourse().getStart()[1]);

        int middle = (int) (Math.random()*genesA.length);

        // Simple crossover
        for (int i = 0; i <= middle; i++) {
            newGenes[i] = new JVector2(genesA[i]);
        }
        for (int i = middle + 1; i < genesA.length; i++) {
            newGenes[i] = new JVector2(genesB[i]);
        }

        this.bot.fillLandings(newGenes, landings);
        return new Individual(newGenes, landings);
    }
}
