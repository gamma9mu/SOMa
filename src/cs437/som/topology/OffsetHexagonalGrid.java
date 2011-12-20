package cs437.som.topology;

import cs437.som.Dimension;
import cs437.som.GridType;
import cs437.som.SOMError;

/**
 * An offset hexagonal grid strategy for self-organizing map.
 *
 * This particular variety of hexagonal map offsets every other column by one
 * half a cell's height.
 *
 * The layout is, for a 5 by 3 grid, as in the following illustration.
 * <pre>
 *  ___     ___     ___
 * | 0 |___| 2 |___| 4 |
 * |___| 1 |___| 3 |___|
 * | 5 |___| 7 |___| 9 |
 * |___| 6 |___| 8 |___|
 * | 10|___| 12|___| 14|
 * |___| 11|___| 13|___|
 *     |___|   |___|
 * </pre>
 */
public class OffsetHexagonalGrid implements GridType {
    private int neuronCount = 0;
    private int width = 0;

    @Override
    public void setNeuronCount(Dimension dimension) {
        width = dimension.x;
        neuronCount = width * dimension.y;
    }

    @Override
    public double gridDistance(int neuron0, int neuron1) {
        if (neuron0 > neuronCount || neuron0 < 0) {
            throw new SOMError("Invalid neuron index: " + neuron0);
        }

        if (neuron1 > neuronCount || neuron1 < 0) {
            throw new SOMError("Invalid neuron index: " + neuron1);
        }

        // I had a lot of problems here until I stumbled across
        // http://www.gamedev.net/topic/610847-measuring-distance-on-hex-grid/
        int x0 = neuron0 % width;
        int x1 = neuron1 % width;
        int y0 = (neuron0 / width) - (x0 / 2);
        int y1 = (neuron1 / width) - (x1 / 2);

        int dx = x1 - x0;
        int dy = y1 - y0;

        int distance;
        if(sign(dx) == sign(dy)) {
            distance = Math.abs(dx + dy);
        } else {
            distance = Math.max(Math.abs(dx), Math.abs(dy));
        }

        return distance;
    }

    private static int sign(int n) {
        return (n >= 0) ? 1 : -1;
    }

    @Override
    public String toString() {
        return "OffsetHexagonalGrid";
    }
}
