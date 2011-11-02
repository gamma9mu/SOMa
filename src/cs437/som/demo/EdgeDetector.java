package cs437.som.demo;

import cs437.som.network.BasicHexGridSOM;
import cs437.som.SelfOrganizingMap;
import cs437.som.network.BasicSquareGridSOM;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An image edge detector using a self-organizing map.
 */
public class EdgeDetector {
    /** The minimum distance between 2 colors to classify as a change in detail */
    private static final int MINIMUM_COLOR_DISTANCE = 60;

    /** Better than stdout... */
    private Logger log = Logger.getLogger("EdgeDetector");

    /** The ED's SOM */
    private SelfOrganizingMap map = null;

    /** The number of possible 3x3 matrices with each element having 3 possible
     * values
     */
    private static final int threeRaiseNine = 19683;

    /** The number of possible colors that can be stored in 24 bits (2^25 - 1) */
    private static final int possibleColors = 33554431;

    /**
     * Create an edge detector.
     *
     * @param size The number of neurons to use in the self-organizing map.
     * @param iterations The number of iterations used in training the SOM.
     * @param useHex Whether to use an offset hexagonal grid in the SOM.  True
     * means use a hexagonal grid and false means use a square grid.
     */
    private EdgeDetector(int size, int iterations, boolean useHex) {
        if (useHex)
            map = new BasicHexGridSOM(size, 9, iterations);
        else
            map = new BasicSquareGridSOM(size, 9, iterations);
    }

    /**
     * Train the edge detector's self-organizing map with a random sample of
     * the possible inputs it may see.
     *
     * @param n The number of sample inputs to use for training.
     */
    private void trainWithRandomPermutations(int n) {
        int[][] matrices = generateRandomPermutations(n);
        log.info("Training.");
        for (int[] matrix : matrices) {
            map.trainWith(matrix);
        }
    }

    /**
     * Generate a random sampling of input matrices.
     *
     * @param n The number of samples to produce.
     * @return An array (size n) of double[9], each of which represents a 3x3
     * matrix in row major form.
     */
    private int[][] generateRandomPermutations(int n) {
        int[][] permutations = new int[n][9];
        Random r = new SecureRandom();

        log.info("Generating " + n + " random matrices.");
        for (int i = 0; i < permutations.length; i++) {
            permutations[i] = new int[] {
                    r.nextInt(3) - 1, r.nextInt(3) - 1, r.nextInt(3) - 1,
                    r.nextInt(3) - 1, r.nextInt(3) - 1, r.nextInt(3) - 1,
                    r.nextInt(3) - 1, r.nextInt(3) - 1, r.nextInt(3) - 1
            };
        }

        return permutations;
    }

    /**
     * Train the edge detector's self-organizing map with every possible input
     * matrix being shown to it once.
     */
    private void trainExhaustively() {
        int[][] matrices = generateAllPermutations();
        log.info("Training.");
        for (int[] matrix : matrices) {
            map.trainWith(matrix);
        }
    }

    /**
     * Generate all possible input matrices.
     *
     * @return An array (size 19683, or 3^9) of double[9], each of which
     * represents a 3x3 matrix in row major form.
     */
    private int[][] generateAllPermutations() {
        int[] possibleValues  = { -1, 0, 1 };

        log.info("Creating " + threeRaiseNine + " (all possible) matrices.");

        int[][] permutations = new int[threeRaiseNine][9];
        int row = 0;
        for (int possibleValue0 : possibleValues) {
            int[] temp = {possibleValue0, 0, 0, 0, 0, 0, 0, 0, 0};
            for (int possibleValue1 : possibleValues) {
                temp[1] = possibleValue1;
                for (int possibleValue2 : possibleValues) {
                    temp[2] = possibleValue2;
                    for (int possibleValue3 : possibleValues) {
                        temp[3] = possibleValue3;
                        for (int possibleValue4 : possibleValues) {
                            temp[4] = possibleValue4;
                            for (int possibleValue5 : possibleValues) {
                                temp[5] = possibleValue5;
                                for (int possibleValue6 : possibleValues) {
                                    temp[6] = possibleValue6;
                                    for (int possibleValue7 : possibleValues) {
                                        temp[7] = possibleValue7;
                                        for (int possibleValue8 : possibleValues) {
                                            temp[8] = possibleValue8;
                                            System.arraycopy(temp, 0, permutations[row], 0, 9);
                                            row++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return permutations;
    }

    /**
     * Detect the edges in an image.
     *
     * @param image The image to process.
     * @return A new image where the pixels correspond to colors assigned to
     * the individual neurons of the self-organizing map.
     */
    public BufferedImage runOnImage(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();

        log.info("Processing " + width + "x" + height + " image.");

        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int colorStep = possibleColors / map.getNeuronCount();

        for (int y = 1; y < height; y++) {
            for (int x = 1; x < width; x++) {
                int[] differenceMatrix = getDifferenceMatrix(image, x, y);
                int best = map.getBestMatchingNeuron(differenceMatrix);
                out.setRGB(x, y, colorStep * best);
            }
        }

        return out;
    }

    /**
     * Normalize an image produced in a previous processing.
     *
     * The most common color corresponds to the SOM's neuron that responds to a
     * lack of an edge.  Whatever color that neuron was assigned is converted
     * to black to serve as the background.  All other colors are converted to
     * white.
     *
     * This operation is predicated on the idea that several neurons will
     * respond to different features in the image that correspond to parts of
     * an edge, whereas another single neuron will be most closely associated
     * with a lack of an edge.
     *
     * @param image The image to normalize.
     * @return A black and white image where the white pixels indicate an edge
     * in the original image.
     */
    public BufferedImage normalizeImage(BufferedImage image) {
        int neurons = map.getNeuronCount();
        Map<Integer, Integer> colorFrequency = new HashMap<Integer, Integer>(neurons * neurons);

        log.info("Normalizing.");
        log.fine("Computing color frequencies.");
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int color = image.getRGB(x, y);
                if (colorFrequency.containsKey(color))
                    colorFrequency.put(color, colorFrequency.get(color) + 1);
                else
                    colorFrequency.put(color, 1);
            }
        }

        log.fine("Finding most common color.");
        int mostCommonColor = -1;
        int max = 0;
        for (Integer integer : colorFrequency.keySet()) {
            int count = colorFrequency.get(integer);
            if (count > max) {
                max = count;
                mostCommonColor = integer;
            }
        }

        log.fine("Rewriting colors.");
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int black = Color.black.getRGB();
        int white = Color.white.getRGB();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int color = image.getRGB(x, y);
                if (color == mostCommonColor)
                    out.setRGB(x, y, black);
                else
                    out.setRGB(x, y, white);
            }
        }

        return out;
    }

    /**
     * Write an image to a PNG file.
     *
     * @param image The image to store.
     * @param filename The name of the file to write to (without the ".png"
     * extension).
     */
    public void writeImage(BufferedImage image, String filename) {
        try {
            ImageIO.write(image, "png", new File(filename + ".png"));
        } catch (IOException e) {
            log.severe("Exception while writing file: " + filename + ".png");
            log.log(Level.SEVERE, "Exception: ", e);
        }
    }

    private int[] getDifferenceMatrix(BufferedImage img, int x, int y) {
        int[] matrix = new int[9];

        int center = getPixelColor(img, x, y);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int pixel = getPixelColor(img, x + j - 1, y + i - 1);
                if (pixel == -1) {
                    matrix[i * 3 + j] = -1;
                } else {
                    double difference = colorDistance(center, pixel);
                    if (difference < MINIMUM_COLOR_DISTANCE) {
                        matrix[i * 3 + j] = 0;
                    } else {
                        matrix[i * 3 + j] = 1;
                    }
                }
            }
        }

        return matrix;
    }

    /**
     * Retrieve the color of a single pixel in an image.
     *
     * @param image The image in question.
     * @param x The x-axis location of the pixel.
     * @param y The y-axis locatioj of the pixel.
     * @return The color, as a 32-bit ARGB value, at (x, y) or -1 if that
     * location would be outside of the boundaries of the image.
     */
    private int getPixelColor(BufferedImage image, int x, int y) {
        if (x < 0 || x > image.getWidth() - 1 || y < 0 || y > image.getHeight() - 1)
            return -1;
        return image.getRGB(x, y);
    }

    /**
     * Calculate the Euclidean difference between two colors using their RGB
     * values as elements of a 3-dimensional vector.
     *
     * @param one The first color as a 32-bit ARGB value.
     * @param two The second color as a 32-bit ARGB value.
     * @return The distance between one and two.
     */
    private double colorDistance(int one, int two) {
        int r1 = (one >> 16) & 0xFF, g1 = (one >> 8) & 0xFF, b1 = one & 0xFF;
        int r2 = (two >> 16) & 0xFF, g2 = (two >> 8) & 0xFF, b2 = two & 0xFF;
        int dr = r1 - r2, dg = g1 - g2, db = b1 - b2;
        return Math.sqrt(dr * dr + dg * dg + db * db);
    }

    @Override
    public String toString() {
        return "EdgeDetector{map=" + map + '}';
    }

    /**
     * Convenience method to create an edge detector with a square grid map
     * that has been trained with a random sampling of inputs.
     *
     * @param mapSize The number of neurons to size the map.
     * @param numberOfMatrices The number of matrices to use in training the ED's map.
     * @return The trained edge detector.
     */
    public static EdgeDetector edgeDetectorTrainedWithRandomData(int mapSize, int numberOfMatrices) {
        EdgeDetector ed = new EdgeDetector(mapSize, numberOfMatrices, false);
        ed.trainWithRandomPermutations(numberOfMatrices);
        return ed;
    }

    /**
     * Convenience method to create an edge detector with a square grid map
     * that has been exhaustively trained.
     *
     * @param mapSize The number of neurons to size the map.
     * @return The trained edge detector.
     */
    public static EdgeDetector edgeDetectorExhaustivelyTrained(int mapSize) {
        EdgeDetector ed = new EdgeDetector(mapSize, threeRaiseNine, false);
        ed.trainExhaustively();
        return ed;
    }

    /**
     * Convenience method to create an edge detector with a hex grid map that
     * has been trained with a random sampling of inputs.
     *
     * @param mapSize The number of neurons to size the map.
     * @param numberOfMatrices The number of matrices to use in training the ED's map.
     * @return The trained edge detector.
     */
    public static EdgeDetector edgeDetectorHexTrainedWithRandomData(int mapSize, int numberOfMatrices) {
        EdgeDetector ed = new EdgeDetector(mapSize, numberOfMatrices, true);
        ed.trainWithRandomPermutations(numberOfMatrices);
        return ed;
    }

    /**
     * Convenience method to create an edge detector with a hex grid map that
     * has been exhaustively trained.
     *
     * @param mapSize The number of neurons to size the map.
     * @return The trained edge detector.
     */
    public static EdgeDetector edgeDetectorHexExhaustivelyTrained(int mapSize) {
        EdgeDetector ed = new EdgeDetector(mapSize, threeRaiseNine, true);
        ed.trainExhaustively();
        return ed;
    }

    public static void main(String[] args) throws IOException {
        EdgeDetector ed = EdgeDetector.edgeDetectorExhaustivelyTrained(10);

        BufferedImage original = ImageIO.read(new File("image.jpg"));
        BufferedImage detected = ed.runOnImage(original);

        ed.writeImage(detected, "out");

        BufferedImage normalized = ed.normalizeImage(detected);
        ed.writeImage(normalized, "out_normalized");

        new ImageFrame("Reference Image", ImageIO.read(new File("known_edges.jpg")), 0, 0);
        new ImageFrame("Processed Image", detected, 500, 0);
        new ImageFrame("Normalized Image", normalized, 1000, 0);
    }
}
