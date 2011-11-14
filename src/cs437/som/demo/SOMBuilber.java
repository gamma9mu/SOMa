package cs437.som.demo;

import cs437.som.SOMBuilderConfigPanel;
import cs437.som.TrainableSelfOrganizingMap;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SOMBuilber extends JDialog implements PropertyChangeListener {
    private static final long serialVersionUID = 0L;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private SOMBuilderConfigPanel mapConfigPanel;

    private TrainableSelfOrganizingMap map = null;

    private int inputSize;
    private int expectedIterations;

    public SOMBuilber(int inputSize, int expectedIterations) {
        this.inputSize = inputSize;
        this.expectedIterations = expectedIterations;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        mapConfigPanel.addPropertyChangeListener(this);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                map = mapConfigPanel.createSOM(SOMBuilber.this.inputSize,
                        SOMBuilber.this.expectedIterations);
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

    }

    public TrainableSelfOrganizingMap getMap() {
        return map;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        buttonOK.setEnabled(mapConfigPanel.isValid());
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
