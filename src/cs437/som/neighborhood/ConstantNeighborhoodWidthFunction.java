package cs437.som.neighborhood;

import cs437.som.NeightborhoodWidthFunction;

/**
 * Constant neighborhood strategy.
 */
public class ConstantNeighborhoodWidthFunction implements NeightborhoodWidthFunction {
    private final double neighborhoodWidth;

    public ConstantNeighborhoodWidthFunction(double width) {
        neighborhoodWidth = width;
    }

    public void setExpectedIterations(int expectedIterations) {
    }

    public double neighborhoodWidth(int iteration) {
        return neighborhoodWidth;
    }

    @Override
    public String toString() {
        return "ConstantNeighborhoodWidthFunction";
    }
}
