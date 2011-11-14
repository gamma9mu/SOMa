package cs437.som.visualization;

import cs437.som.SOM;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Visualization for SOM with a 2-dimensional input.
 */
public class SOM2dPlotter extends JFrame {
    private static final long serialVersionUID = 0L;
    
    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;
    SOM map = null;

    /**
     * Create and setup a dot plot for a 2D input SOM.
     * @param map The SOM to plot.
     */
    public SOM2dPlotter(SOM map) {
        super("SOM Plot");

        if (map.getInputLength() != 2) throw new IllegalArgumentException("SOM does not map 2d inputs");

        this.map = map;
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

//        double xmin, xmax, ymin, ymax;
//        xmin = xmax = pts[0][0] = map.getWeight(0, 0);
//        ymin = ymax = pts[0][1] = map.getWeight(0, 1);

        for (int i = 0; i < neuronCount; i++) {
            pts[i][0] = map.getWeight(i, 0);
            pts[i][1] = map.getWeight(i, 1);
//            if (pts[i][0] < xmin) xmin = pts[i][0];
//            if (pts[i][0] > xmax) xmax = pts[i][0];
//            if (pts[i][1] < ymin) ymin = pts[i][1];
//            if (pts[i][1] > ymax) ymax = pts[i][1];
        }
//        double dx = (xmax - xmin);
//        double dy = (ymax - ymin);

        g.setColor(Color.black);
        for (double[] pt : pts) {
//            int x = (int) Math.round(pt[0] / dx * (getWidth() - 10));
//            int y = (int) Math.round(pt[1] / dy * (getHeight() - 10));
            int x = (int)Math.round(pt[0] * 30) + 20;
            int y = (int)Math.round(pt[1] * 30) + 20;
            g.drawOval(x - 2, y - 2, 4, 4);
        }
        getBufferStrategy().show();
    }

    @Override
    public String toString() {
        return "SOM2dPlotter{map=" + map + '}';
    }
}
