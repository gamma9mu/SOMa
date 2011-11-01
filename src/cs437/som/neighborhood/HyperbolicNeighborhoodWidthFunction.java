package cs437.som.neighborhood;

import cs437.som.NeightborhoodWidthFunction;

/**
 * Hyperbolic neighborhood width strategy for self-organizing map.
 */
public class HyperbolicNeighborhoodWidthFunction implements NeightborhoodWidthFunction {
    private double expectedIterations;

    public void setExpectedIterations(int expectedIterations) {
        this.expectedIterations = expectedIterations;
    }

    public double neighborhoodWidth(int iteration) {
        return expectedIterations / (expectedIterations + iteration);
    }

    @Override
    public String toString() {
        return "HyperbolicNeighborhoodWidthFunction";
    }
}
