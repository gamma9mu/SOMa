package cs437.som;

/**
 * Interface for handling differing self-organizing maps uniformly.  This
 * interface is used for maps that will or are undergoing training.
 */
public interface TrainableSelfOrganizingMap extends SelfOrganizingMap {
    /**
     * Get the expected iteration count for map training.  This value should be
     * passed to the map upon construction and is used for scaling functions
     * during training.
     *
     * @return The expected number of iterations for map training.
     */
    int getExpectedIterations();

    /**
     * Train the map with a vector.
     *
     * @param data The vector to train with.
     */
    void trainWith(double[] data);

    /**
     * Train the map with a vector. Integer convenience method.
     *
     * @param data The vector to train with.
     */
    void trainWith(int[] data);
}
