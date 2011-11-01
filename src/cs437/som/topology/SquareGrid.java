package cs437.som.topology;

import cs437.som.GridType;
import cs437.som.SOMError;

/**
 * Square grid strategy for self-organizing map.
 */
public class SquareGrid implements GridType {
    private int sideDimension;

    public void setNeuronCount(int neuronCount) {
        sideDimension = (int) Math.round(Math.floor(Math.sqrt(neuronCount)));
        if ((sideDimension * sideDimension) != neuronCount) {
            throw new SOMError("Neuron grid is not square.");
        }
    }

    public double gridDistance(int neuron0, int neuron1) {
        int row0 = neuron0 / sideDimension;
        int col0 = neuron0 % sideDimension;
        int row1 = neuron1 / sideDimension;
        int col1 = neuron1 % sideDimension;

        int dr = row1 - row0;
        int dc = col1 - col0;

        return Math.sqrt((dr * dr) + (dc * dc));
    }

    @Override
    public String toString() {
        return "SquareGrid";
    }
}
