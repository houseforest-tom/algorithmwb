package de.tuhh.swp;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tom on 11.01.2016.
 */
public class RadioButtonArray extends JPanel {
    private JRadioButton[] buttons;
    private ButtonGroup group;

    public RadioButtonArray(String[] labels) {
        this.setLayout(new FlowLayout());
        this.buttons = new JRadioButton[labels.length];
        this.group = new ButtonGroup();
        for (int i = 0; i < labels.length; ++i) {
            add(buttons[i] = new JRadioButton(labels[i]));
            group.add(buttons[i]);
        }
        this.buttons[0].setSelected(true);
    }

    public String getSelection() {
        for (int i = 0; i < buttons.length; ++i) {
            if (buttons[i].isSelected()) {
                return buttons[i].getText();
            }
        }
        return null;
    }

    public void setSelection(String label) {
        for (int i = 0; i < buttons.length; ++i) {
            if (buttons[i].getText().equals(label)) {
                buttons[i].setSelected(true);
            } else {
                buttons[i].setSelected(false);
            }
        }
    }
}
