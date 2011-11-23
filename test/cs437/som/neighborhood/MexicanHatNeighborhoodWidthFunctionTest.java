package cs437.som.neighborhood;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class MexicanHatNeighborhoodWidthFunctionTest {
    private static final int ITERATIONS = 1000;
    static final double STANDARD_DEVIATION = 100.0;
    private static final int[] SAMPLES = {0, 1, 10, 100, 200, 500, 1000};
    private static final double[] SAMPLE_RESULTS =
            {0.0867325, 0.0867195, 0.0854369, 0, -0.0352139, -7.75733e-6,
                    -1.65612e-21};
    private static final double[] SAMPLE_ACCURACY =
            {1.0e-7, 1.0e-7, 1.0e-7, 1.0e-8, 1.0e-7, 1.0e-11, 1.0e-26};
    private static final int derivativeEqualsZero =
            173; /* .2050807568877293527446341 */
    
    private MexicanHatNeighborhoodWidthFunction gnwf;

    @BeforeTest
    public void setUp() {
        gnwf = new MexicanHatNeighborhoodWidthFunction(STANDARD_DEVIATION);
        gnwf.setExpectedIterations(ITERATIONS);
    }

    @Test
    public void testAlwaysDecreasingBeforeMinimum() throws Exception {
        double last = gnwf.neighborhoodWidth(0);
        for (int i = 1; i < derivativeEqualsZero; i++) {
            double current = gnwf.neighborhoodWidth(i);
            assertTrue(current <= last,
                    "Must consistently decrease before minimum.");
            last = current;
        }
    }

    @Test
    public void testBySampling() {
        for (int j = 0; j < SAMPLES.length; j++) {
            assertEquals(gnwf.neighborhoodWidth(SAMPLES[j]), SAMPLE_RESULTS[j],
                    SAMPLE_ACCURACY[j]);
        }
    }

    @Test
    public void testAlwaysIncreasingAfterMinimum() {
        double last = gnwf.neighborhoodWidth(derivativeEqualsZero);
        for (int i = derivativeEqualsZero + 1; i < ITERATIONS; i++) {
            double current = gnwf.neighborhoodWidth(i);
            assertTrue(current >= last,
                    "Must consistently increase after minimum. " + i);
            last = current;
        }
    }
}
