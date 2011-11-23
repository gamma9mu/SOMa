package cs437.som.learningrate;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ExponentialDecayLearningRateFunctionTest {
    private static final double MAXIMUM_DIFFERENCE = 0.0000000000000001;
    private static final int ITERATIONS = 1000;
    private static final double LEARNING_RATE = 1.0;

    private ExponentialDecayLearningRateFunction edrf;

    @BeforeTest
    public void setUp() {
        edrf = new ExponentialDecayLearningRateFunction(LEARNING_RATE);
        edrf.setExpectedIterations(ITERATIONS);
    }

    @Test
    public void testLearningRate() throws Exception {
        assertEquals(edrf.learningRate(0), LEARNING_RATE, MAXIMUM_DIFFERENCE);
    }

    @Test
    public void testShape() {
        double last = edrf.learningRate(1);
        for (int i = 2; i < ITERATIONS; i += 10) {
            double current = edrf.learningRate(i);
            assertTrue(current < last, "Should consistently decay.");
            last = current;
        }

        testLessThanLinear();
    }

    @Test
    public void testLessThanLinear() {
        assertEquals(edrf.learningRate(0), LEARNING_RATE,
                "Exponential decay should be equal with linear at start.");
        for (int i = 1; i < ITERATIONS; i++) {
            assertTrue(edrf.learningRate(i) < LEARNING_RATE - (i / ITERATIONS),
                    "Exponential decay should be less than linear.");
        }
    }
}
