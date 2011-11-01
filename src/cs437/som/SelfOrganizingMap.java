package cs437.som;

/**
 * Interface for handling differing self-organizing uniformly.
 */
public interface SelfOrganizingMap {
    /**
     * Get the expected iteration count for map training.  This value should be passed
     * to the map upon construction and is used for scaling functions during training.
     * 
     * @return The expected number of iterations for map training.
     */
    public int getExpectedIterations();

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
     * Find a specific neuron's input component weight.
     *
     * @param neuron The neuron's index.
     * @param weightIndex The component's index in an input vector.
     * @return The weight corresponding to the neuron and input index.
     */
    double getWeight(int neuron, int weightIndex);

    /**
     * Find the best matching neuron (BMU, for Best Matching Unit).  The BMU is the neuron
     * who's weights most closely match the input vector.
     * 
     * @param input The input vector to match neurons to.
     * @return The index of the neuron closest to input.
     */
    int getBestMatchingNeuron(double[] input);

    /**
     * Find the best matching neuron (BMU, for Best Matching Unit). Integer convenience method.
     *
     * @param input The input vector to match neurons to.
     * @return The index of the neuron closest to input.
     */
    int getBestMatchingNeuron(int[] input);

    /**
     * Train the map with a vector.
     *
     * @param data The vector to train with.
     */
    void trainWith(double[] data);

    /**
     * Train the map with a vector. Integer convenience method.
     *
     * @param data The vector to train with.
     */
    void trainWith(int[] data);
}
