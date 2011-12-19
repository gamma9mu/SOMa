package cs437.som.topology;

import cs437.som.Dimension;
import cs437.som.GridType;
import cs437.som.SOMError;

/**
 * A square grid strategy for SOMs, using Chebyshev distance, which corresponds
 * to the Moore neighborhood.
 */
public class Moore implements GridType {
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

        int row0 = neuron0 / width;
        int col0 = neuron0 % width;
        int row1 = neuron1 / width;
        int col1 = neuron1 % width;

        int dr = Math.abs(row1 - row0);
        int dc = Math.abs(col1 - col0);

        return (dr > dc) ? dr : dc;
    }

    @Override
    public String toString() {
        return "Moore";
    }
}
