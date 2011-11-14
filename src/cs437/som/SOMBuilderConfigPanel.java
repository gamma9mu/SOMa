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
import javax.swing.event.SwingPropertyChangeSupport;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.regex.Pattern;

/**
 * Provides a JPanel through which a CustomizableSOM can be configured for use
 * in a Swing application.
 */
public class SOMBuilderConfigPanel {
    private JComboBox learningRateCmb;
    private JComboBox distanceCmb;
    private JComboBox neighborhoodCmb;
    private JTextField widthText;
    private JTextField heightText;
    private JComboBox topologyCmb;
    private JPanel SOMBuilderConfigPanel;

    private Pattern positiveInteger = Pattern.compile("[1-9]\\d*");
    private boolean valid = false;
    private PropertyChangeSupport propertyChangeSupport =
            new SwingPropertyChangeSupport(this, true);

    /**
     * Create a new SOMBuilderConfigPanel.
     */
    public SOMBuilderConfigPanel() {
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

    /**
     * Determine if the input to the SOMBuilderConfigPanel corresponds to a
     * valid SOM.
     *
     * @return True if this SOMBuilderConfigPanel can produce a valid
     * CustomizableSOM and false otherwise.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Internal setter for {@code valid}, used to handle property changes.
     * 
     * @param valid The new value of {@code valid}.
     */
    private void setValid(boolean valid) {
        boolean oldvalid = this.valid;
        this.valid = valid;
        propertyChangeSupport.firePropertyChange("valid", oldvalid, valid);
    }

    /**
     * Create a CustomizableSOM based on the data in this form.
     *
     * @param inputSize The input size of the SOM.
     * @param expectedIterations The expected iterations the SOM will undergo
     * in training.
     * @return A CustomizableSOM based on inputSize, expectedIterations and the
     * form's input.
     */
    public TrainableSelfOrganizingMap createSOM(int inputSize, int expectedIterations) {
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

    /**
     * Translate the topology selection in the form to a GridType object.
     *
     * @return a GridType for use in a CustomizableSOM.
     */
    private GridType gridType() {
        switch (topologyCmb.getSelectedIndex()) {
            case 1:
                return new SkewHexagonalGrid();
            default:
                return new SquareGrid();
        }
    }

    /**
     * Translate the learning rate selection in the form to a GridType object.
     *
     * @return a LearningRateFunction for use in a CustomizableSOM.
     */
    private LearningRateFunction learningRateType() {
        switch (learningRateCmb.getSelectedIndex()) {
            case 1:
                return new HyperbolicLearningRateFunction(0.8, 0.1);
            default:
                return new ConstantLearningRateFunction(0.2);
        }
    }

    /**
     * Translate the neighborhood width selection in the form to a GridType
     * object.
     *
     * @return a NeighborhoodWidthFunction for use in a CustomizableSOM.
     */
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

    /**
     * Translate the distance metric selection in the form to a GridType object.
     * 
     * @return a DistanceMetric for use in a CustomizableSOM.
     */
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

    /**
     * Validate the form's inputs.
     */
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

        setValid(pass);
    }

    /**
     * Get the actual JPanel for inclusion in a form.
     *
     * @return A JPanel for configuring a CustomizableSOM.
     */
    public JPanel getSOMBuilderConfigPanel() {
        return SOMBuilderConfigPanel;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}
