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
public class KMeanSettingsPanel extends JPanel {
    public KMeanSettingsPanel(Workbench workbench) {

        LinkedHashMap<String, JComponent> components = new LinkedHashMap<>();
        components.put("k", new SliderPanel("k", 1, 100, 20));
        components.put("iterations", new SliderPanel("Iterations", 1, 500, 20));
        components.put("minDelta", new FloatSliderPanel("Min. Delta", 1.0f, 100.0f, 5.0f));
        components.put("learningSamples", new SliderPanel(
                "Learning Samples",
                0,
                workbench.getImages().length,
                workbench.getImages().length / 2
        ));
        components.put("evaluationSamples", new SliderPanel(
                "Evaluation Samples",
                0,
                workbench.getImages().length,
                workbench.getImages().length / 2
        ));
        components.put("distanceMeasure", new ArrayDropdown<>(
                "Distance Measure",
                AbstractAlgorithm.DistanceMeasure.values()
        ));
        components.put("initialClusters", new ArrayDropdown<>("Initial Clusters", new String[]{
                "Random Generation",
                "Random Selection"
        }));

        // -------------------------------

        ImageValue[] images = workbench.getImages();
        LearningData learnset = new LearningData();

        // Initiates cluster search.
        JButton searchButton = new JButton("Search Clusters");

        // Runs algorithm against test samples.
        JButton performButton = new JButton("Perform Algorithm");

        // Add button listeners.
        searchButton.addActionListener((ActionEvent event) -> {
            KMean kmean;
            workbench.setKMeanAlgorithm(kmean = new KMean(
                    (int) ((SliderPanel) components.get("k")).getSliderValue(),
                    images[0].getDefinition(),
                    ((ArrayDropdown<AbstractAlgorithm.DistanceMeasure>) components.get("distanceMeasure")).getSelection()
            ));

            // Add learning samples.
            learnset.clear();
            for (int i = 0; i < (int) ((SliderPanel) components.get("learningSamples")).getSliderValue(); ++i) {
                learnset.add(images[i]);
            }

            System.out.println("Feeding k-Mean algorithm " + learnset.size() + " learning samples...");
            kmean.feed(
                    (int) ((SliderPanel) components.get("iterations")).getSliderValue(),
                    ((SliderPanel) components.get("minDelta")).getSliderValue(),
                    learnset,
                    ((ArrayDropdown<String>) components.get("initialClusters")).getSelection().equals("Random Generation")
            );
            System.out.println("Finished searching " + kmean.getClusters().length + " clusters, please assign labels.");

            // Open cluster assignment view.
            new KMeanClusterAssignmentFrame(workbench).setVisible(true);
        });

        performButton.addActionListener((ActionEvent event) -> {
            int attempts = (int) ((SliderPanel) components.get("evaluationSamples")).getSliderValue();
            int correctAttemptCount = 0;
            int offset = learnset.size();
            int end = Math.min(offset + attempts, images.length);
            attempts = end - offset; // Adjust attempt count.

            // Construct empty algorithm result.
            AlgorithmResult result = new AlgorithmResult(
                    attempts,
                    learnset
            );

            System.out.println("Evaluating " + attempts + " samples with k-Mean algorithm...");
            byte guessedLabel;
            for (int i = offset; i < end; ++i) {
                if ((guessedLabel = workbench.getKMeanAlgorithm().evaluate(images[i])) == images[i].getLabel()) {
                    correctAttemptCount++;
                } else {
                    result.addFailure(new AlgorithmFailure(images[i], guessedLabel));
                }
            }

            // Update correct attempts.
            result.setCorrectAttemptCount(correctAttemptCount);


            // Open results page.
            new KMeanResultsFrame(result, workbench).setVisible(true);
        });

        setLayout(new MigLayout("", "", ""));
        JLabel heading = new JLabel("k-Mean Algorithm");
        heading.setFont(new Font("Serif", Font.BOLD, 30));
        add(heading, "center, wrap 16");
        for (Map.Entry<String, JComponent> component : components.entrySet()) {
            add(component.getValue(), "wrap");
        }
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(searchButton);
        buttonPanel.add(performButton);
        add(Box.createVerticalStrut(0), "wrap 16");
        add(buttonPanel, "center");
    }
}
