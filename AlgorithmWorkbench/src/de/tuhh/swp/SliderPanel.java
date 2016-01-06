package de.tuhh.swp;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

/**
 * Created by Tom on 06.01.2016.
 */
public class SliderPanel extends JPanel {
    private JLabel nameLabel;
    private JSlider slider;
    private JLabel currentValueLabel;

    public SliderPanel(String label, int min, int max, int value) {
        setLayout(new FlowLayout());

        this.nameLabel = new JLabel(label + ": ");
        this.slider = new JSlider(min, max, value);
        this.currentValueLabel = new JLabel("" + value);
        this.slider.addChangeListener((ChangeEvent e) -> {
            currentValueLabel.setText("" + slider.getValue());
        });

        add(nameLabel);
        add(slider);
        add(currentValueLabel);

        setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public int getSliderValue(){
        return slider.getValue();
    }
}
