package cs437.som.util;

import cs437.som.TrainableSelfOrganizingMap;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A dialog for gathering a self-organizing map's configuration from an
 * application's user.
 */
public class SOMBuilder extends JDialog implements PropertyChangeListener {
    private static final long serialVersionUID = 0L;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private SOMBuilderConfigPanel mapConfigPanel;

    private TrainableSelfOrganizingMap map = null;

    private final int inputSize;
    private final int expectedIterations;

    /**
     * Create a new SOMBuilder that will, if successful, return a trainable
     * self-organizing map that expects {@code inputSize} as the length of its
     * input vector and {@code expectedIterations} iterations of training.
     * 
     * @param inputSize The input vector length for the generated map.
     * @param expectedIterations  The expected amount of training iterations
     * for the generated map.
     */
    public SOMBuilder(int inputSize, int expectedIterations) {
        this.inputSize = inputSize;
        this.expectedIterations = expectedIterations;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        mapConfigPanel.addPropertyChangeListener(this);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                map = mapConfigPanel.createSOM(SOMBuilder.this.inputSize,
                        SOMBuilder.this.expectedIterations);
                dispose();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { dispose(); }
        });

        // call dispose() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { dispose(); }
        });

        // call dispose() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
    }

    /**
     * Get the self-organizing map as configured by input to the form, if one
     * exists.
     *
     * @return A trainable self-organizing map if the user selected 'OK.'  The
     * return result will be {@code null} if the user cancelled the dialog.
     */
    public TrainableSelfOrganizingMap getMap() {
        return map;
    }

    /**
     * Listen for validity changes on the SOMBuilderConfigPanel.
     *
     * {@inheritDoc}
     */
    public void propertyChange(PropertyChangeEvent evt) {
        buttonOK.setEnabled(mapConfigPanel.isValid());
    }

    @Override
    public String toString() {
        return "SOMBuilder";
    }
}
