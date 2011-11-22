package cs437.som.network;

import cs437.som.*;
import cs437.som.distancemetrics.EuclideanDistanceMetric;
import cs437.som.learningrate.ConstantLearningRateFunction;
import cs437.som.neighborhood.ContinuousUnitNormal;
import cs437.som.neighborhood.LinearDecayNeighborhoodWidthFunction;
import cs437.som.topology.SquareGrid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * A fully customizable self-organizing map.
 */
public class CustomizableSOM extends NetworkBase {

    /**
     * The distance metric strategy being employed by the CustomizableSOM.
     */
    protected DistanceMetric distanceMetric = null;

    /**
     * The learning rate strategy being employed by the CustomizableSOM.
     */
    protected LearningRateFunction learningRate = null;

    /**
     * The neighborhood width strategy being employed by the CustomizableSOM.
     */
    protected NeighborhoodWidthFunction neighborhoodWidth = null;

    /**
     * The grid type strategy being employed by the CustomizableSOM.
     */
    protected GridType gridType = null;

    /**
     * Whether to use neighborhood membership to scale the learning rate.
     */
    protected boolean neighborhoodScaling = false;

    /**
     * Create a new CustomizableSOM.
     *
     * @param gridSize The neuron grid dimensions.
     * @param inputSize The input vector size.
     * @param expectedIterations The expected number of training iterations.
     */
    public CustomizableSOM(Dimension gridSize, int inputSize, int expectedIterations) {
        super(gridSize, inputSize, expectedIterations);

        setDistanceMetricStrategy(new EuclideanDistanceMetric());
        setLearningRateFunctionStrategy(
                new ConstantLearningRateFunction(DEFAULT_LEARNING_RATE));
        setGridTypeStrategy(new SquareGrid());

        // This calculation is based on a recommendation from Dr. Kohonen in
        // Kohonen, Teuvo, 1990: The Self-organizing Map. Proc. of the IEEE,
        // Vol. 78, 1469.
        int gridRadius = Math.min(gridSize.x, gridSize.y) / 2;
        setNeighborhoodWidthFunctionStrategy(
                new LinearDecayNeighborhoodWidthFunction(gridRadius));
    }

    /**
     * Provide a distance metric strategy object to the CustomizableSOM.
     * Ownership of {@code strategy} is transferred to the CustomizableSOM.
     *
     * @param strategy A configured DistanceMetric.
     */
    public void setDistanceMetricStrategy(DistanceMetric strategy) {
        if (time == 0) {
            distanceMetric = strategy;
        } else {
            throw new SOMError("Cannot change distance strategy after training has begun.");
        }
    }

    /**
     * Provide a learning rate strategy object to the CustomizableSOM.
     * Ownership of {@code strategy} is transferred to the CustomizableSOM.
     *
     * @param strategy A configured LearningRateFunction.
     */
    public void setLearningRateFunctionStrategy(LearningRateFunction strategy) {
        if (time == 0) {
            learningRate = strategy;
            learningRate.setExpectedIterations(expectedIterations);
        } else {
            throw new SOMError("Cannot change learning rate strategy after training has begun.");
        }
    }

    /**
     * Provide a neighborhood width strategy object to the CustomizableSOM.
     * Ownership of {@code strategy} is transferred to the CustomizableSOM.
     *
     * @param strategy A configured NeighborhoodWidthFunction.
     */
    public void setNeighborhoodWidthFunctionStrategy(NeighborhoodWidthFunction strategy) {
        if (time == 0) {
            neighborhoodWidth = strategy;
            neighborhoodWidth.setExpectedIterations(expectedIterations);
        } else {
            throw new SOMError("Cannot change neighborhood width strategy after training has begun.");
        }
    }

    /**
     * Determine if the neighborhood width strategy is being used to scale the
     * learning rate of the neurons.
     *
     * @return True if the neighborhood width is scaling the learning rate,
     * false otherwise.
     */
    public boolean isNeighborhoodScaleAdjustments() {
        return neighborhoodScaling;
    }

    /**
     * Turn on/off neighborhood scaling of the learning rate.
     *
     * @param enable true enables scaling, false disables it.
     */
    public void setNeighborhoodScaleAdjustments(boolean enable) {
        ContinuousUnitNormal unw =
                (ContinuousUnitNormal) neighborhoodWidth;
        if (unw == null && enable) { // check only needed if enable is true
            throw new SOMError(String.valueOf(neighborhoodWidth)
                    + " cannot be used to scale weight adjustments.");
        }
        neighborhoodScaling = enable;
    }

    /**
     * Determine if the current neighborhood width strategy employed by the
     * CustomizableSOM is capable of being used to scale the learning rate of
     * the neurons.
     *
     * @return True if the neighborhood width strategy offers this capability,
     * false otherwise.
     */
    public boolean canNeighborhoodScaleAdjustments() {
        return neighborhoodWidth instanceof ContinuousUnitNormal;
    }

    /**
     * Provide a grid strategy object to the CustomizableSOM. Ownership of
     * {@code strategy} is transferred to the CustomizableSOM.
     *
     * @param strategy A configured GridType.
     */
    public void setGridTypeStrategy(GridType strategy) {
        if (time == 0) {
            gridType = strategy;
            gridType.setNeuronCount(gridSize);
        } else {
            throw new SOMError("Cannot change grid type strategy after training has begun.");
        }
    }

    @Override
    public int getBestMatchingNeuron(double[] input) {
        checkInput(input);

        int bestMatch = 0;
        double lowestDistance2 = distanceMetric.distance(weightMatrix[0], input);
        for (int i = 1; i < neuronCount; i++) {
            double distance2temp = distanceMetric.distance(weightMatrix[i], input);
            if (distance2temp < lowestDistance2) {
                lowestDistance2 = distance2temp;
                bestMatch = i;
            }
        }
        return bestMatch;
    }

    @Override
    protected void adjustNeuronWeights(int neuron, double[] input) {
        for (int i = 0; i < weightMatrix[neuron].length; i++) {
            double delta = input[i] - weightMatrix[neuron][i];
            delta *= learningRate.learningRate(time);
            if (neighborhoodScaling) {
                delta *= neighborhoodWidth.neighborhoodWidth(time);
            }
            weightMatrix[neuron][i] += delta;
        }
    }

    @Override
    protected boolean inNeighborhoodOf(int bestMatchingNeuron, int testNeuron) {
        return gridType.gridDistance(bestMatchingNeuron, testNeuron)
                < neighborhoodWidth.neighborhoodWidth(time);
    }

    @Override
    protected double neuronDistance(int neuron0, int neuron1) {
        throw new UnsupportedOperationException(
                "neuronDistance not used in CustomizableSOM");
    }

    @Override
    public String toString() {
        return "CustomizableSOM{neuronCount=" + neuronCount +
                ", gridSize=" + gridSize +
                ", inputSize=" + inputVectorSize +
                ", time=" + time +
                ", expectedIterations=" + expectedIterations +
                ", distanceMetric=" + distanceMetric +
                ", learningRate=" +
                learningRate +
                ", neighborhoodWidth=" +
                neighborhoodWidth +
                '}';
    }

    @Override
    public void write(OutputStreamWriter destination) throws IOException {
        destination.write(String.format("Map type: CustomizableSOM%n"));

        destination.write(String.format("Distance metric: %s%n",
                distanceMetric));
        destination.write(String.format("Learning rate function: %s%n",
                learningRate));
        destination.write(String.format("Neighborhood width function: %s%n",
                neighborhoodWidth));
        destination.write(String.format("Grid type: %s%n",
                gridType));

        destination.write(String.format("Iterations: %d of %d%n", time,
                expectedIterations));

        super.write(destination);
        destination.flush();
    }

    /**
     * Read a CustomizableSOM from an input stream.
     *
     * @param input The stream to read from.  This stream should be passed in
     * as soon as it is known to represent a CustomizableSOM.
     * @return A CustomizableSOM as represented by the contents of
     * {@code input}.
     * @throws IOException if something fails while reading the stream.
     */
    public static CustomizableSOM read(BufferedReader input) throws IOException {
        CustomSOMFileReader sfr = new CustomSOMFileReader();
        sfr.parse(input);

        CustomizableSOM bpsom = new CustomizableSOM(
                sfr.getDimension(), sfr.getInputVectorSize(), sfr.getIterations());
        bpsom.weightMatrix = readWeightMatrix(
                input, sfr.getDimension().area, sfr.getInputVectorSize());

        if (sfr.getDistanceMetric() != null)
            bpsom.distanceMetric = sfr.getDistanceMetric();

        if (sfr.getLearningRate() != null)
            bpsom.learningRate = sfr.getLearningRate();

        if (sfr.getGridType() != null)
            bpsom.gridType = sfr.getGridType();

        if (sfr.getNeighborhoodWidth() != null)
            bpsom.neighborhoodWidth = sfr.getNeighborhoodWidth();

        bpsom.time = sfr.getTime();

        return bpsom;
    }
}
