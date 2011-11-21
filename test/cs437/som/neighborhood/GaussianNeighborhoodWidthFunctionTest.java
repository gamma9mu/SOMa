package cs437.som.neighborhood;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class GaussianNeighborhoodWidthFunctionTest {
    private static final double MAXIMUM_DIFFERENCE = 0.0000000000000001;
    static final int ITERATIONS = 1000;
    static final double STANDARD_DEVIATION = 100.0;

    @Test
    public void testNeighborhoodWidth() throws Exception {
        GaussianNeighborhoodWidthFunction gnwf =
                new GaussianNeighborhoodWidthFunction(STANDARD_DEVIATION);
        gnwf.setExpectedIterations(ITERATIONS);

        // always decreasing
        double last = gnwf.neighborhoodWidth(0);
        for (int i = 1; i < ITERATIONS; i++) {
            double current = gnwf.neighborhoodWidth(i);
            assertTrue(current <= last, "Must consistently decrease.");
            last = current;
        }

        // todo need a good way to verify a Gaussian
    }
}
