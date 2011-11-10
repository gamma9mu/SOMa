package cs437.som.topology;

import cs437.som.Dimension;
import cs437.som.GridType;
import cs437.som.SOMError;

/**
 * A hexagonal grid strategy for self-organizing map.
 *
 * This particular variety of hexagonal map uses cells abutting at their sides
 * with the x-axis as in a square grid.  The y-axis is at 60 degrees from the
 * x-axis.  Essentially, a square grid is skewed, shifting each successive
 * column vertically by 1/2 width.
 *
 * The layout is, for a 5 by 3 grid, as in the following illustration.
 * <pre>
 * ____
 * | 0 |___
 * |___| 1 |___
 * | 5 |___| 2 |___
 * |___| 6 |___| 3 |___
 * | 10|___| 7 |___| 4 |
 * |___| 11|___| 8 |___|
 *     |___| 12|___| 9 |
 *         |___| 13|___|
 *             |___| 14|
 *                 |___|
 * </pre>
 */
public class SkewHexagonalGrid implements GridType {
    private int neuronCount = 0;
    private int height = 0;
    private int width = 0;

    public void setNeuronCount(Dimension dimension) {
        height = dimension.y;
        width = dimension.x;
        neuronCount = height * width;
    }

    public double gridDistance(int neuron0, int neuron1) {
        if (neuron0 > neuronCount || neuron0 < 0) {
            throw new SOMError("Invalid neuron index: " + neuron0);
        }

        if (neuron1 > neuronCount || neuron1 < 0) {
            throw new SOMError("Invalid neuron index: " + neuron1);
        }

        int row0 = neuron0 / width;
        int col0 = neuron0 % width;
        int row1 = neuron1 / width;
        int col1 = neuron1 % width;

        int dx = row1 - row0;
        int dy = col1 - col0;

        int distance;
        if (sign(dx) == sign(dy)) {
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
        return "SkewHexagonalGrid";
    }
}
