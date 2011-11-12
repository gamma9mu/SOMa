package cs437.som.network;

import cs437.som.Dimension;
import cs437.som.SOMError;
import cs437.som.SelfOrganizingMap;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

/**
 * Common functionality for basic self-organizing maps.
 */
public abstract class NetworkBase implements SelfOrganizingMap {
    public static final double INITIAL_NEIGHBORHOOD_WIDTH = 5.0;
    public static final double alpha = 0.08;

    protected int time = 1;
    protected int neuronCount;
    protected int inputVectorSize;
    protected int expectedIterations;

    protected double[][] weightMatrix;
    protected Dimension gridSize;

    /**
     * Constructs the common functionality for SOMs.
     *
     * @param gridSize The neuron grid dimensions.
     * @param inputVectorSize The length of expected input vectors
     * @param expectedIterations The expected count of iterations for training.
     */
    protected NetworkBase(Dimension gridSize, int inputVectorSize, int expectedIterations) {
        this.inputVectorSize = inputVectorSize;
        this.expectedIterations = expectedIterations;
        this.gridSize = gridSize;
        this.neuronCount = gridSize.area;

        weightMatrix = new double[neuronCount][inputVectorSize];
        initialize();
    }

    public int getBestMatchingNeuron(double[] input) {
        checkInput(input);

        int bestMatch = 0;
        double lowestDistance2 = distanceToInput(0, input);
        for (int i = 1; i < neuronCount; i++) {
            double distance2temp = distanceToInput(i, input);
            if (distance2temp < lowestDistance2) {
                lowestDistance2 = distance2temp;
                bestMatch = i;
            }
        }
        return bestMatch;
    }

    public int getBestMatchingNeuron(int[] input) {
        double[] dbls = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            dbls[i] = input[i];
        }
        return getBestMatchingNeuron(dbls);
    }

    public int getExpectedIterations() {
        return expectedIterations;
    }

    public Dimension getGridSize() {
        return gridSize;
    }

    public int getNeuronCount() {
        return gridSize.area;
    }

    public int getInputLength() {
        return inputVectorSize;
    }

    public double getWeight(int neuron, int weightIndex) {
        return weightMatrix[neuron][weightIndex];
    }

    public void trainWith(double[] data) {
        checkInput(data);

        int best = getBestMatchingNeuron(data);
        adjustNeuronWeights(best, data);
        adjustNeighborsOf(best, data);
        time++;
    }

    public void trainWith(int[] data) {
        double[] dbls = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            dbls[i] = data[i];
        }
        trainWith(dbls);
    }

    /**
     * Create a string to display the neurons' weights.
     *
     * @return A string grid of the neurons weights (neurons left to right,
     * weights top to bottom).
     */
    public String weightString() {
        StringBuilder sb = new StringBuilder(weightMatrix.length * 3);

        for (double[] aWeightMatrix : weightMatrix) {
            sb.append(Arrays.toString(aWeightMatrix));
            sb.append(System.lineSeparator());
        }
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    /**
     * Calculate the learning rate (alpha in most equations) for the current iteration.
     * The current iteration will be taken from the object's current count
     *
     * @return The learning rate for the current iteration.
     */
    protected double learningRate() {
        return alpha;
    }

    /**
     * Verify that a given input vector's length matches the length expected by
     * the map.
     *
     * @param input The input vector to inspect.
     * @throws cs437.som.SOMError when the length of input does not match the
     * expected length.
     */
    protected void checkInput(double[] input) throws SOMError {
        if (input.length != inputVectorSize) {
            throw new SOMError("Input vector length does not match network input size.");
        }
    }

    /**
     * Initializes the neuron's weight matrix to all random doubles.
     */
    private void initialize() {
        Random r = new SecureRandom();
        for (int i = 0; i < neuronCount; i++) {
            for (int j = 0; j < inputVectorSize; j++) {
                weightMatrix[i][j] = r.nextDouble();
            }
        }
    }

    /**
     * Adjust the weights of a neuron to more closely match a given input vector.
     *
     * @param neuron The index of the neuron to adjust.
     * @param input The input vector to adjust towards.
     */
    protected void adjustNeuronWeights(int neuron, double[] input) {
        for (int i = 0; i < weightMatrix[neuron].length; i++) {
            double delta = input[i] - weightMatrix[neuron][i];
            weightMatrix[neuron][i] += learningRate() * delta;
        }
    }

    /**
     * Adjust the weights of all neurons in the neighborhood if a given neuron to
     * more closely match a given input vector.
     *
     * @param neuron The index of the neuron who's neighborhood will be examined.
     * @param input The input vector to adjust towards.
     */
    protected void adjustNeighborsOf(int neuron, double[] input) {
        for (int i = 0; i < neuronCount; i++) {
            if (i != neuron && inNeighborhoodOf(neuron, i)) {
                adjustNeuronWeights(i, input);
            }
        }
    }

    /**
     * Decide whether a neuron is in another neuron's neighborhood.  The parameters imply a specific
     * ordering, but in most cases this is an unnecessary constraint.  This would only be the case if
     * d(i, j) != d(j, i).  Asymmetric metrics are likely very uncommon, and it should be safe to
     * ignore this constraint.
     *
     * @param winningestNeuron The winning neuron who's neighborhood we are testing.
     * @param testNeuron The neuron we're testing for neighborhood inclusion.
     * @return true if the testNeuron is in the neighborhood of winningestNeuron, false otherwise
     */
    protected boolean inNeighborhoodOf(int winningestNeuron, int testNeuron) {
        return neuronDistance(winningestNeuron, testNeuron) < neighborhoodWidth();
    }

    /**
     * Get the width of the neighborhood of adjustment at a given iteration.  The iteration is
     * taken from the object's current count.
     *
     * @return The width of the neighborhood of adjustment for the current iteration.
     */
    protected double neighborhoodWidth() {
        return INITIAL_NEIGHBORHOOD_WIDTH * (1.0 - (time / (double) expectedIterations));
    }

    /**
     * Calculate the distance between 2 neurons, given the map's layout.
     *
     * @param neuron0 The index of the first neuron.
     * @param neuron1 The index of the second neuron.
     * @return The distance between the two neurons.
     */
    protected abstract double neuronDistance(int neuron0, int neuron1);

    /**
     * Measure the distance from a neuron (specifically, its weight vector) to an input vector.
     *
     * @param neuron The index of the neuron in question.
     * @param input The input vector.
     * @return The distance from the neuron to the vector.
     */
    protected double distanceToInput(int neuron, double[] input) {
        double sum = 0.0;
        for (int i = 0; i < inputVectorSize; i++) {
            double difference = input[i] - weightMatrix[neuron][i];
            sum += difference * difference;
        }
        return sum;
    }

    @Override
    public String toString() {
        return "NetworkBase{" +
                "time=" + time +
                ", dimensions=" + gridSize +
                ", inputVectorSize=" + inputVectorSize +
                ", expectedIterations=" + expectedIterations +
                ", weightMatrix=" + weightString() +
                '}';
    }
}
