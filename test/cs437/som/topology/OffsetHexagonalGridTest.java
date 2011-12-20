package cs437.som.topology;

import cs437.som.Dimension;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class OffsetHexagonalGridTest {
    private static final Dimension dimensions = new Dimension(7,7);
    private static final OffsetHexagonalGrid grid = new OffsetHexagonalGrid();
    private static final double MAX_DISTANCE = 0.001;

    @BeforeMethod
    public void setUp() throws Exception {
        grid.setNeuronCount(dimensions);
    }

    @Test
    public void testGridDistance() throws Exception {
        assertEquals(grid.gridDistance(24, 24), 0, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 17), 1, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 23), 1, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 25), 1, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 30), 1, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 31), 1, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 32), 1, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 10), 2, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 15), 2, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 16), 2, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 18), 2, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 19), 2, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 22), 2, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 26), 2, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 29), 2, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 33), 2, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 37), 2, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 38), 2, MAX_DISTANCE);
        assertEquals(grid.gridDistance(24, 39), 2, MAX_DISTANCE);
    }
}
