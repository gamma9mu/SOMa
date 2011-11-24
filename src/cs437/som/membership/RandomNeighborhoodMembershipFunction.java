package cs437.som.membership;

import cs437.som.NeighborhoodMembershipFunction;

import java.util.Random;

public class RandomNeighborhoodMembershipFunction implements NeighborhoodMembershipFunction {
    Random r;

    public RandomNeighborhoodMembershipFunction() {
        r = new Random();
    }

    public double neighborhoodMembership(double distance, double width) {
        if (distance < width)
            return r.nextDouble();
        else
            return 0;
    }
}
