package cs437.som.membership;

import cs437.som.NeighborhoodMembershipFunction;

/**
 * Geometric neighborhood membership function.
 *
 * The membership of a neuron under this strategy follows the equation:
 * <pre>
 *     \begin{cases}
 *     \left( \frac{w - \delta}{w} \right)^k & \text{if} \delta < w \\
 *     0 & \text{otherwise}
 *     \end{cases}
 * where
 *     w      is the neighborhood width
 *     \delta is the distance from the neuron in question to the BMU
 *     k      is the order of the membership function
 * </pre>
 */
public class GeometricNeighborhoodMembershipFunction implements NeighborhoodMembershipFunction {
    private final double order;

    /**
     * Create a GeometricNeighborhoodMembershipFunction.
     *
     * @param order The exponential order of the function.
     */
    public GeometricNeighborhoodMembershipFunction(double order) {
        this.order = order;
    }

    @Override
    public double neighborhoodMembership(double distance, double width) {
        if (distance < width)
            return Math.pow((width - distance) / width, order);
        else
            return 0;
    }

    @Override
    public String toString() {
        return "GeometricNeighborhoodMembershipFunction " + order;
    }
}
