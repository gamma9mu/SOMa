package cs437.som.learningrate;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class HyperbolicLearningRateFunctionTest {
    private static final double MAXIMUM_DIFFERENCE = 0.0000000000000001;
    private static final int ITERATIONS = 1000;
    private static final double START_RATE = 0.8;
    private static final double END_RATE = 0.01;

    private static final int[] SAMPLES = {0,100,200,500,1000};
    private static final double[] SAMPLE_RESULTS =
            {0.8, 0.516156, 0.333021, 0.0894427, 0.01};
    private static final double[] SAMPLE_ACCURACY =
            {1.0e-1, 1.0e-6, 1.0e-6, 1.0e-7, 1.0e-2};
    
    private HyperbolicLearningRateFunction hlrf;

    @BeforeTest
    public void setUp() {
        hlrf = new HyperbolicLearningRateFunction(START_RATE, END_RATE);
        hlrf.setExpectedIterations(ITERATIONS);
    }

    @Test
    public void testEndpoints() {
        assertEquals(hlrf.learningRate(0), START_RATE, MAXIMUM_DIFFERENCE);
        assertEquals(hlrf.learningRate(ITERATIONS), END_RATE, MAXIMUM_DIFFERENCE);
    }

    @Test
    public void testShape() throws Exception {
        double last = hlrf.learningRate(1);
        for (int i = 2; i < ITERATIONS; i += 10) {
            double current = hlrf.learningRate(i);
            assertTrue(current < last, "Should consistently decay.");
            last = current;
        }
    }

    @Test
    public void testBySampling() {
        // Verify the function fits known samples
        for (int j = 0; j < SAMPLES.length; j++) {
            assertEquals(hlrf.learningRate(SAMPLES[j]), SAMPLE_RESULTS[j],
                    SAMPLE_ACCURACY[j]);
        }
    }
}
