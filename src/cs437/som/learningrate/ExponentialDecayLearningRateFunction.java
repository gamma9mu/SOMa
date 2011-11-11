package cs437.som.learningrate;

import cs437.som.LearningRateFunction;

/**
 * Learning rate strategy for self-organizing maps that decays the learning
 * rate exponentially as the iterations progress.
 *
 * <pre>
 * The exact behavior follows the formula:
 *      \alpha_0 * e^(-t / t_{max})
 *  where
 *      \alpha_0 is the initial learning rate
 *      e        is the base of the natural logarithm
 *      t        is the current iteration
 *      t_{max}  is the maximum expected iteration
 * </pre>
 */
public class ExponentialDecayLearningRateFunction implements LearningRateFunction {
    private final double initialLearningRate;
    private double expectedIterations = 0.0;

    /**
     * Create an exponentially decaying learning rate function.
     *
     * @param initialWidth The initial learning rate.
     */
    public ExponentialDecayLearningRateFunction(double initialWidth) {
        initialLearningRate = initialWidth;
    }

    public void setExpectedIterations(int expectedIterations) {
        this.expectedIterations = expectedIterations;
    }

    public double learningRate(int iteration) {
        return initialLearningRate * Math.exp(-iteration / expectedIterations);
    }

    @Override
    public String toString() {
        return "ExponentialDecayLearningRateFunction";
    }
}
