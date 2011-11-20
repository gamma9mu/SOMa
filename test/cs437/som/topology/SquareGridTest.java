package cs437.som.topology;

import cs437.som.Dimension;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class SquareGridTest {
    private static final Dimension dimension = new Dimension(5, 3);
    private static final SquareGrid squareGrid = new SquareGrid();
    private static final double MAX_DISTANCE = 0.001;

    @BeforeMethod
    public void setUp() {
        squareGrid.setNeuronCount(dimension);
    }

    @Test
    public void testGridDistance() throws Exception {
        assertEquals(squareGrid.gridDistance(0, 0), 0, MAX_DISTANCE);
        assertEquals(squareGrid.gridDistance(0, 1), 1, MAX_DISTANCE);
        assertEquals(squareGrid.gridDistance(0, 5), 1, MAX_DISTANCE);
        assertEquals(squareGrid.gridDistance(7, 2), 1, MAX_DISTANCE);
        assertEquals(squareGrid.gridDistance(7, 6), 1, MAX_DISTANCE);
        assertEquals(squareGrid.gridDistance(7, 8), 1, MAX_DISTANCE);
        assertEquals(squareGrid.gridDistance(7, 12), 1, MAX_DISTANCE);
        assertEquals(squareGrid.gridDistance(14, 8), Math.sqrt(2), MAX_DISTANCE);
        assertEquals(squareGrid.gridDistance(14, 9), 1, MAX_DISTANCE);
        assertEquals(squareGrid.gridDistance(14, 13), 1, MAX_DISTANCE);
    }
}
