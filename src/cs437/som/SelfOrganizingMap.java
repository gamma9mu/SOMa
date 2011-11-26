package cs437.som;

import java.io.IOException;
import java.io.OutputStreamWriter;

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

    /**
     * Measure the distance from a neuron (specifically, its weight vector) to
     * an input vector.
     *
     * @param neuron The index of the neuron in question.
     * @param input The input vector.
     * @return The distance from the neuron to the vector.
     */
    double distanceToInput(int neuron, double[] input);

    /**
     * Write the self-organizing map to a stream.
     *
     * @param destination Where to write to.
     * @throws java.io.IOException If writing throws.
     */
    void write(OutputStreamWriter destination) throws IOException;
}
