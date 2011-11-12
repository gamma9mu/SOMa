package cs437.som.demo;

import cs437.som.Dimension;
import cs437.som.SelfOrganizingMap;
import cs437.som.distancemetrics.*;
import cs437.som.learningrate.*;
import cs437.som.neighborhood.*;
import cs437.som.network.CustomizableSOM;
import cs437.som.topology.*;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

@SuppressWarnings({"MagicNumber", "FeatureEnvy", "SuppressionAnnotation"})
public class SOMBuilber extends JDialog {
    private static final long serialVersionUID = 0L;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField widthText;
    private JTextField heightText;
    private JComboBox topologyCmb;
    private JComboBox learningRateCmb;
    private JComboBox neighborhoodCmb;
    private JComboBox distanceCmb;

    private Pattern positiveInteger = Pattern.compile("[1-9]\\d*");

    private CustomizableSOM map = null;
    private int inputSize;
    private int expectedIterations;
    private boolean isInputConsistent = false;

    public SOMBuilber(int inputSize, int expectedIterations) {
        this.inputSize = inputSize;
        this.expectedIterations = expectedIterations;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        topologyCmb.addItem("Square Grid");
        topologyCmb.addItem("Skew Hexagonal Grid");

        learningRateCmb.addItem("Constant Learning Rate Function");
        learningRateCmb.addItem("Hyperbolic Learning Rate Function");

        neighborhoodCmb.addItem("Constant Neighborhood Width Function");
        neighborhoodCmb.addItem("Linear Decay Neighborhood Width Function");
        neighborhoodCmb.addItem("Hyperbolic Neighborhood Width Function");
        neighborhoodCmb.addItem("Exponential Decay Neighborhood Width");
        neighborhoodCmb.addItem("Gaussian Neighborhood Width Function");
        neighborhoodCmb.addItem("Mexican Hat Neighborhood Width Function");

        distanceCmb.addItem("Euclidean Distance Metric");
        distanceCmb.addItem("Chebyshev Distance Metric");
        distanceCmb.addItem("Manhattan Distance Metric");

        CaretListener caretListener = new CaretListener() {
            public void caretUpdate(CaretEvent e) { tryToEnableOK(); }
        };

        widthText.addCaretListener(caretListener);
        heightText.addCaretListener(caretListener);
    }

    private void tryToEnableOK() {
        boolean canEnable = true;
        if (positiveInteger.matcher(widthText.getText()).matches()) {
            widthText.setBackground(Color.WHITE);
        } else {
            canEnable = false;
            if (! widthText.getText().isEmpty())
                widthText.setBackground(Color.RED);
        }

        if (positiveInteger.matcher(heightText.getText()).matches()) {
            heightText.setBackground(Color.WHITE);
        } else {
            canEnable = false;
            if (! heightText.getText().isEmpty())
                heightText.setBackground(Color.RED);
        }

        isInputConsistent = canEnable;
        tryEnable();
    }

    private void tryEnable() {
        buttonOK.setEnabled(isInputConsistent);
    }

    public SelfOrganizingMap getMap() {
        return map;
    }

    private void onOK() {
        Dimension dimension = new Dimension(
                Integer.parseInt(widthText.getText()),
                Integer.parseInt(heightText.getText()));

        map = new CustomizableSOM(dimension, inputSize, expectedIterations);
        setSOMTopology();
        setSOMLearningRate();
        setSOMNeighborhoodFunction();
        setSOMDistanceMetric();

        dispose();
    }

    private void setSOMDistanceMetric() {
        switch (distanceCmb.getSelectedIndex()) {
            case 1:
                map.setDistanceMetricStrategy(new ChebyshevDistanceMetric());
                break;
            case 2:
                map.setDistanceMetricStrategy(new ManhattanDistanceMetric());
                break;
            default:
                map.setDistanceMetricStrategy(new EuclideanDistanceMetric());
        }
    }

    private void setSOMNeighborhoodFunction() {
        switch (neighborhoodCmb.getSelectedIndex()) {
            case 1:
                map.setNeighborhoodWidthFunctionStrategy(new LinearDecayNeighborhoodWidthFunction(10.0));
                break;
            case 2:
                map.setNeighborhoodWidthFunctionStrategy(new HyperbolicNeighborhoodWidthFunction(10.0, 1.0));
                break;
            case 3:
                map.setNeighborhoodWidthFunctionStrategy(new ExponentialDecayNeighborhoodWidth(10.0));
                break;
            case 4:
                map.setNeighborhoodWidthFunctionStrategy(new GaussianNeighborhoodWidthFunction(3.0));
                break;
            case 5:
                map.setNeighborhoodWidthFunctionStrategy(new MexicanHatNeighborhoodWidthFunction(3.0));
                break;
            default:
                map.setNeighborhoodWidthFunctionStrategy(new ConstantNeighborhoodWidthFunction(1.0));
        }
    }

    private void setSOMLearningRate() {
        switch (learningRateCmb.getSelectedIndex()) {
            case 1:
                map.setLearningRateFunctionStrategy(new HyperbolicLearningRateFunction(0.8, 0.1));
                break;
            default:
                map.setLearningRateFunctionStrategy(new ConstantLearningRateFunction(0.2));
        }
    }

    private void setSOMTopology() {
        switch (topologyCmb.getSelectedIndex()) {
            case 1:
                map.setGridTypeStrategy(new SkewHexagonalGrid());
                break;
            default:
                map.setGridTypeStrategy(new SquareGrid());
        }
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        SOMBuilber dialog = new SOMBuilber(10, 1000);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    @Override
    public String toString() {
        return "SOMBuilber";
    }
}
