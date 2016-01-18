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
        updateText();
        this.slider.addChangeListener((ChangeEvent e) -> {
            currentValueLabel.setText("" + getSliderValue());
        });
    }

    public void setMaxValue(float max){
        this.max = max;
        updateText();
    }

    @Override
    public float getSliderValue() {
        return super.getSliderValue() / 100.0f * (max - min) + min;
    }
}
