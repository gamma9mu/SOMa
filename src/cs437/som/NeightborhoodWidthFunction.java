package cs437.som;

/**
 * Neighborhood width strategy for self-organizing maps.
 */
public interface NeightborhoodWidthFunction {

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
     * Return the neighborhood width for a given iteration.
     *
     * @param iteration The SOM's current iteration.
     * @return The neighborhood width the SOM should use for the current
     * iteration.
     */
    double neighborhoodWidth(int iteration);
}
