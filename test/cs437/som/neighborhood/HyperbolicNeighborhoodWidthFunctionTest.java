package cs437.som.neighborhood;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class HyperbolicNeighborhoodWidthFunctionTest {
    private static final double MAXIMUM_DIFFERENCE = 0.0000000000000001;
    private static final int ITERATIONS = 1000;
    private static final double START_WIDTH = 10.0;
    private static final double END_WIDTH = 1.0;

    private static final int[] SAMPLES = {0,100,200,500,1000};
    private static final double[] SAMPLE_RESULTS =
            {10, 7.94328, 6.30957, 3.16228, 1};
    private static final double[] SAMPLE_ACCURACY =
            {1.0e-10, 1.0e-5, 1.0e-5, 1.0e-5, 1.0e-10};

    private HyperbolicNeighborhoodWidthFunction hnwf;

    @BeforeTest
    public void setUp() {
        hnwf = new HyperbolicNeighborhoodWidthFunction(START_WIDTH, END_WIDTH);
        hnwf.setExpectedIterations(ITERATIONS);
    }

    @Test
    public void testConsistentDecay() throws Exception {
        double last = hnwf.neighborhoodWidth(1);
        for (int i = 2; i < ITERATIONS; i += 10) {
            double current = hnwf.neighborhoodWidth(i);
            assertTrue(current < last, "Should consistently decay.");
            last = current;
        }

        testBySampling();
    }

    @Test
    public void testBySampling() {
        for (int j = 0; j < SAMPLES.length; j++) {
            assertEquals(hnwf.neighborhoodWidth(SAMPLES[j]), SAMPLE_RESULTS[j],
                    SAMPLE_ACCURACY[j]);
        }
    }

    @Test
    public void testLessThanLinear() {
        assertEquals(hnwf.neighborhoodWidth(0), START_WIDTH, MAXIMUM_DIFFERENCE);
        for (int i = 1; i < ITERATIONS; i++) {
            assertTrue(hnwf.neighborhoodWidth(i) < START_WIDTH - (i / ITERATIONS),
                    "Hyperbolic function should be less than linear.");
        }
    }
}
