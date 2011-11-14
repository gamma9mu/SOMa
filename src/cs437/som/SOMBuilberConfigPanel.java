package cs437.som;

import cs437.som.distancemetrics.ChebyshevDistanceMetric;
import cs437.som.distancemetrics.EuclideanDistanceMetric;
import cs437.som.distancemetrics.ManhattanDistanceMetric;
import cs437.som.learningrate.ConstantLearningRateFunction;
import cs437.som.learningrate.HyperbolicLearningRateFunction;
import cs437.som.neighborhood.*;
import cs437.som.network.CustomizableSOM;
import cs437.som.topology.SkewHexagonalGrid;
import cs437.som.topology.SquareGrid;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * Provides a JPanel through which a CustomizableSOM can be configured for use
 * in a Swing application.
 */
public class SOMBuilberConfigPanel {
    private JComboBox learningRateCmb;
    private JComboBox distanceCmb;
    private JComboBox neighborhoodCmb;
    private JTextField widthText;
    private JTextField heightText;
    private JComboBox topologyCmb;
    private JPanel SOMBuilderConfigPanel;

    private Pattern positiveInteger = Pattern.compile("[1-9]\\d*");
    private boolean valid = false;

    public SOMBuilberConfigPanel() {
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
            public void caretUpdate(CaretEvent e) { validate(); }
        };

        widthText.addCaretListener(caretListener);
        heightText.addCaretListener(caretListener);
    }

    public boolean isValid() {
        return valid;
    }

    public SelfOrganizingMap createSOM(int inputSize, int expectedIterations) {
        int x = Integer.parseInt(widthText.getText());
        int y = Integer.parseInt(heightText.getText());
        Dimension dimension = new Dimension(x, y);
        CustomizableSOM som = new CustomizableSOM(dimension, inputSize, expectedIterations);

        som.setGridTypeStrategy(gridType());
        som.setLearningRateFunctionStrategy(learningRateType());
        som.setNeighborhoodWidthFunctionStrategy(neighborhoodType());
        som.setDistanceMetricStrategy(distanceType());

        return som;
    }

    private GridType gridType() {
        switch (topologyCmb.getSelectedIndex()) {
            case 1:
                return new SkewHexagonalGrid();
            default:
                return new SquareGrid();
        }
    }

    private LearningRateFunction learningRateType() {
        switch (learningRateCmb.getSelectedIndex()) {
            case 1:
                return new HyperbolicLearningRateFunction(0.8, 0.1);
            default:
                return new ConstantLearningRateFunction(0.2);
        }
    }

    private NeighborhoodWidthFunction neighborhoodType() {
        switch (neighborhoodCmb.getSelectedIndex()) {
            case 1:
                return new LinearDecayNeighborhoodWidthFunction(10.0);
            case 2:
                return new HyperbolicNeighborhoodWidthFunction(10.0, 1.0);
            case 3:
                return new ExponentialDecayNeighborhoodWidth(10.0);
            case 4:
                return new GaussianNeighborhoodWidthFunction(3.0);
            case 5:
                return new MexicanHatNeighborhoodWidthFunction(3.0);
            default:
                return new ConstantNeighborhoodWidthFunction(1.0);
        }
    }

    private DistanceMetric distanceType() {
        switch (distanceCmb.getSelectedIndex()) {
            case 1:
                return new ChebyshevDistanceMetric();
            case 2:
                return new ManhattanDistanceMetric();
            default:
                return new EuclideanDistanceMetric();
        }
    }

    private void validate() {
        boolean pass = true;
        if (positiveInteger.matcher(widthText.getText()).matches()) {
            widthText.setBackground(Color.WHITE);
        } else {
            pass = false;
            if (! widthText.getText().isEmpty())
                widthText.setBackground(Color.RED);
        }

        if (positiveInteger.matcher(heightText.getText()).matches()) {
            heightText.setBackground(Color.WHITE);
        } else {
            pass = false;
            if (! heightText.getText().isEmpty())
                heightText.setBackground(Color.RED);
        }

        valid = pass;
    }
}
