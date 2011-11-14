/*
 * Copyright (c) 2011 Brian A. Guthrie, Colin Murphy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *     2. Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *     3. The name of the authors may not be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHORS ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package cs437.som;

/**
 * Interface for handling differing self-organizing maps uniformly.  This
 * interface is used for maps that were previously trained.
 */
public interface SelfOrganizingMap {
    /**
     * Get the expected vector length for training the map and matching neurons.
     *
     * @return Thee exact input vector length that the map expects.
     */
    int getInputLength();

    /**
     * Get the neuron count in a map.
     *
     * @return The total number of neurons in a map, irrespective of the grid.
     */
    int getNeuronCount();

    /**
     * Get the neuron grid dimensions. (Will be changed to 2 dimensions)
     *
     * @return The dimensions of one side of a square grid.
     */
    Dimension getGridSize();

    /**
     * Find a specific neuron's input component weight.
     *
     * @param neuron The neuron's index.
     * @param weightIndex The component's index in an input vector.
     * @return The weight corresponding to the neuron and input index.
     */
    double getWeight(int neuron, int weightIndex);

    /**
     * Find the best matching neuron (BMU, for Best Matching Unit).  The BMU is
     * the neuron who's weights most closely match the input vector.
     *
     * @param input The input vector to match neurons to.
     * @return The index of the neuron closest to input.
     */
    int getBestMatchingNeuron(double[] input);

    /**
     * Find the best matching neuron (BMU, for Best Matching Unit). Integer
     * convenience method.
     *
     * @param input The input vector to match neurons to.
     * @return The index of the neuron closest to input.
     */
    int getBestMatchingNeuron(int[] input);
}
