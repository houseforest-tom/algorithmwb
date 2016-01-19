package de.tuhh.swp.gui.panel;

import de.tuhh.swp.Workbench;
import de.tuhh.swp.algorithm.AbstractAlgorithm;
import de.tuhh.swp.algorithm.KMean;
import de.tuhh.swp.algorithm.LearningData;
import de.tuhh.swp.gui.component.HeadingLabel;
import de.tuhh.swp.gui.frame.KMeanClusterAssignmentFrame;
import de.tuhh.swp.image.ImageValue;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Tom on 13.01.2016.
 */
public class KMeanSettingsPanel extends JPanel {

    // Possible settings for k.
    public static final int K_MIN = 1;
    public static final int K_MAX = 60;
    public static final int K_DEFAULT = 10;

    // Possible settings for iteration count.
    public static final int ITER_MIN = 1;
    public static final int ITER_MAX = 500;
    public static final int ITER_DEFAULT = 30;

    // Possible settings for min delta.
    public static final float THRESH_MIN = 1;
    public static final float THRESH_MAX = 500;
    public static final float THRESH_DEFAULT = 30;

    public KMeanSettingsPanel(Workbench workbench) {

        LinkedHashMap<String, JComponent> components = new LinkedHashMap<>();

        components.put("k", new SliderPanel("k", K_MIN, K_MAX, K_DEFAULT));
        components.put("iterations", new SliderPanel("Iterations", ITER_MIN, ITER_MAX, ITER_DEFAULT));
        components.put("minDelta", new FloatSliderPanel("Min. Delta", THRESH_MIN, THRESH_MAX, THRESH_DEFAULT));
        components.put("sampleSettings", new SampleSettingsPanel(workbench));

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
        final SampleSettingsPanel settings = (SampleSettingsPanel) components.get("sampleSettings");

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

            int offset = settings.getSetting(SampleSettingsPanel.LEARNING_SAMPLES_OFFSET);
            int end = offset + settings.getSetting(SampleSettingsPanel.LEARNING_SAMPLES_COUNT);

            // Add learning samples.
            learnset.clear();
            for (int i = offset; i < end; ++i) {
                learnset.add(images[i]);
            }

            Workbench.Debug.println("Feeding k-Mean algorithm " + learnset.size() + " learning samples...");
            kmean.setIterations((int) ((SliderPanel) components.get("iterations")).getSliderValue());
            kmean.setThreshold(((SliderPanel) components.get("minDelta")).getSliderValue());
            kmean.setInitialClusterRNG(((ArrayDropdownPanel<String>) components.get("initialClusters")).getSelection().equals("Random Generation"));
            kmean.feed(learnset);
            Workbench.Debug.println("Finished searching " + kmean.getClusters().length + " clusters, please assign labels.");

            // Open cluster assignment view.
            new KMeanClusterAssignmentFrame(workbench).setVisible(true);
        });

        performButton.addActionListener((ActionEvent event) -> {
            workbench.performAlgorithmTestRun(
                    workbench.getKMeanAlgorithm(),
                    learnset,
                    settings.getSetting(SampleSettingsPanel.EVALUATION_SAMPLES_OFFSET),
                    settings.getSetting(SampleSettingsPanel.EVALUATION_SAMPLES_COUNT)
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
