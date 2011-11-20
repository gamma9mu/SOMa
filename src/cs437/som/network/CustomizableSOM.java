package cs437.som.network;

import cs437.som.*;
import cs437.som.distancemetrics.EuclideanDistanceMetric;
import cs437.som.learningrate.ConstantLearningRateFunction;
import cs437.som.neighborhood.ContinuousUnitNormal;
import cs437.som.neighborhood.LinearDecayNeighborhoodWidthFunction;
import cs437.som.topology.SquareGrid;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * A fully customizable self-organizing map.
 */
public class CustomizableSOM extends NetworkBase {
    static final double DEFAULT_LEARNING_RATE = 0.1;

    protected DistanceMetric distanceMetricStrategy = null;
    protected LearningRateFunction learningRateFunctionStrategy = null;
    protected NeighborhoodWidthFunction neighborhoodWidthFunctionStrategy = null;
    protected GridType gridTypeStrategy = null;

    protected boolean neighborhoodScaleAdjustments = false;

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
            distanceMetricStrategy = strategy;
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
            learningRateFunctionStrategy = strategy;
            learningRateFunctionStrategy.setExpectedIterations(expectedIterations);
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
            neighborhoodWidthFunctionStrategy = strategy;
            neighborhoodWidthFunctionStrategy.setExpectedIterations(expectedIterations);
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
        return neighborhoodScaleAdjustments;
    }

    /**
     * Turn on/off neighborhood scaling of the learning rate.
     *
     * @param enable true enables scaling, false disables it.
     */
    public void setNeighborhoodScaleAdjustments(boolean enable) {
        ContinuousUnitNormal unw =
                (ContinuousUnitNormal) neighborhoodWidthFunctionStrategy;
        if (unw == null && enable) { // check only needed if enable is true
            throw new SOMError(String.valueOf(neighborhoodWidthFunctionStrategy)
                    + " cannot be used to scale weight adjustments.");
        }
        neighborhoodScaleAdjustments = enable;
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
        return neighborhoodWidthFunctionStrategy instanceof ContinuousUnitNormal;
    }

    /**
     * Provide a grid strategy object to the CustomizableSOM. Ownership of
     * {@code strategy} is transferred to the CustomizableSOM.
     *
     * @param strategy A configured GridType.
     */
    public void setGridTypeStrategy(GridType strategy) {
        if (time == 0) {
            gridTypeStrategy = strategy;
            gridTypeStrategy.setNeuronCount(gridSize);
        } else {
            throw new SOMError("Cannot change grid type strategy after training has begun.");
        }
    }

    @Override
    public int getBestMatchingNeuron(double[] input) {
        checkInput(input);

        int bestMatch = 0;
        double lowestDistance2 = distanceMetricStrategy.distance(weightMatrix[0], input);
        for (int i = 1; i < neuronCount; i++) {
            double distance2temp = distanceMetricStrategy.distance(weightMatrix[i], input);
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
            delta *= learningRateFunctionStrategy.learningRate(time);
            if (neighborhoodScaleAdjustments) {
                delta *= neighborhoodWidthFunctionStrategy.neighborhoodWidth(time);
            }
            weightMatrix[neuron][i] += delta;
        }
    }

    @Override
    protected boolean inNeighborhoodOf(int bestMatchingNeuron, int testNeuron) {
        return gridTypeStrategy.gridDistance(bestMatchingNeuron, testNeuron)
                < neighborhoodWidthFunctionStrategy.neighborhoodWidth(time);
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
                ", distanceMetricStrategy=" + distanceMetricStrategy +
                ", learningRateFunctionStrategy=" +
                learningRateFunctionStrategy +
                ", neighborhoodWidthFunctionStrategy=" +
                neighborhoodWidthFunctionStrategy +
                '}';
    }

    @Override
    public void write(OutputStreamWriter destination) throws IOException {
        destination.write(String.format("Map type: CustomizableSOM%n"));

        destination.write(String.format("Distance metric: %s%n",
                distanceMetricStrategy));
        destination.write(String.format("Learning rate function: %s%n",
                learningRateFunctionStrategy));
        destination.write(String.format("Neighborhood width function: %s%n",
                neighborhoodWidthFunctionStrategy));
        destination.write(String.format("Grid type: %s%n",
                gridTypeStrategy));

        destination.write(String.format("Iterations: %d of %d%n", time,
                expectedIterations));

        super.write(destination);
        destination.flush();
    }
}
