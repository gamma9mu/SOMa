package cs437.som.neighborhood;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class HyperbolicNeighborhoodWidthFunctionTest {
    private static final double MAXIMUM_DIFFERENCE = 0.0000000000000001;
    private static final int ITERATIONS = 1000;
    private static final double START_WIDTH = 10.0;
    private static final double END_WIDTH = 1.0;

    @Test
    public void testNeighborhoodWidth() throws Exception {
        HyperbolicNeighborhoodWidthFunction hnwf =
                new HyperbolicNeighborhoodWidthFunction(START_WIDTH, END_WIDTH);
        hnwf.setExpectedIterations(ITERATIONS);

        double last = hnwf.neighborhoodWidth(1);
        for (int i = 2; i < ITERATIONS; i += 10) {
            double current = hnwf.neighborhoodWidth(i);
            assertTrue(current < last, "Should consistently decay.");
            last = current;
        }

        assertEquals(hnwf.neighborhoodWidth(0), START_WIDTH, MAXIMUM_DIFFERENCE);
        for (int i = 1; i < ITERATIONS; i++) {
            assertTrue(hnwf.neighborhoodWidth(i) < START_WIDTH - (i / ITERATIONS),
                    "Hyperbolic function should be less than linear.");
        }

        // todo check against perfect hyperbola
    }
}
