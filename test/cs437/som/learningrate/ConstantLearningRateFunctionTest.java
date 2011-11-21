package cs437.som.learningrate;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ConstantLearningRateFunctionTest {
    private static final double MAXIMUM_DIFFERENCE = 0.0000000000000001;
    private static final int ITERATIONS = 1000;
    private static final double LEARNING_RATE = 0.2;

    @Test
    public void testLearningRate() throws Exception {
        ConstantLearningRateFunction clrf = new ConstantLearningRateFunction(LEARNING_RATE);
        clrf.setExpectedIterations(ITERATIONS);
        assertEquals(clrf.learningRate(1), LEARNING_RATE, MAXIMUM_DIFFERENCE);
        assertEquals(clrf.learningRate(10), LEARNING_RATE, MAXIMUM_DIFFERENCE);
        assertEquals(clrf.learningRate(100), LEARNING_RATE, MAXIMUM_DIFFERENCE);
        assertEquals(clrf.learningRate(1000), LEARNING_RATE, MAXIMUM_DIFFERENCE);
    }
}
