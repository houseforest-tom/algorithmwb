package de.tuhh.swp;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * Created by Tom on 11.01.2016.
 */
public class NumPadPanel extends JPanel {
    private JRadioButton[] buttons;
    private ButtonGroup group;

    public NumPadPanel() {
        String[] numbers = new String[]{"7", "8", "9", "4", "5", "6", "1", "2", "3", "0"};
        this.setLayout(new MigLayout("", "[20!]10[20!]10[20!]", "[20!]10[20!]"));
        this.buttons = new JRadioButton[10];
        this.group = new ButtonGroup();
        String constraints = "";
        for (int i = 0; i < buttons.length; ++i) {

            if (i % 3 == 2) {
                constraints = "wrap";
            } else if (i == 9) {
                constraints = "center";
            } else {
                constraints = "";
            }

            buttons[i] = new JRadioButton(numbers[i]);
            group.add(buttons[i]);
            add(buttons[i], constraints);
        }
        this.buttons[this.buttons.length - 1].setSelected(true);
        this.setFocusable(true);
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
