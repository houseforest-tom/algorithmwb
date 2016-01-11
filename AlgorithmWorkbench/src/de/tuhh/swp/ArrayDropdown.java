package de.tuhh.swp;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tom on 06.01.2016.
 */
public class ArrayDropdown<T> extends JPanel {
    private T[] values;
    private JComboBox<T> dropdown;

    public ArrayDropdown(String label, T[] values) {
        this.values = values;
        dropdown = new JComboBox<>();
        dropdown.setModel(new DefaultComboBoxModel<>(values));
        add(new JLabel(label + ": "));
        add(dropdown);
        setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public T getSelection() {
        return values[dropdown.getSelectedIndex()];
    }
}
