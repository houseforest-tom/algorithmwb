package de.tuhh.swp.gui.panel;

import de.tuhh.swp.gui.component.HeadingLabel;
import de.tuhh.swp.gui.frame.KMeanClusterAssignmentFrame;
import de.tuhh.swp.gui.frame.AlgorithmResultsFrame;
import de.tuhh.swp.Workbench;
import de.tuhh.swp.algorithm.*;
import de.tuhh.swp.image.ImageValue;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
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
        components.put("initialClusters", new ArrayDropdownPanel<>("Initial Clusters", new String[]{
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
                    ((ArrayDropdownPanel<AbstractAlgorithm.DistanceMeasure>) components.get("distanceMeasure")).getSelection()
            ));

            // Add learning samples.
            learnset.clear();
            for (int i = 0; i < (int) ((SliderPanel) components.get("learningSamples")).getSliderValue(); ++i) {
                learnset.add(images[i]);
            }

            System.out.println("Feeding k-Mean algorithm " + learnset.size() + " learning samples...");
            kmean.setIterations((int) ((SliderPanel) components.get("iterations")).getSliderValue());
            kmean.setThreshold(((SliderPanel) components.get("minDelta")).getSliderValue());
            kmean.setInitialClusterRNG(((ArrayDropdownPanel<String>) components.get("initialClusters")).getSelection().equals("Random Generation"));
            kmean.feed(learnset);
            System.out.println("Finished searching " + kmean.getClusters().length + " clusters, please assign labels.");

            // Open cluster assignment view.
            new KMeanClusterAssignmentFrame(workbench).setVisible(true);
        });

        performButton.addActionListener((ActionEvent event) -> {
            workbench.performAlgorithmTestRun(
                    workbench.getKMeanAlgorithm(),
                    learnset,
                    (int) ((SliderPanel) components.get("evaluationSamples")).getSliderValue()
            );
        });

        setLayout(new MigLayout("", "", ""));
        add(new HeadingLabel("k-Mean Algorithm", 30), "center, wrap 16");
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
