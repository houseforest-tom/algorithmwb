package de.tuhh.swp.gui.panel;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tom on 06.01.2016.
 */
public class ArrayDropdownPanel<T> extends JPanel {
    private T[] values;
    private JComboBox<T> dropdown;

    public ArrayDropdownPanel(String label, T[] values) {
        this.values = values;
        setLayout(new MigLayout("", "[100!]20[200!]", ""));
        dropdown = new JComboBox<>();
        dropdown.setModel(new DefaultComboBoxModel<>(values));
        add(new JLabel(label + ": "));
        add(dropdown);
    }

    public T getSelection() {
        return values[dropdown.getSelectedIndex()];
    }
}
