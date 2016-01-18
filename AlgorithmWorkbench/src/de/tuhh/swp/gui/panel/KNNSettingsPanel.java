package de.tuhh.swp.gui.panel;

import de.tuhh.swp.Workbench;
import de.tuhh.swp.algorithm.AbstractAlgorithm;
import de.tuhh.swp.algorithm.KNN;
import de.tuhh.swp.algorithm.LearningData;
import de.tuhh.swp.gui.component.HeadingLabel;
import de.tuhh.swp.image.ImageValue;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Tom on 13.01.2016.
 */
public class KNNSettingsPanel extends JPanel {

    public KNNSettingsPanel(Workbench workbench) {

        LinkedHashMap<String, JComponent> components = new LinkedHashMap<>();
        components.put("k", new SliderPanel("k", 1, 100, 5));
        components.put("learningSamples", new SliderPanel(
                "Learning Samples",
                0,
                workbench.getImages().length,
                workbench.getImages().length / 1000 * 999
        ));
        components.put("evaluationSamples", new SliderPanel(
                "Evaluation Samples",
                0,
                workbench.getImages().length,
                workbench.getImages().length / 1000
        ));
        final SliderPanel learningSamplesPanel = (SliderPanel) components.get("learningSamples");
        final SliderPanel evaluationSamplesPanel = (SliderPanel) components.get("evaluationSamples");
        learningSamplesPanel.addSliderChangeListener((ChangeEvent e) -> {
            evaluationSamplesPanel.setMaxValue(workbench.getImages().length - (int)learningSamplesPanel.getSliderValue());
            evaluationSamplesPanel.updateText();
        });
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
            for (int i = 0; i < (int) ((SliderPanel) components.get("learningSamples")).getSliderValue(); ++i) {
                learnset.add(images[i]);
            }

            System.out.println("Feeding k-Nearest-Neighbour algorithm " + learnset.size() + " learning samples...");
            knn.feed(learnset);

            workbench.performAlgorithmTestRun(knn, learnset, (int) ((SliderPanel) components.get("evaluationSamples")).getSliderValue());
        });

        setLayout(new MigLayout("", "", ""));
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
