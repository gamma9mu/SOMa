package cs437.som.network;

import cs437.som.Dimension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

/**
 * A simple self-organizing map, using a square grid for the neurons.
 *
 * An simple example usage:
 *
 * <code>
 * <pre>
 * private static final int iterations = 500;
 *
 * public static void main(String[] args) {
 *     TrainableSelfOrganizingMap som = new BasicSquareGridSOM(7, 2, iterations);
 *     Random r = new SecureRandom();
 *
 *     for (int i = 0; i < iterations; i++) {
 *         double[] in = {r.nextDouble() * 10, r.nextDouble() * 10};
 *         som.trainWith(in);
 *     }
 *
 *     // At this point, the SOM would be queried for it's BMU.
 * }
 * </pre>
 * </code>
 */
public class BasicSquareGridSOM extends NetworkBase {

    /**
     * Create a new BasicSquareGridSOM.
     *
     * @param gridSize The neuron grid dimensions.
     * @param inputVectorSize The input vector size.
     * @param expectedIterations The expected number of training iterations.
     */
    public BasicSquareGridSOM(Dimension gridSize, int inputVectorSize,
                              int expectedIterations) {
        super(gridSize, inputVectorSize, expectedIterations);
    }

    @Override
    protected double neuronDistance(int neuron0, int neuron1) {
        int row0 = neuron0 / gridSize.x;
        int col0 = neuron0 % gridSize.x;
        int row1 = neuron1 / gridSize.x;
        int col1 = neuron1 % gridSize.x;

        int dr = row1 - row0;
        int dc = col1 - col0;

        return Math.sqrt((dr * dr) + (dc * dc));
    }

    @Override
    public String toString() {
        return "BasicSquareGridSOM{time=" + time +
                ", weightMatrix=" +
                (weightMatrix == null ? null : Arrays.asList(weightMatrix)) +
                ", neuronCount=" + neuronCount +
                ", inputSize=" + inputVectorSize + '}';
    }

    @Override
    public void write(OutputStreamWriter destination) throws IOException {
        destination.write(String.format("Map type: BasicSquareGridSOM%n"));
        super.write(destination);
        destination.flush();
    }

    /**
     * Read a BasicSquareGridSOM from an input stream.
     *
     * @param input The stream to read from.  This stream should be passed in
     * as soon as it is known to represent a BasicSquareGridSOM.
     * @return A BasicSquareGridSOM as represented by the contents of
     * {@code input}.
     * @throws IOException if something fails while reading the stream.
     */
    public static BasicSquareGridSOM read(BufferedReader input) throws IOException {
        SOMFileReader sfr = new SOMFileReader();
                sfr.parse(input);

        BasicSquareGridSOM bsgsom = new BasicSquareGridSOM(
                sfr.getDimension(), sfr.getInputVectorSize(), sfr.getIterations());
        bsgsom.weightMatrix = readWeightMatrix(
                input, sfr.getDimension().area, sfr.getInputVectorSize());
        return bsgsom;
    }
}
