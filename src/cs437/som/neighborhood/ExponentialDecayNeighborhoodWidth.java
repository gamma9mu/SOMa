package cs437.som.neighborhood;

/**
 * Neighborhood width strategy for self-organizing maps that decays the width
 * exponentially as the iterations progress.
 *
 * The exact behavior follows the formula:
 *      w_i * e^(-t / t_max)
 *  where
 *      w_i   is the initial width of the neighborhood
 *      e     is the base of the natural logarithm
 *      t     is the current iteration
 *      t_max is the maximum expected iteration
 */
public class ExponentialDecayNeighborhoodWidth {
    private final double initialNeighborhoodWidth;
    private double expectedIterations = 0.0;

    public ExponentialDecayNeighborhoodWidth(double initialWidth) {
        initialNeighborhoodWidth = initialWidth;
    }

    public void setExpectedIterations(int expectedIterations) {
        this.expectedIterations = expectedIterations;
    }

    public double neighborhoodWidth(int iteration) {
        return initialNeighborhoodWidth * Math.exp(-iteration / expectedIterations);
    }

    @Override
    public String toString() {
        return "ExponentialDecayNeighborhoodWidth";
    }
}
