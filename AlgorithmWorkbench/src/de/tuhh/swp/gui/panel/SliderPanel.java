package de.tuhh.swp.gui.panel;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
            updateText();
        });

        add(nameLabel);
        add(slider);
        add(currentValueLabel);
    }

    protected void updateText() {
        this.currentValueLabel.setText("" + Math.round(getSliderValue()));
    }

    public void addSliderChangeListener(ChangeListener onChange) {
        this.slider.addChangeListener(onChange);
    }

    public void setMaxValue(int max) {
        int prevMax = this.slider.getMaximum();
        this.slider.setMaximum(max);
        this.slider.setValue((int) ((double) this.slider.getValue() * ((double) max / (double) prevMax)));
        updateText();
    }

    public void setMinValue(int min) {
        int prevMin = this.slider.getMinimum();
        this.slider.setMinimum(min);
        int max = this.slider.getMaximum();
        if(max-prevMin == 0){
            this.slider.setValue(max);
        }else {
            this.slider.setValue(this.slider.getValue() * (max - min) / (max - prevMin));
        }
        updateText();
    }

    public float getSliderValue() {
        return (float) slider.getValue();
    }
}
