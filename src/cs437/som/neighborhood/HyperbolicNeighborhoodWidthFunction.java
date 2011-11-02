package cs437.som.neighborhood;

import cs437.som.NeightborhoodWidthFunction;

/**
 * Hyperbolic neighborhood width strategy for self-organizing map.
 *
 * The exact behavior follows the formula:
 *      w_i / (t + t_max)
 *  where
 *      w_i   is the initial width of the neighborhood
 *      t     is the current iteration
 *      t_max is the maximum expected iteration
 */
public class HyperbolicNeighborhoodWidthFunction implements NeightborhoodWidthFunction {
    private double expectedIterations = 0.0;

    /**
     * {@inheritDoc}
     */
    public void setExpectedIterations(int expectedIterations) {
        this.expectedIterations = expectedIterations;
    }

    /**
     * {@inheritDoc}
     */
    public double neighborhoodWidth(int iteration) {
        return expectedIterations / (expectedIterations + iteration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "HyperbolicNeighborhoodWidthFunction";
    }
}
