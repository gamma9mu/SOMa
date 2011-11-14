package cs437.som.learningrate;

import cs437.som.LearningRateFunction;

/**
 * Constant learning rate strategy.
 */
public class ConstantLearningRateFunction implements LearningRateFunction {
    private final double learningRate;

    /**
     * Create a constant learning rate strategy.
     *
     * @param learningRate The value to be returned for an SOM's learning rate
     * given every iteration.
     */
    public ConstantLearningRateFunction(double learningRate) {
        this.learningRate = learningRate;
    }

    public void setExpectedIterations(int expectedIterations) {
    }

    public double learningRate(int iteration) {
        return learningRate;
    }

    @Override
    public String toString() {
        return "ConstantLearningRateFunction " + learningRate;
    }
}
