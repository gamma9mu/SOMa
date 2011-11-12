/*
 * Copyright (c) 2011 Brian A. Guthrie, Colin Murphy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *     2. Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *     3. The name of the authors may not be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHORS ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package cs437.som;

import cs437.som.demo.SOMBuilber;
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
 * gamma @ 11/11/11 1:15 PM
 */
public class SOMBuilberConfigPanel {
    private JComboBox learningRateCmb;
    private JComboBox distanceCmb;
    private JComboBox neighborhoodCmb;
    private JTextField widthText;
    private JTextField heightText;
    private JComboBox topologyCmb;
    private JLabel lblWidth;
    private JLabel lblTopology;
    private JLabel lblHeight;
    private JPanel topologyPanel;

    private Pattern positiveInteger = Pattern.compile("[1-9]\\d*");
    private boolean valid = false;

    public SOMBuilberConfigPanel() {
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
