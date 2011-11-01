package cs437.som.neighborhood;

import cs437.som.NeightborhoodWidthFunction;

/**
 * Neighborhood width strategy for self-organizing maps that decays the width
 * by the Gaussian function, centered at 0 and parametrized by the standard
 * deviation.
 */
public class GaussianNeighborhoodWidthFunction implements NeightborhoodWidthFunction {
    private final double stdDeviation;
    private final double coefficient;

    public GaussianNeighborhoodWidthFunction(double width) {
        stdDeviation = width;
        coefficient = 1 / (stdDeviation * Math.sqrt(2 * Math.PI));
    }

    public void setExpectedIterations(int expectedIterations) {
    }

    public double neighborhoodWidth(int iteration) {
        return coefficient *
                Math.exp((iteration * iteration) / (2 * stdDeviation * stdDeviation));
    }

    @Override
    public String toString() {
        return "GaussianNeighborhoodWidthFunction";
    }
}
