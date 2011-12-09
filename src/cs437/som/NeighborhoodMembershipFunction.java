package cs437.som;

/**
 * Neighborhood membership strategy interface for self-organizing maps.
 */
public interface NeighborhoodMembershipFunction {
    double neighborhoodMembership(double distance, double width);
}
