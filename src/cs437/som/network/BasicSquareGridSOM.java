package cs437.som.network;

import cs437.som.SelfOrganizingMap;
import cs437.som.visualization.SOM2dPlotter;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

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
 *     SelfOrganizingMap som = new BasicSquareGridSOM(7, 2, iterations);
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
@SuppressWarnings({"UseOfSystemOutOrSystemErr"})
public class BasicSquareGridSOM extends NetworkBase {

    public BasicSquareGridSOM(int gridSize, int inputVectorSize, int expectedIterations) {
        super(inputVectorSize, gridSize * gridSize, expectedIterations, gridSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double neuronDistance(int neuron0, int neuron1) {
        int row0 = neuron0 / gridSize;
        int col0 = neuron0 % gridSize;
        int row1 = neuron1 / gridSize;
        int col1 = neuron1 % gridSize;

        int dr = row1 - row0;
        int dc = col1 - col0;

        return Math.sqrt((dr * dr) + (dc * dc));
    }

    /* --------------------------------------------------------------------- */
    /*                        dry run methods follow                         */
    /* --------------------------------------------------------------------- */

    private static final int iterations = 500;
    private static final int iterDelay = 20; /* ms */
    private static final double tenByTenStep = 10.1;
    private static final double nearnessOffest = 0.5;

    public static void main(String[] args) {
        SelfOrganizingMap som = new BasicSquareGridSOM(7, 2, iterations);
        Random r = new SecureRandom();

        System.out.println("Before Training");
        print10x10Map(som);

        SOM2dPlotter plot = new SOM2dPlotter(som);
        for (int i = 0; i < iterations; i++) {
            double[] in = {r.nextDouble() * 10, r.nextDouble() * 10};
            som.trainWith(in);
            plot.draw();
            try { Thread.sleep(iterDelay); } catch (InterruptedException ignored) { }
        }

        System.out.println("After training");
        print10x10Map(som);

        System.out.println("Nearby points");
        print10x10NearbyMap(som);
    }

    private static void print10x10Map(SelfOrganizingMap som) {
        System.out.println("  \t 1  2  3  4  5  6  7  8  9 10");

        for (double i = 1.0; i < tenByTenStep; i += 1.0) {
            System.out.print(String.format("%2d\t", (int) Math.round(i)));
            for (double j = 1.0; j < tenByTenStep; j += 1.0) {
                System.out.print(String.format("%2d ", som.getBestMatchingNeuron(new double[]{i, j})));
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void print10x10NearbyMap(SelfOrganizingMap som) {
        Random r = new SecureRandom();

        System.out.println("  \t 1  2  3  4  5  6  7  8  9 10");


        for (double i = 1.0; i < tenByTenStep; i += 1.0) {
            System.out.print(String.format("%2d\t", (int) Math.round(i)));
            for (double j = 1.0; j < tenByTenStep; j += 1.0) {
                System.out.print(String.format("%2d ",
                        som.getBestMatchingNeuron(new double[]{i + r.nextDouble() - nearnessOffest,
                                j + r.nextDouble() - nearnessOffest})));
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "BasicSquareGridSOM{time=" + time +
                ", weightMatrix=" + (weightMatrix == null ? null : Arrays.asList(weightMatrix)) +
                ", neuronCount=" + neuronCount + ", inputSize=" + inputVectorSize + '}';
    }
}
