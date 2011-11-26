package cs437.som.visualization;

import cs437.som.SelfOrganizingMap;
import cs437.som.Dimension;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Visualization for SOM with a 3-dimensional input.
 * Adapted from the 2D plotter. Instead of using position to map neuron weights, it uses color.
 */
public class SOMColorPlotter extends JFrame {
    private static final long serialVersionUID = 0L;

    /**
     * The frame width.
     */
    public static final int WIDTH = 500;

    /**
     * The frame height.
     */
    public static final int HEIGHT = 500;

    // The maximum pixel alpha.
    private static final int MAX_ALPHA = 0xFF000000;
    // The maximum 8-bit byte value.
    private static final int BYTE_MAX = 255;
    private static final int BYTE_WIDTH = 8;

    private SelfOrganizingMap som = null;
    private Dimension dims;
    private BufferedImage img;
    private int neuronCount = 0;

    /**
     * Create and setup a dot plot for a 3D input SOM.
     * @param map The SOM to plot.
     */
    public SOMColorPlotter(SelfOrganizingMap map) {
        super("SOM Plot");
        if (map.getInputLength() != 3) {
            throw new IllegalArgumentException("\"map\" must accept 3-tuple input.");
        }

        som = map;
        dims = som.getGridSize();
        neuronCount = dims.area;

        img = new BufferedImage(dims.x, dims.y, BufferedImage.TYPE_INT_ARGB);

        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        createBufferStrategy(2);
    }

    /**
     * Draw the current weights of the SOM's neurons.
     */
    public void draw() {
        Graphics g = getBufferStrategy().getDrawGraphics();

        int[] pts = new int[neuronCount];

        for (int i = 0; i < neuronCount; i++) {
            pts[i] = MAX_ALPHA;
            pts[i] |= (int)(som.getWeight(i, 0) * BYTE_MAX) << (2 * BYTE_WIDTH);
            pts[i] |= (int)(som.getWeight(i, 1) * BYTE_MAX) << BYTE_WIDTH;
            pts[i] |= (int)(som.getWeight(i, 2) * BYTE_MAX);
        }

        img.setRGB(0,0, dims.x, dims.y, pts, 0, dims.x);

        g.drawImage(img.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT), 0, 0, null);

        getBufferStrategy().show();
    }

    @Override
    public String toString() {
        return "SOMColorPlotter{som=" + som + '}';
    }
}
