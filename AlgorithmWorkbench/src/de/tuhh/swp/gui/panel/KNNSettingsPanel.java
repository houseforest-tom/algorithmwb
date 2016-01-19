package de.tuhh.swp.gui.panel;

import de.tuhh.swp.Workbench;
import de.tuhh.swp.algorithm.*;
import de.tuhh.swp.gui.component.HeadingLabel;
import de.tuhh.swp.image.ImageValue;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Tom on 13.01.2016.
 */
public class KNNSettingsPanel extends JPanel {

    // Possible settings for k.
    public static final int K_MIN = 1;
    public static final int K_MAX = 15;
    public static final int K_DEFAULT = 5;

    public KNNSettingsPanel(Workbench workbench) {

        // Total number of sample images.
        final int numImages = workbench.getImages().length;

        LinkedHashMap<String, JComponent> components = new LinkedHashMap<>();
        components.put("k", new SliderPanel("k", K_MIN, K_MAX, K_DEFAULT));
        components.put("sampleSettings", new SampleSettingsPanel(workbench));
        components.put("distanceMeasure", new ArrayDropdownPanel<>(
                "Distance Measure",
                AbstractAlgorithm.DistanceMeasure.values()
        ));

        // Runs the algorithm against test samples.
        JButton performButton = new JButton("Perform Algorithm");

        // -------------------------------

        // Add button listener.
        performButton.addActionListener((ActionEvent event) -> {

            KNN knn;
            ImageValue[] images = workbench.getImages();
            workbench.setKNNAlgorithm(knn = new KNN(
                    (int) ((SliderPanel) components.get("k")).getSliderValue(),
                    images[0].getDefinition(),
                    ((ArrayDropdownPanel<AbstractAlgorithm.DistanceMeasure>) components.get("distanceMeasure")).getSelection()
            ));

            LearningData learnset = new LearningData();
            SampleSettingsPanel settings = (SampleSettingsPanel) components.get("sampleSettings");
            int offset = settings.getSetting(SampleSettingsPanel.LEARNING_SAMPLES_OFFSET);
            int end = offset + settings.getSetting(SampleSettingsPanel.LEARNING_SAMPLES_COUNT);
            Example example;
            for (int i = offset; i < end; ++i) {
                example = new Example(Schema.DEFAULT_SCHEMA);
                example.setImage(images[i]);
                learnset.add(example);
            }

            Workbench.Debug.println("Feeding k-Nearest-Neighbour algorithm " + learnset.size() + " learning samples...");
            knn.feed(learnset);

            workbench.performAlgorithmTestRun(
                    knn,
                    learnset,
                    settings.getSetting(SampleSettingsPanel.EVALUATION_SAMPLES_OFFSET),
                    settings.getSetting(SampleSettingsPanel.EVALUATION_SAMPLES_COUNT)
            );
        });

        setLayout(new MigLayout());
        add(new HeadingLabel("k-Nearest-Neighbour Algorithm", 30), "center, wrap 16");
        for (Map.Entry<String, JComponent> component : components.entrySet()) {
            add(component.getValue(), "wrap");
        }
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(performButton);
        add(Box.createVerticalStrut(0), "wrap 16");
        add(buttonPanel, "center");
    }
}
