package cs437.som;

/**
 * Neighborhood membership strategy interface for self-organizing maps.
 */
public interface NeighborhoodMembershipFunction {

    /**
     * Compute a neuron's membership amount in the BMU's network.
     *
     * @param distance The distance from the neuron in question to the BMU.
     * @param width The current width of the BMU's neighborhood.
     * @return The membership rate of the neuron in the BMU's neighborhood.
     */
    double neighborhoodMembership(double distance, double width);
}
