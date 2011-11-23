package cs437.som.neighborhood;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class CompoundNeighborhoodTest {
    private static final double MAXIMUM_DIFFERENCE = 0.0000000000000001;
    private static final int CROSSOVER_POINT = 500;
    private static final int FIRST_VALUE = 10;
    private static final int SECOND_VALUE = 2;

    private ConstantNeighborhoodWidthFunction cnwf0;
    private ConstantNeighborhoodWidthFunction cnwf1;
    private CompoundNeighborhood cn;

    @BeforeTest
    public void setUp() {
        cnwf0 = new ConstantNeighborhoodWidthFunction(FIRST_VALUE);
        cnwf0.setExpectedIterations(CROSSOVER_POINT);
        cnwf1 = new ConstantNeighborhoodWidthFunction(SECOND_VALUE);
        cnwf1.setExpectedIterations(CROSSOVER_POINT);
        cn = new CompoundNeighborhood(cnwf0);
        cn.addNeighborhood(cnwf1, CROSSOVER_POINT);
    }

    @Test
    public void testFirstChild() throws Exception {
        for (int i = 0; i < CROSSOVER_POINT; i++) {
            assertEquals(cn.neighborhoodWidth(i), FIRST_VALUE,
                    MAXIMUM_DIFFERENCE);
        }
    }

    @Test
    public void testSecondChild() {
        for (int j = 0; j < CROSSOVER_POINT; j++) {
            assertEquals(cn.neighborhoodWidth(j + CROSSOVER_POINT),
                    FIRST_VALUE, MAXIMUM_DIFFERENCE);
        }
    }
}
