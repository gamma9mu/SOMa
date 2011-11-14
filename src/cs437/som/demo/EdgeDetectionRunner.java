package cs437.som.demo;

import cs437.som.SOMBuilderConfigPanel;
import cs437.som.SelfOrganizingMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * A form for running multiple EdgeDetectors and comparing their output to a
 * reference image.
 */
public class EdgeDetectionRunner {
    private static Pattern positiveInteger = Pattern.compile("[1-9]\\d*");

    private JRadioButton exhaustiveRadioButton;
    private JRadioButton sampledRadioButton;
    private JTextField iterationCountInput;
    private SOMBuilderConfigPanel mapConfig;
    private JComboBox<String> outputImageCmb;
    private JButton trainButton;
    private JButton runButton;
    private JPanel edgeDetectionRunnerForm;
    private JLabel referenceLabel;
    private JLabel outputLabel;
    private JFrame holdingFrame;

    private BufferedImage inputImage = null;
    private BufferedImage outputImage = null;
    private BufferedImage normalImage = null;

    private EdgeDetector ed = null;

    public EdgeDetectionRunner() {
        createUIComponents();

        iterationCountInput.setEnabled(false);
        exhaustiveRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (exhaustiveRadioButton.isSelected()) {
                    iterationCountInput.setEnabled(false);
                    trainButton.setEnabled(true);
                }
            }
        });

        iterationCountInput.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) { validateIterationCount(); }
        });

        holdingFrame = new JFrame();
        holdingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        holdingFrame.getContentPane().add(edgeDetectionRunnerForm);
        holdingFrame.pack();
        holdingFrame.setVisible(true);
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
            public void actionPerformed(ActionEvent e) { displayOutputImage(); }
        });
        sampledRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (sampledRadioButton.isSelected()) {
                    iterationCountInput.setEnabled(true);
                }
            }
        });
    }

    private void validateIterationCount() {
        boolean canEnableTrain = true;
        if (positiveInteger.matcher(iterationCountInput.getText()).matches()) {
            iterationCountInput.setBackground(Color.WHITE);
        } else {
            iterationCountInput.setBackground(Color.RED);
            canEnableTrain = false;
        }

        if (exhaustiveRadioButton.isSelected()) {
            canEnableTrain = true;
        }

        trainButton.setEnabled(canEnableTrain);
    }

    private void trainMap() {
        runButton.setEnabled(false);
        
        if (!mapConfig.isValid()) {
            return; // TODO WRONG!
        }

        if (exhaustiveRadioButton.isSelected()) {
            createExhaustiveSOM();
        } else {
            createSampleSOM();
        }

        runButton.setEnabled(true);
    }

    private void createSampleSOM() {
        int iterations = Integer.parseInt(iterationCountInput.getText());
        SelfOrganizingMap som = mapConfig.createSOM(9, iterations);
        ed = EdgeDetector.trainRandomlyFromMap(som, iterations);
    }

    private void createExhaustiveSOM() {
        int iterations = EdgeDetector.threeRaiseNine;
        SelfOrganizingMap som = mapConfig.createSOM(9, iterations);
        ed = EdgeDetector.trainExhaustivelyFromMap(som);
    }

    private void runMap() {
        try {
            inputImage = ImageIO.read(new File("image.jpg"));
        } catch (IOException ignored) { }

        outputImage = ed.runOnImage(inputImage);
        normalImage = ed.normalizeImage(outputImage);

        displayImages();
        holdingFrame.pack();
    }

    private void displayImages() {
        if (inputImage == null) {
            return;
        }

        referenceLabel.setIcon(new ImageIcon(inputImage));
        displayOutputImage();
    }

    private void displayOutputImage() {
        if (outputImage == null || normalImage == null) {
            return;
        }
        if (outputImageCmb.getSelectedIndex() == 0) {
            outputLabel.setIcon(new ImageIcon(outputImage));
        } else {
            outputLabel.setIcon(new ImageIcon(normalImage));
        }
    }

    public static void main(String[] args) {
        new EdgeDetectionRunner();
    }

    @Override
    public String toString() {
        return "EdgeDetectionRunner";
    }

    private void createUIComponents() {
    }
}
