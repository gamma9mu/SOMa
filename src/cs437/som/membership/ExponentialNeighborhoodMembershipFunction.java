package cs437.som.membership;

import cs437.som.NeighborhoodMembershipFunction;

/**
 * Exponential neighborhood membership strategy.
 *
 * This membership function follows the equation:
 * <pre>
 *     e^{-\frac{w - \delta}{\w}}
 * where
 *     w       is the neighborhood width
 *     \delta  is the distance from the neuron in question to the BMU
 * </pre>
 */
public class ExponentialNeighborhoodMembershipFunction implements NeighborhoodMembershipFunction {
    @Override
    public double neighborhoodMembership(double distance, double width) {
        if (distance < width)
            return Math.exp(-(width - distance) / width);
        else
            return 0;
    }
}
