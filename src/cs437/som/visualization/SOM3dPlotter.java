package cs437.som.visualization;

import cs437.som.SelfOrganizingMap;
import cs437.som.Dimension;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Visualization for SOM with a 3-dimensional input.
 * Adapted from 2D plotter. Instead of using position to map neuron weights, it uses color.
 */
public class SOM3dPlotter extends JFrame {
    private static final long serialVersionUID = 0L;
    
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private SelfOrganizingMap map = null;
    private Dimension dims;
    private BufferedImage img;

    /**
     * Create and setup a dot plot for a 3D input SOM.
     * @param map The SOM to plot.
     */
    public SOM3dPlotter(SelfOrganizingMap map) {
        super("SOM Plot");

        if (map.getInputLength() != 3) throw new IllegalArgumentException("SOM does not map 3d inputs");

        this.map = map;

        dims = this.map.getGridSize();
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

        int neuronCount = map.getNeuronCount();
        int[] pts = new int[neuronCount];

        for (int i = 0; i < neuronCount; i++) {
            pts[i] = 0xFF000000;
            pts[i] += (int)(map.getWeight(i,0)*255)<<16;
            pts[i] += (int)(map.getWeight(i,1)*255)<<8;
            pts[i] += (int)(map.getWeight(i,2)*255);
        }

        img.setRGB(0,0, dims.x, dims.y, pts, 0, dims.x);

        g.drawImage(img.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT), 0, 0, null);

        getBufferStrategy().show();
    }

    @Override
    public String toString() {
        return "SOM3dPlotter{map=" + map + '}';
    }
}
