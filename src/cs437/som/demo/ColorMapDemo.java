package cs437.som.demo;

import cs437.som.Dimension;
import cs437.som.TrainableSelfOrganizingMap;
import cs437.som.distancemetrics.EuclideanDistanceMetric;
import cs437.som.membership.GeometricNeighborhoodMembershipFunction;
import cs437.som.neighborhood.LinearDecayNeighborhoodWidthFunction;
import cs437.som.network.CustomizableSOM;
import cs437.som.visualization.SOMColorPlotter;
import cs437.som.visualization.SOMHeatMap;

import java.security.SecureRandom;
import java.util.Random;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

/**
 * Demonstrates maps with 3-dimensional inputs with a visualization during training.
 */
public class ColorMapDemo {
    private final Logger logger = Logger.getLogger("ColorMapDemo");
    private static final int MAP_DIMENSION = 300;

    private final TrainableSelfOrganizingMap som;
    private final Random r = new SecureRandom();
    private final SOMColorPlotter plot;

    private SOMHeatMap heatMap;
    // Training samples: blue, green, red, purple, yellow, teal
    private double[][] samples = {{0,0,1},{0,1,0},{1,0,0},{1,0,1},{1,1,0},{0,1,1}};

    /**
     * Create a new SOM demo.
     * @param map An untrained SOM to watch.
     */
    public ColorMapDemo(TrainableSelfOrganizingMap map) {
        som = map;
        plot = new SOMColorPlotter(som);
        heatMap = new SOMHeatMap(som);
        heatMap.setLocation(plot.getX() + plot.getWidth(), plot.getY());

//        selectSamples();
    }

    /**
     * Run the demonstration.
     */
    public void run() {
        int iterations = som.getExpectedIterations();

        logger.info("Before Training");
        double[] heatMapSample = {1,0,0}; // show red component in heat map

        // Train the SOM on the samples, repeatedly update the visuals
        for (int i = 0; i < iterations; i++) {
            double[] in = samples[r.nextInt(samples.length)];
            som.trainWith(in);
            plot.draw();
            heatMap.update(heatMapSample);
            heatMap.draw();
        }

        logger.info("After training");
        // Iterate though component heat maps
        double[][] rgb = {{1,0,0},{0,1,0},{0,0,1}};
        while (heatMap.isEnabled()) {
            for (double[] arr : rgb) {
                heatMap.update(arr);
                heatMap.draw();
                try { sleep(1000); } catch (InterruptedException ignored) { }
            }
        }
    }

    /**
     * Select random colors to use in place of the default samples.
     */
    private void selectSamples() {
        logger.info("Selecting Training Samples");

        for (double[] color : samples) {
            color[0] = r.nextDouble();
            color[1] = r.nextDouble();
            color[2] = r.nextDouble();
        }
    }

    public static void main(String[] args) {
        Dimension dimension = new Dimension(MAP_DIMENSION, MAP_DIMENSION);
        CustomizableSOM som = new CustomizableSOM(dimension, 3, 1000);
        som.setDistanceMetricStrategy(new EuclideanDistanceMetric());
        som.setNeighborhoodWidthFunctionStrategy(
                new LinearDecayNeighborhoodWidthFunction((2.0/3.0)*MAP_DIMENSION));
        som.setNeighborhoodMembershipFunctionStrategy(
                new GeometricNeighborhoodMembershipFunction(0.75));
        new ColorMapDemo(som).run();
    }

    @Override
    public String toString() {
        return "ColorMapDemo{som=" + som + '}';
    }
}
