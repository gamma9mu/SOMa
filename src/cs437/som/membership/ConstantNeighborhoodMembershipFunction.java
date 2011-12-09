package cs437.som.membership;

import cs437.som.NeighborhoodMembershipFunction;

/**
 * Neighborhood membership strategy for self-organizing maps. Returns 1 if the
 * neuron is within the neighborhood, 0 otherwise.
 */
public class ConstantNeighborhoodMembershipFunction implements NeighborhoodMembershipFunction {
    @Override
    public double neighborhoodMembership(double distance, double width) {
        if (distance < width)
            return 1;
        else
            return 0;
    }
}
