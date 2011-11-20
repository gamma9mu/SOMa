package cs437.som.topology;

import cs437.som.Dimension;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class SkewHexagonalGridTest {
    private static final Dimension dimension = new Dimension(5, 3);
    private static final SkewHexagonalGrid hexGrid = new SkewHexagonalGrid();
    private static final double MAX_DISTANCE = 0.001;

    @BeforeMethod
    public void setUp() {
        hexGrid.setNeuronCount(dimension);
    }

    @Test
    public void testGridDistance() throws Exception {
        assertEquals(hexGrid.gridDistance(0, 0), 0, MAX_DISTANCE);
        assertEquals(hexGrid.gridDistance(0, 1), 1, MAX_DISTANCE);
        assertEquals(hexGrid.gridDistance(0, 5), 1, MAX_DISTANCE);
        assertEquals(hexGrid.gridDistance(7, 2), 1, MAX_DISTANCE);
        assertEquals(hexGrid.gridDistance(7, 6), 1, MAX_DISTANCE);
        assertEquals(hexGrid.gridDistance(7, 8), 1, MAX_DISTANCE);
        assertEquals(hexGrid.gridDistance(7, 12), 1, MAX_DISTANCE);
        assertEquals(hexGrid.gridDistance(14, 8), 2, MAX_DISTANCE);
        assertEquals(hexGrid.gridDistance(14, 9), 1, MAX_DISTANCE);
        assertEquals(hexGrid.gridDistance(14, 2), 4, MAX_DISTANCE);
    }
}
