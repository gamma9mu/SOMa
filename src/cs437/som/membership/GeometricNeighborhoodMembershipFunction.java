package cs437.som.membership;

import cs437.som.NeighborhoodMembershipFunction;

/**
 * Geometric neighborhood membership function.
 *
 * The membership of a neuron under this strategy follows the equation:
 * <pre>
 *     \begin{cases}
 *     \left(\frac{w - D(N, N_{bm})}{w}\right)^k & \text{if} D(N, N_{bm}) < w\\
 *     0 & \text{otherwise}
 *     \end{cases}
 * where
 *     w      is the neighborhood width
 *     N      is the neuron in question
 *     N_{bm} is the BMU
 *     k      is the order of the membership function
 *     D(x,y) is the provided distance from the neuron to the BMU
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
