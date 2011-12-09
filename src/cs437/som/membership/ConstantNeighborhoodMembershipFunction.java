package cs437.som.membership;

import cs437.som.NeighborhoodMembershipFunction;

/**
 * Neighborhood membership strategy for self-organizing maps. Returns 1 if the
 * neuron is within the neighborhood, 0 otherwise.
 */
public class ConstantNeighborhoodMembershipFunction implements NeighborhoodMembershipFunction {
    private final double value;

    public ConstantNeighborhoodMembershipFunction(double value) {
        this.value = value;
    }

    @Override
    public double neighborhoodMembership(double distance, double width) {
        if (distance < width)
            return value;
        else
            return 0;
    }


    @Override
    public String toString() {
        return "ConstantNeighborhoodMembershipFunction " + value;
    }
}
