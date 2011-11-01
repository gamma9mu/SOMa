package cs437.som;

/**
 * Learning rate strategy for self-organizing maps.
 */
public interface LearningRateFunction {

    /**
     * expectedIterations is a write-only property that allows the learning
     * rate object to scale its value based on the number of iterations the SOM
     * will be trained through.
     * 
     * @param expectedIterations The number of iterations the containing SOM
     * expects to run through.
     */
    void setExpectedIterations(int expectedIterations);

    /**
     * Return the learning rate for a given iteration.
     *
     * @param iteration The SOM's current iteration.
     * @return The learning rate the SOM should use for the current iteration.
     */
    double learningRate(int iteration);
}
