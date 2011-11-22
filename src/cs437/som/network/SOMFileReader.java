package cs437.som.network;

import cs437.som.Dimension;
import cs437.som.SOMError;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides for reading in self-organizing maps from files.
 */
class SOMFileReader {
    private static final Pattern dimensionRegEx = Pattern.compile(
            "(?:grid)?\\s*dimensions\\s*:\\s*(\\d+)\\s*,\\s*(\\d+)",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern inputVectorSizeRegEx = Pattern.compile(
            "(?:input)?\\s*length\\s*:\\s*(\\d+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern iterationsRegEx = Pattern.compile(
            "iterations\\s*:\\s*(\\d+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern weightRegEx = Pattern.compile(
            "weights\\s*(?::)", Pattern.CASE_INSENSITIVE);

    private Dimension dimension = null;
    private int inputVectorSize = 0;
    protected int iterations = 0;

    /**
     * Parse a self-organizing map from an input stream.
     *
     * @param input The input stream.
     * @throws IOException When an I/O error occurs.
     */
    public void parse(BufferedReader input) throws IOException {
        String line = input.readLine();
        Matcher match = weightRegEx.matcher(line);
        while (!match.matches() && input.ready()) {
            if (!matchDimension(line)
                    && !matchInputVectorSize(line)
                    && !matchIterations(line)) {
                unmatchedLine(line);
            }

            line = input.readLine();
            match = weightRegEx.matcher(line);
        }

        if (dimension == null || inputVectorSize < 1) {
            throw new SOMError(String.format(
                    "A valid dimension and input vector size must appear in "
                            + "a map's configuration%nand they must appear before "
                            + "the weight matrix."));
        }
    }

    /**
     * If SOMFileReader does not recognize, it calls this method.  The default
     * behavior is doing nothing.  This method is provided for extension in
     * subclasses.
     *
     * @param line The unrecognized line.
     */
    protected void unmatchedLine(String line) {
    }

    /**
     * Match an iteration line.
     *
     * @param line The input's line to attempt to match and extract from.
     * @return {@code true} if the line is matched, {@code false} otherwise.
     */
    protected boolean matchIterations(String line) {
        Matcher iterationsMatch = iterationsRegEx.matcher(line);
        if (iterationsMatch.matches()) {
            iterations = Integer.parseInt(iterationsMatch.group(1));
            return true;
        }
        return false;
    }

    /**
     * Match an input vector size line.
     *
     * @param line The input's line to attempt to match and extract from.
     * @return {@code true} if the line is matched, {@code false} otherwise.
     */
    private boolean matchInputVectorSize(String line) {
        Matcher inputMatch = inputVectorSizeRegEx.matcher(line);
        if (inputMatch.matches()) {
            inputVectorSize = Integer.parseInt(inputMatch.group(1));
            return true;
        }
        return false;
    }

    /**
     * Match a dimension line.
     *
     * @param line The input's line to attempt to match and extract from.
     * @return {@code true} if the line is matched, {@code false} otherwise.
     */
    private boolean matchDimension(String line) {
        Matcher dimMatch = dimensionRegEx.matcher(line);
        if (dimMatch.matches()) {
            dimension = new Dimension(Integer.parseInt(dimMatch.group(1)),
                    Integer.parseInt(dimMatch.group(2)));
            return true;
        }
        return false;
    }

    /**
     * Retrieve the parsed dimension.
     *
     * @return The dimensions from the input stream.
     */
    public Dimension getDimension() {
        return dimension;
    }

    /**
     * Retrieve the parsed input vector size.
     *
     * @return The input vector size from the input stream.
     */
    public int getInputVectorSize() {
        return inputVectorSize;
    }

    /**
     * Return the parsed iteration count.
     *
     * @return The iteration count from the input stream.
     */
    public int getIterations() {
        return iterations;
    }

    @Override
    public String toString() {
        return "SOMFileReader";
    }
}
