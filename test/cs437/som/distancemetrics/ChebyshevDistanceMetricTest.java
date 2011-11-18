package cs437.som.distancemetrics;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ChebyshevDistanceMetricTest {
    static final double MAX_DIFFERENCE = 0.000000000000000001;

    private static final double[] v10 = { 8.60e-1};
    private static final double[] v11 = {-2.10e-16};
    private static final double v1 = 0.8600000000000002;

    private static final double[] v20 = { 1.20e+0, -3.90e-1};
    private static final double[] v21 = {-1.10e+0,  6.30e-1};
    private static final double v2 = 2.3;

    private static final double[] v50 = {-5.80e-1, -1.80e+0, 2.00e-1, 2.10e-1,  6.20e-1};
    private static final double[] v51 = {-6.00e-2, -1.30e-1, 7.10e-1, 7.60e-1, -9.30e-1};
    private static final double v5 = 1.67;
    
    @Test
    public void testDistance() throws Exception {
        ChebyshevDistanceMetric cdm = new ChebyshevDistanceMetric();
        assertEquals(cdm.distance(v10, v11), v1, MAX_DIFFERENCE);
        assertEquals(cdm.distance(v20, v21), v2, MAX_DIFFERENCE);
        assertEquals(cdm.distance(v50, v51), v5, MAX_DIFFERENCE);
    }
}
