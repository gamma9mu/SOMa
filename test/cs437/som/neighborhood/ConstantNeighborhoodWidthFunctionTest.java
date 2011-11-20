package cs437.som.neighborhood;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ConstantNeighborhoodWidthFunctionTest {
    private static final double MAXIMUM_DIFFERENCE = 0.0000000000000001;
    private static final int ITERATIONS = 1000;
    private static final double WIDTH = 1.0;

    @Test
    public void testNeighborhoodWidth() throws Exception {
        ConstantNeighborhoodWidthFunction cnwf =
                new ConstantNeighborhoodWidthFunction(WIDTH);
        cnwf.setExpectedIterations(ITERATIONS);
        for (int i = 0; i <= ITERATIONS; i++) {
            assertEquals(cnwf.neighborhoodWidth(i), WIDTH, MAXIMUM_DIFFERENCE);
        }
    }
}
