package cs437.som;

/**
 * Distance strategy interface for self-organizing maps.
 *
 * Implementations of this interface are intended to define a method of
 * determining the distance between 2 vectors.  Once an object of an
 * implementing class is given to an SOM, that SOM assumes control of that
 * object.  It should not be given to multiple SOMs or modified once it has
 * been handed to an SOM.
 * 
 */
public interface DistanceMetric {

    /**
     * Calculate the distance between 2 vectors.
     * 
     * @param v0 The first vector.
     * @param v1 The second vector.
     * @return The distance between v0 and v1.
     * @throws SOMError If the vector sizes do not match.
     */
    double distance(double[] v0, double[] v1) throws SOMError;

}
