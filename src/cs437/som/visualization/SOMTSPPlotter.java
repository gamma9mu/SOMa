package cs437.som.visualization;

import cs437.som.SelfOrganizingMap;

import javax.swing.*;
import java.awt.*;

/**
 * Visualization for SOM with a 2-dimensional input.
 */
public class SOMTSPPlotter extends JFrame {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;
    SelfOrganizingMap map = null;
    double[][] cities;

    /**
     * Create and setup a dot plot for a 2D input SOM.
     * @param map The SOM to plot.
     * @param cities Coordinates of cities
     */
    public SOMTSPPlotter(SelfOrganizingMap map, double[][] cities) {
        super("SOM Plot");

        if (map.getInputLength() != 2) throw new IllegalArgumentException("SOM does not map 2d inputs");

        this.map = map;
        this.cities = cities;
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        createBufferStrategy(2);
    }

    /**
     * Draw the current "locations" of the SOM's neurons.
     */
    public void draw() {
        Graphics g = getBufferStrategy().getDrawGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        int neuronCount = map.getNeuronCount();
        double[][] pts = new double[neuronCount][2];

        for (int i = 0; i < neuronCount; i++) {
            pts[i][0] = map.getWeight(i, 0);
            pts[i][1] = map.getWeight(i, 1);
        }

        g.setColor(Color.black);
        int x1 = (int)(pts[0][0]*WIDTH);
        int y1 = (int)(pts[0][1]*HEIGHT);
        for (int i = 1; i <= pts.length; i++) {
            int x2 = (int)(pts[i%pts.length][0]*WIDTH);
            int y2 = (int)(pts[i%pts.length][1]*HEIGHT);
            g.drawLine(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
        }

        g.setColor(Color.red);
        for (double[] city : cities) {
          g.fillOval((int)(city[0]*WIDTH)-3, (int)(city[1]*HEIGHT)-3, 6, 6);
        }

        getBufferStrategy().show();
    }

    @Override
    public String toString() {
        return "SOM2dPlotter{map=" + map + '}';
    }
}
