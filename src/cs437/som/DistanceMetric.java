package cs437.som;

/**
 * Distance strategy for self-organizing maps.
 */
public interface DistanceMetric {

    /**
     * Calculate the distance between 2 vectors.
     * @param v0 The first vector.
     * @param v1 The second vector.
     * @return The distance between v0 and v1.
     * @throws SOMError If the vector sizes do not match.
     */
    double distance(double[] v0, double[] v1) throws SOMError;

}
