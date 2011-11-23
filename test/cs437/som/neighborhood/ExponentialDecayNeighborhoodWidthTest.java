package cs437.som.neighborhood;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ExponentialDecayNeighborhoodWidthTest {
    private static final double MAXIMUM_DIFFERENCE = 0.0000000000000001;
    private static final int ITERATIONS = 1000;
    private static final double WIDTH = 1.0;

    private ExponentialDecayNeighborhoodWidth ednwf;

    @BeforeTest
    public void setUp() {
        ednwf = new ExponentialDecayNeighborhoodWidth(WIDTH);
        ednwf.setExpectedIterations(ITERATIONS);
    }

    @Test
    public void testConsistentDecay() throws Exception {
        double last = ednwf.neighborhoodWidth(1);
        for (int i = 2; i < ITERATIONS; i += 10) {
            double current = ednwf.neighborhoodWidth(i);
            assertTrue(current < last, "Should consistently decay.");
            last = current;
        }
    }

    @Test
    public void testByDirectComparison() {
        for (int i = 0; i < ITERATIONS; i++) {
            assertEquals(ednwf.neighborhoodWidth(i),
                    Math.exp(-(double)i / ITERATIONS), MAXIMUM_DIFFERENCE);
        }
    }

    @Test
    public void testLessThanLinear() {
        assertEquals(ednwf.neighborhoodWidth(0), WIDTH, MAXIMUM_DIFFERENCE);
        for (int i = 1; i < ITERATIONS; i++) {
            assertTrue(ednwf.neighborhoodWidth(i) < WIDTH - (i / ITERATIONS),
                    "Exponential decay should be less than linear.");
        }
    }
}
