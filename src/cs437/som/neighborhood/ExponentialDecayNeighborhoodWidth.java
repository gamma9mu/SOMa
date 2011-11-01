package cs437.som.neighborhood;

/**
 * Neighborhood width strategy for self-organizing maps that decays the width
 * exponentially as the iterations progress.
 */
public class ExponentialDecayNeighborhoodWidth {
    private final double initialNeighborhoodWidth;
    private double expectedIterations;

    public ExponentialDecayNeighborhoodWidth(double initialWidth) {
        initialNeighborhoodWidth = initialWidth;
    }

    public void setExpectedIterations(int expectedIterations) {
        this.expectedIterations = expectedIterations;
    }

    public double neighborhoodWidth(int iteration) {
        return initialNeighborhoodWidth * Math.exp(-iteration / expectedIterations);
    }

    @Override
    public String toString() {
        return "ExponentialDecayNeighborhoodWidth";
    }
}
