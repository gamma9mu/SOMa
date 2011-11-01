package cs437.som.neighborhood;

import cs437.som.NeightborhoodWidthFunction;

/**
 * Neighborhood width strategy for self-organizing maps that decays the width
 * by the so called "Mexican Hat" function (also, the Ricker wavelet or
 * negative normalized second derivative of a Gaussian), centered at 0 and
 * parametrized by the standard deviation.
 */
public class MexicanHatNeighborhoodWidthFunction implements NeightborhoodWidthFunction {
    static final double oneFourth = 0.25;
    private final double coefficient;
    private final double variance;

    public MexicanHatNeighborhoodWidthFunction(double standardDeviation) {
        coefficient = 2 / (Math.sqrt(3 * standardDeviation) * Math.pow(Math.PI, oneFourth));
        variance = standardDeviation * standardDeviation;
    }

    public void setExpectedIterations(int expectedIterations) {
    }

    public double neighborhoodWidth(int iteration) {
        double i2 = (double) iteration * iteration;
        return coefficient *
                Math.exp(-i2 / (2 * variance)) *
                (1 - (i2 / variance));
    }

    @Override
    public String toString() {
        return "MexicanHatNeighborhoodWidthFunction";
    }
}
