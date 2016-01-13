package de.tuhh.swp.gui.panel;

import javax.swing.event.ChangeEvent;

/**
 * Created by Tom on 11.01.2016.
 */
public class FloatSliderPanel extends SliderPanel {

    private float min, max;

    public FloatSliderPanel(String label, float min, float max, float value) {
        super(label, 0, 100, (int) (value / (max - min) * 100));
        this.min = min;
        this.max = max;
        super.currentValueLabel.setText("" + getSliderValue());
        this.slider.addChangeListener((ChangeEvent e) -> {
            currentValueLabel.setText("" + getSliderValue());
        });
    }

    @Override
    public float getSliderValue() {
        return super.getSliderValue() / 100.0f * (max - min) + min;
    }
}
