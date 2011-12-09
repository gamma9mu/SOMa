package cs437.som.membership;

import cs437.som.NeighborhoodMembershipFunction;

public class ExponentialNeighborhoodMembershipFunction implements NeighborhoodMembershipFunction {
    @Override
    public double neighborhoodMembership(double distance, double width) {
        if (distance < width)
            return Math.exp(-(width - distance) / width);
        else
            return 0;
    }
}
