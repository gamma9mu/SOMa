package cs437.som.neighborhood;

import cs437.som.NeightborhoodWidthFunction;

/**
 * Neighborhood width strategy for self-organizing maps that decays the width
 * linearly as the iterations progress.
 */
public class LinearDecayNeighborhoodWidthFunction implements NeightborhoodWidthFunction {
    private final double initialNeighborhoodWidth;
    private double expectedIterations;

    public LinearDecayNeighborhoodWidthFunction(double initialWidth) {
        initialNeighborhoodWidth = initialWidth;
    }

    public void setExpectedIterations(int expectedIterations) {
        this.expectedIterations = expectedIterations;
    }

    public double neighborhoodWidth(int iteration) {
        return initialNeighborhoodWidth * (1.0 - (iteration / expectedIterations));
    }

    @Override
    public String toString() {
        return "LinearDecayNeighborhoodWidthFunction";
    }
}
