package cs437.som.topology;

import cs437.som.Dimension;
import cs437.som.GridType;
import cs437.som.SOMError;

/**
 * Square grid strategy for self-organizing map.
 *
 * The layout is, for a 5 by 3 grid, as in the following illustration.
 * <pre>
 * _____________________
 * | 0 | 1 | 2 | 3 | 4 |
 * |---|---|---|---|---|
 * | 5 | 6 | 7 | 8 | 9 |
 * |---|---|---|---|---|
 * | 10| 11| 12| 13| 14|
 * |___|___|___|___|___|
 * </pre>
 */
public class SquareGrid implements GridType {
    private int neuronCount = 0;
    private int width = 0;

    public void setNeuronCount(Dimension dimension) {
        width = dimension.x;
        neuronCount = width * dimension.y;
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

        int dr = row1 - row0;
        int dc = col1 - col0;

        return Math.sqrt((dr * dr) + (dc * dc));
    }

    @Override
    public String toString() {
        return "SquareGrid";
    }
}
