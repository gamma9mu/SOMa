package cs437.som.demo;

import cs437.som.SelfOrganizingMap;
import cs437.som.network.BasicSquareGridSOM;
import cs437.som.visualization.SOM2dPlotter;

import java.security.SecureRandom;
import java.util.Random;

public class BasicMapDemo {
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
}