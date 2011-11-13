package cs437.som.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
* A simple frame for displaying a square scaledImage, scaled to 400x400 in a window.
*/
public class ImageFrame extends JPanel {
    private static final long serialVersionUID = 0L;
    
    /** The height and width dimension to which the scaledImage will be scaled */
    private static final int IMAGE_DIMENSION = 400;

    /** Image cache */
    private Image scaledImage = null;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(IMAGE_DIMENSION, IMAGE_DIMENSION);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(IMAGE_DIMENSION, IMAGE_DIMENSION);
    }

    /**
     * Create an empty panel.
     */
    public ImageFrame() {
        setSize(IMAGE_DIMENSION, IMAGE_DIMENSION);
    }

    /**
     * Display an image in a new panel.
     *
     * @param image The image to display.
     */
    public ImageFrame(BufferedImage image) {
        setSize(IMAGE_DIMENSION, IMAGE_DIMENSION);
        setImage(image);
    }

    /**
     * Set the image to display.
     * 
     * @param image The image to display.
     */
    public void setImage(BufferedImage image) {
        scaledImage = image.getScaledInstance(IMAGE_DIMENSION, IMAGE_DIMENSION,
                Image.SCALE_DEFAULT);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (scaledImage != null) {
            g.drawImage(scaledImage, 0, 0, IMAGE_DIMENSION, IMAGE_DIMENSION, this);
        }
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
        return (infoflags & ALLBITS) != 0;
    }

    @Override public String toString() { return "ImageFrame"; }

    /**
     * Create an ImageFrame and place it in a JFrame.
     *
     * @param title The title of the JFrame.
     * @param image The image to display.
     * @return A JFrame containing an ImageFrame.
     */
    public static JFrame createInJFrame(String title, BufferedImage image) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(new ImageFrame(image));
        frame.pack();
        return frame;
    }
}
