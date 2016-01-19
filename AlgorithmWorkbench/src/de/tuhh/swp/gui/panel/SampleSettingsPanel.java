package de.tuhh.swp.gui.panel;

import de.tuhh.swp.Workbench;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

/**
 * Created by Tom on 19.01.2016.
 */
public class SampleSettingsPanel extends JPanel {

    public static final int LEARNING_SAMPLES_OFFSET = 0;
    public static final int LEARNING_SAMPLES_COUNT = 1;
    public static final int EVALUATION_SAMPLES_OFFSET = 2;
    public static final int EVALUATION_SAMPLES_COUNT = 3;

    private SliderPanel[] sliders;

    public SampleSettingsPanel(Workbench workbench) {

        setLayout(new MigLayout());

        this.sliders = new SliderPanel[4];

        final int numImages = workbench.getImages().length;

        sliders[LEARNING_SAMPLES_OFFSET] = new SliderPanel(
                "Start Index",
                0,
                numImages - 2,
                0
        );

        sliders[LEARNING_SAMPLES_COUNT] = new SliderPanel(
                "Count",
                1,
                numImages - 1,
                1
        );

        sliders[EVALUATION_SAMPLES_OFFSET] = new SliderPanel(
                "Start Index",
                1,
                numImages - 1,
                1
        );

        sliders[EVALUATION_SAMPLES_COUNT] = new SliderPanel(
                "Count",
                1,
                numImages - 1,
                1
        );

        // Listen to changes to learning sample offset.
        sliders[LEARNING_SAMPLES_OFFSET].addSliderChangeListener((ChangeEvent e) -> {

            // Adjust maximum number of learning samples.
            sliders[LEARNING_SAMPLES_COUNT].setMaxValue((int) (numImages - sliders[LEARNING_SAMPLES_OFFSET].getSliderValue() - 1));

            // Adjust minimum evaluation sample offset.
            sliders[EVALUATION_SAMPLES_OFFSET].setMinValue((int) (sliders[LEARNING_SAMPLES_COUNT].getSliderValue() + sliders[LEARNING_SAMPLES_OFFSET].getSliderValue()));
        });

        // Listen to changes to learning sample count.
        sliders[LEARNING_SAMPLES_COUNT].addSliderChangeListener((ChangeEvent e) -> {

            // Adjust minimum evaluation sample offset.
            sliders[EVALUATION_SAMPLES_OFFSET].setMinValue((int) (sliders[LEARNING_SAMPLES_COUNT].getSliderValue() + sliders[LEARNING_SAMPLES_OFFSET].getSliderValue()));
        });

        // Listen to changes to evaluation sample offset.
        sliders[EVALUATION_SAMPLES_OFFSET].addSliderChangeListener((ChangeEvent e) -> {

            // Adjust maximum evaluation sample count.
            sliders[EVALUATION_SAMPLES_COUNT].setMaxValue((int) (numImages - sliders[EVALUATION_SAMPLES_OFFSET].getSliderValue()));
        });

        add(new JLabel("Learning Samples:"), "wrap 8");
        this.add(sliders[LEARNING_SAMPLES_OFFSET], "wrap");
        this.add(sliders[LEARNING_SAMPLES_COUNT], "wrap 12");
        add(new JLabel("Evaluation Samples:"), "wrap 8");
        this.add(sliders[EVALUATION_SAMPLES_OFFSET], "wrap");
        this.add(sliders[EVALUATION_SAMPLES_COUNT], "wrap");
    }

    public int getSetting(int settingId){
        if(settingId >= this.sliders.length){
            System.err.println("Invalid setting id.");
            return -1;
        }else{
            return (int)this.sliders[settingId].getSliderValue();
        }
    }
}
