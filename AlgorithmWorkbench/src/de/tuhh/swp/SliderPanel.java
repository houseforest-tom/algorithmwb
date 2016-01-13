package de.tuhh.swp;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

/**
 * Created by Tom on 06.01.2016.
 */
public class SliderPanel extends JPanel {
    protected JLabel nameLabel;
    protected JSlider slider;
    protected JLabel currentValueLabel;

    public SliderPanel(String label, int min, int max, int value) {
        setLayout(new MigLayout("", "[100!]20[200!]20[70!]", ""));

        this.nameLabel = new JLabel(label + ": ");
        this.slider = new JSlider(min, max, value);
        this.currentValueLabel = new JLabel("" + value);
        this.slider.addChangeListener((ChangeEvent e) -> {
            currentValueLabel.setText("" + getSliderValue());
        });

        add(nameLabel);
        add(slider);
        add(currentValueLabel);
    }

    public float getSliderValue() {
        return (float) slider.getValue();
    }
}
