package cs437.som.topology;

import cs437.som.GridType;
import cs437.som.SOMError;

/**
 * Hexagonal grid strategy for self-organizing map.
 *
 * This particular variety of hexagonal map uses cells abutting at their sides
 * with the x-axis as in a square grid.  The y-axis is at 60 degrees from the
 * x-axis.  Essentially, a square grid is skewed, shifting each successive
 * column vertically by 1/2 width.
 */
public class SkewHexagonalGrid implements GridType {
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

    private int sign(int n) {
        return (n >= 0) ? 1 : -1;
    }

    @Override
    public String toString() {
        return "SkewHexagonalGrid";
    }
}
