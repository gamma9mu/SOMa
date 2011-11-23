package cs437.som.neighborhood;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class LinearDecayNeighborhoodWidthFunctionTest {
    private static final double MAXIMUM_DIFFERENCE = 0.0000000000000001;
    private static final double MAXIMUM_SLOPE_DIFFERENCE = 0.001;
    private static final int ITERATIONS = 1000;
    private static final double WIDTH = 1.0;

    private LinearDecayNeighborhoodWidthFunction ldnwf;

    @BeforeTest
    public void setUp() {
        ldnwf = new LinearDecayNeighborhoodWidthFunction(WIDTH);
        ldnwf.setExpectedIterations(ITERATIONS);
    }

    @Test
    public void testAlwaysDecays() throws Exception {
        double last = ldnwf.neighborhoodWidth(0);
        for (int i = 1; i < ITERATIONS; i++) {
            double current = ldnwf.neighborhoodWidth(i);
            assertTrue(current < last, "Should consistently decay.");
            last = current;
        }
    }

    @Test
    public void testMustBeLinear() {
        assertEquals(ldnwf.neighborhoodWidth(0), WIDTH, MAXIMUM_DIFFERENCE);
        for (int i = 1; i < ITERATIONS; i++) {
            assertEquals(ldnwf.neighborhoodWidth(i), WIDTH - ((double) i / ITERATIONS),
                    MAXIMUM_DIFFERENCE, "Should be exactly linear.");
        }
    }

    @Test
    public void testConstantSlope() {
        double slope = ldnwf.neighborhoodWidth(1) - ldnwf.neighborhoodWidth(0);
        for (int i = 1; i < ITERATIONS; i++) {
            double currentSlope = ldnwf.neighborhoodWidth(i + 1)
                    - ldnwf.neighborhoodWidth(i);
            assertEquals(currentSlope, slope, MAXIMUM_SLOPE_DIFFERENCE,
                    String.format("Should have constant slope. @%d", i));
        }
    }
}
