package cs437.som.neighborhood;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class LinearDecayNeighborhoodWidthFunctionTest {
    private static final double MAXIMUM_DIFFERENCE = 0.0000000000000001;
    private static final double MAXIMUM_SLOPE_DIFFERENCE = 0.001;
    private static final int ITERATIONS = 1000;
    private static final double WIDTH = 1.0;

    @Test
    public void testNeighborhoodWidth() throws Exception {
        LinearDecayNeighborhoodWidthFunction ldnwf =
                new LinearDecayNeighborhoodWidthFunction(WIDTH);
        ldnwf.setExpectedIterations(ITERATIONS);

        double last = ldnwf.neighborhoodWidth(0);
        for (int i = 1; i < ITERATIONS; i++) {
            double current = ldnwf.neighborhoodWidth(i);
            assertTrue(current < last, "Should consistently decay.");
            last = current;
        }

        double slope = ldnwf.neighborhoodWidth(1) - ldnwf.neighborhoodWidth(0);
        for (int i = 1; i < ITERATIONS; i++) {
            double currentSlope = ldnwf.neighborhoodWidth(i+1)
                    - ldnwf.neighborhoodWidth(i);
            assertEquals(currentSlope, slope, MAXIMUM_SLOPE_DIFFERENCE,
                    String.format("Should have constant slope. @%d", i));
        }

        assertEquals(ldnwf.neighborhoodWidth(0), WIDTH, MAXIMUM_DIFFERENCE);
        for (int i = 1; i < ITERATIONS; i++) {
            assertEquals(ldnwf.neighborhoodWidth(i), WIDTH - ((double) i / ITERATIONS),
                    MAXIMUM_DIFFERENCE, "Should be exactly linear.");
        }
    }
}
