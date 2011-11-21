package cs437.som.network;

import cs437.som.Dimension;
import cs437.som.SOMError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple self-organizing map, using a hexagonal grid for the neurons.
 *
 * An simple example usage:
 *
 * <code>
 * <pre>
 * private static final int iterations = 500;
 *
 * public static void main(String[] args) {
 *     TrainableSelfOrganizingMap som = new BasicHexGridSOM(7, 2, iterations);
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
public class BasicHexGridSOM extends NetworkBase {

    /**
     * Create a new BasicHexGridSOM.
     * 
     * @param gridSize The neuron grid dimensions.
     * @param inputVectorSize The input vector size.
     * @param expectedIterations The expected number of training iterations.
     */
    public BasicHexGridSOM(Dimension gridSize, int inputVectorSize,
                           int expectedIterations) {
        super(gridSize, inputVectorSize, expectedIterations);
    }

    @Override
    protected double neuronDistance(int neuron0, int neuron1) {
        int row0 = neuron0 / gridSize.x;
        int col0 = neuron0 % gridSize.x;
        int row1 = neuron1 / gridSize.x;
        int col1 = neuron1 % gridSize.x;

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
        return "BasicHexGridSOM{time=" + time +
                ", weightMatrix=" +
                (weightMatrix == null ? null : Arrays.deepToString(weightMatrix)) +
                ", neuronCount=" + neuronCount +
                ", inputSize=" + inputVectorSize + '}';
    }

    @Override
    public void write(OutputStreamWriter destination) throws IOException {
        destination.write(String.format("Map type: BasicHexGridSOM%n"));
        super.write(destination);
        destination.flush();
    }

    /**
     * Read a BasicHexGridSOM from an input stream.
     *
     * @param input The stream to read from.  This stream should be passed in
     * as soon as it is known to represent a BasicHexGridSOM.
     * @return A BasicHexGridSOM as represented by the contents of
     * {@code input}.
     * @throws IOException if something fails while reading the stream.
     */
    public static BasicHexGridSOM read(BufferedReader input) throws IOException {
        Pattern dimensionRegEx = Pattern.compile(
                "(?:grid)?\\s*dimensions\\s*:\\s*(\\d+)\\s*,\\s*(\\d+)",
                Pattern.CASE_INSENSITIVE);
        Pattern inputVectorSizeRegEx = Pattern.compile(
            "(?:input)?\\s*length\\s*:\\s*(\\d+)",
            Pattern.CASE_INSENSITIVE);
        Pattern iterationsRegEx = Pattern.compile("iterations\\s*:\\s*(\\d+)",
                Pattern.CASE_INSENSITIVE);
        Pattern weightRegEx = Pattern.compile("weights\\s*(?::)",
                Pattern.CASE_INSENSITIVE);

        Dimension dimension = null;
        int inputVectorSize = 0;
        int iterations = 0;

        String line = input.readLine();
        Matcher match = weightRegEx.matcher(line);
        while (! match.matches() && input.ready()) {
            Matcher dimMatch = dimensionRegEx.matcher(line);
            if (dimMatch.matches()) {
                dimension = new Dimension(Integer.parseInt(dimMatch.group(1)),
                        Integer.parseInt(dimMatch.group(2)));
            }

            Matcher inputMatch = inputVectorSizeRegEx.matcher(line);
            if (inputMatch.matches()) {
                inputVectorSize = Integer.parseInt(inputMatch.group(1));
            }

            Matcher iterationsMatch = iterationsRegEx.matcher(line);
            if (iterationsMatch.matches()) {
                iterations = Integer.parseInt(iterationsMatch.group(1));
            }
            line = input.readLine();
            match = weightRegEx.matcher(line);
        }

        if (dimension == null || inputVectorSize < 1) {
            throw new SOMError("A valid dimension and input vector size must" +
                    " appear in a map's configuration%nand they must appear " +
                    "before the weight matrix.");
        }

        BasicHexGridSOM bhgsom =
                new BasicHexGridSOM(dimension, inputVectorSize, iterations);
        bhgsom.weightMatrix =
                readWeightMatrix(input, dimension.area, inputVectorSize);
        return bhgsom;
    }

    /**
     * Read a weight matrix from a stored map.
     *
     * @param input The input to read from
     * @param entryCount The number of neurons to read for.
     * @param length The input vector length.
     * @return A weight matrix that can be dropped into a SOM.
     * @throws IOException if something fails while reading the stream.
     */
    protected static double[][] readWeightMatrix(BufferedReader input,
                int entryCount, int length) throws IOException {
        Pattern endTagRegEx = Pattern.compile("end\\s*(?:weights)",
                Pattern.CASE_INSENSITIVE);
        Pattern weightVectorRegEx = Pattern.compile(
            "([+-]?[0-9]*\\.?[0-9]+(?:[Ee][+-]?[0-9]+)?)(?:,?\\s*)?");

        int readLines = 0;
        double[][] weights = new double[entryCount][length];

        String line = input.readLine();
        while (readLines < entryCount && input.ready() &&
                !endTagRegEx.matcher(line).matches()) {
            Matcher weightMatch = weightVectorRegEx.matcher(line);
            for (int i = 0; i < length; i++) {
                weightMatch.find();
                weights[readLines][i] =
                        Double.parseDouble(weightMatch.group(1));
            }
            line = input.readLine();
            readLines++;
        }

        return weights;
    }
}
