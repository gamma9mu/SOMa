package cs437.som.learningrate;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class HyperbolicLearningRateFunctionTest {
    private static final double MAXIMUM_DIFFERENCE = 0.0000000000000001;
    private static final int ITERATIONS = 1000;
    private static final double START_RATE = 0.7;
    private static final double END_RATE = 0.1;

    @Test
    public void testLearningRate() throws Exception {
        HyperbolicLearningRateFunction hlrf =
                new HyperbolicLearningRateFunction(START_RATE, END_RATE);
        hlrf.setExpectedIterations(ITERATIONS);
        assertEquals(hlrf.learningRate(0), START_RATE, MAXIMUM_DIFFERENCE);
        assertEquals(hlrf.learningRate(ITERATIONS), END_RATE, MAXIMUM_DIFFERENCE);

        double last = hlrf.learningRate(1);
        for (int i = 2; i < ITERATIONS; i += 10) {
            double current = hlrf.learningRate(i);
            assertTrue(current < last, "Should consistently decay.");
            last = current;
        }

        // todo check against perfect hyperbola
    }
}
