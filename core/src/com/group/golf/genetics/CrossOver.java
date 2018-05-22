package com.group.golf.genetics;

import com.group.golf.math.JVector2;

/**
 * Interface to hold crossOver algorithms
 */
public interface CrossOver {

    /**
     * Apply a crossOver to two chains of genes
     * @param genesA the first chain
     * @param genesB the second chain
     * @return an Individual whose chain is a combination of genesA and genesB
     */
    Individual crossOver(JVector2[] genesA, JVector2[] genesB);

}
