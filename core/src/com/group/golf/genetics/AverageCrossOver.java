package com.group.golf.genetics;

import com.group.golf.ai.GeneticBot;
import com.group.golf.math.JVector2;
import com.group.golf.math.MathLib;

/**
 * AverageCrossOver algorithm
 */
public class AverageCrossOver implements CrossOver {

    private GeneticBot bot;

    /**
     * Generate a new instance of AverageCrossOver
     * @param bot the bot that will operate on the instance
     */
    public AverageCrossOver(GeneticBot bot) {
        this.bot = bot;
    }

    @Override
    public Individual crossOver(JVector2[] genesA, JVector2[] genesB) {
        JVector2[] newGenes = new JVector2[genesA.length];
        JVector2[] landings = new JVector2[genesA.length + 1];
        landings[0] = new JVector2(this.bot.getCourse().getStart()[0], this.bot.getCourse().getStart()[1]);

        // Average crossover
        for (int i = 0; i < newGenes.length; i++) {
            float aX = genesA[i].getX(); float aY = genesA[i].getY();
            float bX = genesB[i].getX(); float bY = genesB[i].getY();
            float x = MathLib.average(Math.abs(aX), Math.abs(bX));
            if (Math.random() < 0.5) x = -x;
            float y = MathLib.average(Math.abs(aY), Math.abs(bY));
            if (Math.random() < 0.5) y = -y;
            JVector2 gene = new JVector2(x, y);
            newGenes[i] = gene;
        }
        this.bot.fillLandings(newGenes, landings);
        return new Individual(newGenes, landings);
    }
}
