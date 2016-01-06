package de.tuhh.swp;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tom on 06.01.2016.
 */
public class DistanceMeasureDropdown extends JPanel {
    private JComboBox<AbstractAlgorithm.DistanceMeasure> dropdown;

    public DistanceMeasureDropdown() {
        dropdown = new JComboBox<>();
        dropdown.setModel(new DefaultComboBoxModel<>(AbstractAlgorithm.DistanceMeasure.values()));
        add(new JLabel("Select Distance Measure: "));
        add(dropdown);
        setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
