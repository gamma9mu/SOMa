package cs437.som.membership;

import cs437.som.NeighborhoodMembershipFunction;

public class GeometricNeighborhoodMembershipFunction implements NeighborhoodMembershipFunction {
    double order;

    public GeometricNeighborhoodMembershipFunction(double order) {
        this.order = order;
    }

    public double neighborhoodMembership(double distance, double width) {
        if (distance < width)
            return Math.pow((width - distance) / width, order);
        else
            return 0;
    }
}
