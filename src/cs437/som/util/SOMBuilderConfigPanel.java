package cs437.som.util;

import cs437.som.*;
import cs437.som.Dimension;
import cs437.som.distancemetrics.ChebyshevDistanceMetric;
import cs437.som.distancemetrics.EuclideanDistanceMetric;
import cs437.som.distancemetrics.ManhattanDistanceMetric;
import cs437.som.learningrate.ConstantLearningRateFunction;
import cs437.som.learningrate.ExponentialDecayLearningRateFunction;
import cs437.som.learningrate.HyperbolicLearningRateFunction;
import cs437.som.membership.ConstantNeighborhoodMembershipFunction;
import cs437.som.membership.ExponentialNeighborhoodMembershipFunction;
import cs437.som.membership.GeometricNeighborhoodMembershipFunction;
import cs437.som.membership.LinearNeighborhoodMembershipFunction;
import cs437.som.neighborhood.*;
import cs437.som.network.CustomizableSOM;
import cs437.som.topology.*;

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
    private JComboBox<String> learningRateCmb;
    private JComboBox<String> distanceCmb;
    private JComboBox<String> neighborhoodCmb;
    private JTextField widthText;
    private JTextField heightText;
    private JComboBox<String> topologyCmb;
    private JPanel SOMBuilderConfigPanel;
    private JComboBox<String> membershipCmb;

    private final Pattern positiveInteger = Pattern.compile("[1-9]\\d*");
    private boolean valid = false;
    private final PropertyChangeSupport propertyChangeSupport =
            new SwingPropertyChangeSupport(this, true);

    /**
     * Create a new SOMBuilderConfigPanel.
     */
    public SOMBuilderConfigPanel() {
        topologyCmb.addItem("Square Grid");
        topologyCmb.addItem("Toroidal Square Grid");
        topologyCmb.addItem("vN Square Grid");
        topologyCmb.addItem("Moore Square Grid");
        topologyCmb.addItem("Skew Hexagonal Grid");
        topologyCmb.addItem("Offset Hexagonal Grid");

        learningRateCmb.addItem("Constant Learning Rate Function");
        learningRateCmb.addItem("Hyperbolic Learning Rate Function");
        learningRateCmb.addItem("Exponential Decay Learning Rate Function");

        neighborhoodCmb.addItem("Constant Neighborhood Width Function");
        neighborhoodCmb.addItem("Linear Decay Neighborhood Width Function");
        neighborhoodCmb.addItem("Hyperbolic Neighborhood Width Function");
        neighborhoodCmb.addItem("Exponential Decay Neighborhood Width");
        neighborhoodCmb.addItem("Gaussian Neighborhood Width Function");
        neighborhoodCmb.addItem("Mexican Hat Neighborhood Width Function");

        membershipCmb.addItem("Constant Neighborhood Membership Function");
        membershipCmb.addItem("Linear Neighborhood Membership Function");
        membershipCmb.addItem("Geometric Neighborhood Membership Function");
        membershipCmb.addItem("Exponential Neighborhood Membership Function");

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
        CustomizableSOM som = new CustomizableSOM(dimension(), inputSize, expectedIterations);

        gridType(som);
        learningRateType(som);
        neighborhoodType(som);
        membershipType(som);
        distanceType(som);

        return som;
    }

    /**
     * Translate the dimensions in the form input into a Dimension object.
     * 
     * @return The {@code Dimension} corresponding to the forms input.
     */
    private Dimension dimension() {
        int x = Integer.parseInt(widthText.getText());
        int y = Integer.parseInt(heightText.getText());
        return new Dimension(x, y);
    }

    /**
     * Translate the topology selection in the form to a GridType object and
     * set a map's grid type to that.
     *
     * @param map The map who's grid type will be set.
     */
    private void gridType(CustomizableSOM map) {
        GridType gridType;
        switch (topologyCmb.getSelectedIndex()) {
            case 1:
                gridType = new ToroidalEuclideanSquare();
                break;
            case 2:
                gridType = new VonNeumann();
                break;
            case 3:
                gridType = new Moore();
                break;
            case 4:
                gridType = new SkewHexagonalGrid();
                break;
            case 5:
                gridType = new OffsetHexagonalGrid();
                break;
            default:
                gridType = new SquareGrid();
        }
        map.setGridTypeStrategy(gridType);
    }

    /**
     * Translate the learning rate selection into the form to a GridType object
     * and set a map's learning rate function to that.
     *
     * @param map The map who's learning rate function will be set.
     */
    private void learningRateType(CustomizableSOM map) {
        LearningRateFunction lrf;
        switch (learningRateCmb.getSelectedIndex()) {
            case 1:
                lrf = new HyperbolicLearningRateFunction(0.8, 0.1);
                break;
            case 2:
                lrf = new ExponentialDecayLearningRateFunction(0.8);
                break;
            default:
                lrf =  new ConstantLearningRateFunction(0.2);
        }
        map.setLearningRateFunctionStrategy(lrf);
    }

    /**
     * Translate the neighborhood width selection into the form to a GridType
     * object and set a map's neighborhood function to that.
     *
     * @param map The map whose neighborhood function will be set.
     */
    private void neighborhoodType(CustomizableSOM map) {
        NeighborhoodWidthFunction nwf;
        switch (neighborhoodCmb.getSelectedIndex()) {
            case 1:
                nwf = new LinearDecayNeighborhoodWidthFunction(10.0);
                break;
            case 2:
                nwf = new HyperbolicNeighborhoodWidthFunction(10.0, 1.0);
                break;
            case 3:
                nwf = new ExponentialDecayNeighborhoodWidth(10.0);
                break;
            case 4:
                nwf = new GaussianNeighborhoodWidthFunction(3.0);
                break;
            case 5:
                nwf = new MexicanHatNeighborhoodWidthFunction(3.0);
                break;
            default:
                nwf = new ConstantNeighborhoodWidthFunction(1.0);
        }
        map.setNeighborhoodWidthFunctionStrategy(nwf);
    }

    /**
     * Translate the neighborhood membership selection into the form to a
     * GridType object and set a map's neighborhood function to that.
     *
     * @param map The map whose neighborhood function will be set.
     */
    private void membershipType(CustomizableSOM map) {
        NeighborhoodMembershipFunction nmf;
        switch (neighborhoodCmb.getSelectedIndex()) {
            case 1:
                nmf = new LinearNeighborhoodMembershipFunction();
                break;
            case 2:
                nmf = new GeometricNeighborhoodMembershipFunction(.5);
                break;
            case 3:
                nmf = new ExponentialNeighborhoodMembershipFunction();
                break;
            default:
                nmf = new ConstantNeighborhoodMembershipFunction(1);
        }
        map.setNeighborhoodMembershipFunctionStrategy(nmf);
    }

    /**
     * Translate the distance metric selection into the form to a GridType
     * object and set a map's distance type to that.
     *
     * @param map The map who's distance type will be set
     */
    private void distanceType(CustomizableSOM map) {
        DistanceMetric dm;
        switch (distanceCmb.getSelectedIndex()) {
            case 1:
                dm = new ChebyshevDistanceMetric();
                break;
            case 2:
                dm = new ManhattanDistanceMetric();
                break;
            default:
                dm = new EuclideanDistanceMetric();
        }
        map.setDistanceMetricStrategy(dm);
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

    /**
     * Add a property listener.  Updates will be sent when a change in the
     * validity of the input occurs.
     *
     * @param listener The listener to add.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Stop an object from receiving updates on the validity of the form.
     *
     * @param listener The listener to remove.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        return "SOMBuilderConfigPanel";
    }
}
