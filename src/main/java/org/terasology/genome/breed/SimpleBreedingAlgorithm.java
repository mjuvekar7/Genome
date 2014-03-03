/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.genome.breed;

import org.terasology.genome.breed.mutator.GeneMutator;
import org.terasology.utilities.random.FastRandom;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class SimpleBreedingAlgorithm implements BreedingAlgorithm {
    private int genomeLength;
    private int minimumCrossSimilarity;
    private float mutationChance;
    private GeneMutator geneMutator;

    public SimpleBreedingAlgorithm(int genomeLength, int minimumCrossSimilarity, float mutationChance, GeneMutator geneMutator) {
        this.genomeLength = genomeLength;
        this.minimumCrossSimilarity = minimumCrossSimilarity;
        this.mutationChance = mutationChance;
        this.geneMutator = geneMutator;
    }

    @Override
    public boolean canCross(String genes1, String genes2) {
        validateGenes(genes1, genes2);

        int sameGenesCount = 0;
        char[] chars1 = genes1.toCharArray();
        char[] chars2 = genes2.toCharArray();

        for (int i = 0; i < genomeLength; i++) {
            if (chars1[i] == chars2[i]) {
                sameGenesCount++;
            }
        }

        return sameGenesCount >= minimumCrossSimilarity;
    }

    @Override
    public String produceCross(String genes1, String genes2) {
        if (!canCross(genes1, genes2)) {
            return null;
        }

        FastRandom rand = new FastRandom();

        StringBuilder result = new StringBuilder(genomeLength);

        char[] chars1 = genes1.toCharArray();
        char[] chars2 = genes2.toCharArray();

        for (int i = 0; i < genomeLength; i++) {
            result.append(rand.nextBoolean() ? chars1[i] : chars2[i]);
        }

        if (mutationChance >= rand.nextFloat()) {
            int geneIndex = rand.nextInt(genomeLength);
            char gene = geneMutator.mutateGene(rand.nextFloat(), geneIndex, result.charAt(geneIndex));
            result.replace(geneIndex, geneIndex + 1, "" + gene);
        }

        return result.toString();
    }

    private void validateGenes(String genes1, String genes2) {
        if (genes1 == null || genes2 == null || genes1.length() != genes2.length()
                || genes1.length() != genomeLength) {
            throw new IllegalArgumentException("Genomes not defined or of incorrect length");
        }
    }
}
