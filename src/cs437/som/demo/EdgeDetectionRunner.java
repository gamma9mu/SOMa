package cs437.som.demo;

import cs437.som.SOMBuilberConfigPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A form for running multiple EdgeDetectors and comparing their output to a
 * reference image.
 */
public class EdgeDetectionRunner {
    private static final long serialVersionUID = 0L;
    
    private JRadioButton exhaustiveRadioButton;
    private JRadioButton sampledRadioButton;
    private JTextField iterationCountInput;
    private SOMBuilberConfigPanel mapConfig;
    private JComboBox outputImageCmb;
    private JButton trainButton;
    private JButton runButton;
    private JPanel edgeDetectionRunnerForm;
    private ImageFrame reference;
    private ImageFrame output;

    public EdgeDetectionRunner() {

        iterationCountInput.setEnabled(false);
        exhaustiveRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (exhaustiveRadioButton.isSelected())
                    iterationCountInput.setEnabled(false);
            }
        });

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(edgeDetectionRunnerForm);
        frame.pack();
        frame.setVisible(true);
        trainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trainMap();
            }
        });
        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runMap();
            }
        });
        outputImageCmb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (outputImageCmb.getSelectedIndex() == 0) {
                }
            }
        });
    }

    private void trainMap() {
        ;
    }

    private void runMap() {
        ;
    }

    public static void main(String[] args) {
        new EdgeDetectionRunner();
    }

    @Override
    public String toString() {
        return "EdgeDetectionRunner";
    }
}
