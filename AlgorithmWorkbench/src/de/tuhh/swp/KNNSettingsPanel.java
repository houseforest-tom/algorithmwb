package de.tuhh.swp;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
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
        components.put("distanceMeasure", new ArrayDropdown<>(
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
                    ((ArrayDropdown<AbstractAlgorithm.DistanceMeasure>) components.get("distanceMeasure")).getSelection()
            ));

            LearningData learnset = new LearningData();
            for (int i = 0; i < (int) ((SliderPanel) components.get("learningSamples")).getSliderValue(); ++i) {
                learnset.add(images[i]);
            }

            System.out.println("Feeding k-Nearest-Neighbour algorithm " + learnset.size() + " learning samples...");
            knn.feed(learnset);

            int attempts = (int) ((SliderPanel) components.get("evaluationSamples")).getSliderValue();
            int correctGuesses = 0;
            int offset = learnset.size();
            int end = Math.min(offset + attempts, images.length);
            attempts = end - offset; // Adjust attempt count.
            System.out.println("Evaluating " + attempts + " samples with k-Nearest-Neighbour algorithm...");
            for (int i = offset; i < end; ++i) {
                if (knn.evaluate(images[i]) == images[i].getLabel()) {
                    correctGuesses++;
                }
            }
            System.out.println("k-Nearest-Neighbour guessed " + (double) correctGuesses / (double) (attempts) * 100.0 + "% correctly.");
        });

        setLayout(new MigLayout("", "", ""));
        JLabel heading = new JLabel("k-Nearest-Neighbour Algorithm");
        heading.setFont(new Font("Serif", Font.BOLD, 30));
        add(heading, "center, wrap 16");
        for (Map.Entry<String, JComponent> component : components.entrySet()) {
            add(component.getValue(), "wrap");
        }
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(performButton);
        add(Box.createVerticalStrut(0), "wrap 16");
        add(buttonPanel, "center");
    }
}
