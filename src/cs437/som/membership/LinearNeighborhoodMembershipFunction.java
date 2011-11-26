package cs437.som.membership;

import cs437.som.NeighborhoodMembershipFunction;

/**
 * Neighborhood membership strategy for self-organizing maps. Returns a scalar
 * linearly proportional to distance.
 */
public class LinearNeighborhoodMembershipFunction implements NeighborhoodMembershipFunction {
    public double neighborhoodMembership(double distance, double width) {
        if (distance < width)
            return (width - distance) / width;
        else
            return 0;
    }
}
