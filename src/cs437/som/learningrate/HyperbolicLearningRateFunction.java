package cs437.som.learningrate;

import cs437.som.NeighborhoodWidthFunction;

/**
 * Hyperbolic learning rate strategy.
 *
 * <pre>
 * The exact behavior follows the formula:
 *      \alpha_0 \cdot (\frac{\alpha_{final}}{\alpha_0})^\frac{t}{t_{max}}
 *  where
 *      \alpha_0       is the learning rate at the first iteration
 *      \alpha_{final} is the learning rate at the last iteration
 *      t              is the current iteration
 *      t_{max}        is the maximum expected iteration
 * </pre>
 */
public class HyperbolicLearningRateFunction implements NeighborhoodWidthFunction {
    private double initialRate;
    private double finalRate;
    private double expectedIterations = 0.0;

    /**
     * Create a hyperbolic learning rate strategy.
     *
     * @param initialRate The learning rate at the starting iteration.
     * @param finalRate The learning rate at the final iteration.
     */
    public HyperbolicLearningRateFunction(double initialRate, double finalRate) {
        this.initialRate = initialRate;
        this.finalRate = finalRate;
    }

    public void setExpectedIterations(int expectedIterations) {
        this.expectedIterations = expectedIterations;
    }

    public double neighborhoodWidth(int iteration) {
        return initialRate * Math.pow((finalRate / initialRate),
                (iteration / expectedIterations));
    }

    @Override
    public String toString() {
        return "HyperbolicLearningRateFunction";
    }
}
