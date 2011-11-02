package cs437.som.network;

import cs437.som.SelfOrganizingMap;
import cs437.som.visualization.SOM2dPlotter;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

/**
 * A basic self-organizing map where the neurons are arranged by their weights.
 *
 * An simple example usage:
 *
 * <code>
 * <pre>
 * private static final int iterations = 500;
 *
 * public static void main(String[] args) {
 *     SelfOrganizingMap som = new BasicPlanarGridSOM(7, 2, iterations);
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
public class BasicPlanarSOM extends NetworkBase {

    public BasicPlanarSOM(int neuronCount, int inputVectorSize, int expectedIterations) {
        super(inputVectorSize, neuronCount, expectedIterations);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double neuronDistance(int neuron0, int neuron1) {
        double sum = 0.0;
        for (int i = 0; i < inputVectorSize; i++) {
            double difference = weightMatrix[neuron0][i] - weightMatrix[neuron1][i];
            sum += difference * difference;
        }
        return Math.sqrt(sum);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double neighborhoodWidth() {
        if (time < (expectedIterations / 5))
            return 1.0 * (1.0 - (time / (double) expectedIterations));
        return 0.0;
    }

    private static final int iterations = 3000;
    private static final int iterDelay = 10; /* ms */
    private static final double tenByTenStep = 10.1;
    private static final double nearnessOffest = 0.5;

    public static void main(String[] args) {
        SelfOrganizingMap som = new BasicPlanarSOM(100, 2, iterations);
        Random r = new SecureRandom();

        System.out.println("Before Training");
        print10x10Map(som);

        SOM2dPlotter plot = new SOM2dPlotter(som);
        for (int i = 0; i < iterations; i++) {
            double[] in = { r.nextDouble() * 10, r.nextDouble() * 10 };
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
            System.out.print(String.format("%2d\t", (int)Math.round(i)));
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
            System.out.print(String.format("%2d\t", (int)Math.round(i)));
            for (double j = 1.0; j < tenByTenStep; j += 1.0) {
                System.out.print(String.format("%2d ",
                        som.getBestMatchingNeuron(new double[]{i + r.nextDouble() - nearnessOffest, j + r.nextDouble() - nearnessOffest})));
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
        return "BasicPlanarSOM{weightMatrix=" + (weightMatrix == null ? null : Arrays.toString(weightMatrix)) + '}';
    }
}
