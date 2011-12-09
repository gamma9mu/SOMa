package cs437.som.membership;

import cs437.som.NeighborhoodMembershipFunction;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Random neighborhood membership strategy.
 *
 * Returns a random double in [0, 1] if the neuron is in the BMU's neighborhood
 * or 0 if it is not.
 */
public class RandomNeighborhoodMembershipFunction implements NeighborhoodMembershipFunction {
    private Random r = new SecureRandom();

    @Override
    public double neighborhoodMembership(double distance, double width) {
        if (distance < width)
            return r.nextDouble();
        else
            return 0;
    }
}
