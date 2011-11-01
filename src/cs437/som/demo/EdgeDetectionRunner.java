package cs437.som.demo;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * A form for running multiple EdgeDetectors and comparing their output to a
 * reference image.
 */
public class EdgeDetectionRunner {
    private JRadioButton exhaustiveRadioButton;
    private JRadioButton sampledRadioButton;
    private JTextField neuronCountInput;
    private JTextField iterationCountInput;
    private JLabel referenceImage;
    private JLabel outputImage;
    private JLabel neu;

    private int neuronCount;

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    private int iterations;

    public int getNeuronCount() {
        return neuronCount;
    }

    public void setNeuronCount(int neuronCount) {
        this.neuronCount = neuronCount;
    }

}
