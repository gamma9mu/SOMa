package cs437.som.neighborhood;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class CompoundNeighborhoodTest {
    private static final double MAXIMUM_DIFFERENCE = 0.0000000000000001;
    private static final int CROSSOVER_POINT = 500;
    private static final int FIRST_VALUE = 10;
    private static final int SECOND_VALUE = 2;

    @Test
    public void testNeighborhoodWidth() throws Exception {
        ConstantNeighborhoodWidthFunction cnwf0 =
                new ConstantNeighborhoodWidthFunction(FIRST_VALUE);
        cnwf0.setExpectedIterations(CROSSOVER_POINT);
        ConstantNeighborhoodWidthFunction cnwf1 =
                new ConstantNeighborhoodWidthFunction(SECOND_VALUE);
        cnwf1.setExpectedIterations(CROSSOVER_POINT);
        CompoundNeighborhood cn = new CompoundNeighborhood(cnwf0);
        cn.addNeighborhood(cnwf1, CROSSOVER_POINT);

        for (int i = 0; i < CROSSOVER_POINT; i++) {
            assertEquals(cn.neighborhoodWidth(i), FIRST_VALUE,
                    MAXIMUM_DIFFERENCE);
        }

        for (int j = 0; j < CROSSOVER_POINT; j++) {
            assertEquals(cn.neighborhoodWidth(j + CROSSOVER_POINT),
                    FIRST_VALUE, MAXIMUM_DIFFERENCE);
        }
    }
}
