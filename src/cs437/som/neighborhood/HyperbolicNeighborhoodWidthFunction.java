package cs437.som.neighborhood;

import cs437.som.NeighborhoodWidthFunction;

/**
 * Hyperbolic neighborhood width strategy for self-organizing map.
 *
 * <pre>
 * The exact behavior follows the formula:
 *      w_0 \cdot (\frac{w_{final}}{w_0})^\frac{t}{t_{max}}
 *  where
 *      w_0       is the learning rate at the first iteration
 *      w_{final} is the learning rate at the last iteration
 *      t              is the current iteration
 *      t_{max}        is the maximum expected iteration
 * </pre>
 */
public class HyperbolicNeighborhoodWidthFunction
        implements NeighborhoodWidthFunction {
    private double expectedIterations = 0.0;
    private final double initialWidth;
    private final double widthRatio;
    private final double finalWidth;

    public HyperbolicNeighborhoodWidthFunction(double initialWidth,
                                               double finalWidth) {
        this.initialWidth = initialWidth;
        this.finalWidth = finalWidth;
        widthRatio = finalWidth / initialWidth;
    }

    public void setExpectedIterations(int expectedIterations) {
        this.expectedIterations = expectedIterations;
    }

    public double neighborhoodWidth(int iteration) {
        return initialWidth * Math.pow(widthRatio, (iteration / expectedIterations));
    }

    @Override
    public String toString() {
        return "HyperbolicNeighborhoodWidthFunction " + initialWidth + ' ' + finalWidth;
    }
}
