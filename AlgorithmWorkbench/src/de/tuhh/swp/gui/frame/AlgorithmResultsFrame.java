package de.tuhh.swp.gui.frame;

import de.tuhh.swp.Workbench;
import de.tuhh.swp.algorithm.AbstractAlgorithm;
import de.tuhh.swp.algorithm.AlgorithmFailure;
import de.tuhh.swp.algorithm.AlgorithmResult;
import de.tuhh.swp.gui.component.HeadingLabel;
import de.tuhh.swp.gui.preview.ImagePreview;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Tom on 13.01.2016.
 */
public class AlgorithmResultsFrame extends JFrame {

    private int previewImageId = 0;

    public AlgorithmResultsFrame(AbstractAlgorithm algorithm, AlgorithmResult result, Workbench workbench) {

        super("Results of " + algorithm.getName() + " test run.");
        setLayout(new MigLayout());
        setFocusable(true);

        JLabel heading = new HeadingLabel("Evaluation Results", 30);
        add(heading, "center, wrap 16");

        JLabel classifier = new HeadingLabel("Classifier Information", 20);
        add(classifier, "wrap 8");

        add(new JLabel("Algorithm Name: " + algorithm.getName()), "wrap");

        // Learning data information.
        add(new JLabel("Used Learning Samples: " + result.getLearnset().size()), "wrap");

        // Learning data distribution.
        JPanel distributionPanel = new JPanel();
        distributionPanel.setLayout(new MigLayout("", "[80!]10[80!]10[80!]", "[20!]4[20!]4[20!]"));
        for (int i = 0; i <= 9; ++i) {
            distributionPanel.add(new JLabel(i + ": " + result.getLearnset().getSampleCount((byte) i)), (i % 3 == 2 ? "wrap" : ""));
        }
        add(distributionPanel, "wrap 8");

        double successRate = (double) result.getCorrectAttempts() / (double) (result.getAttemptCount());
        add(new JLabel(
                "Correct guesses: " + result.getCorrectAttempts()
                        + " / " + result.getAttemptCount()
                        + " (" + String.format("%.2f%%", successRate * 100.0) + ")"
        ), "wrap");


        // There were wrong guesses.
        if (result.getCorrectAttempts() < result.getAttemptCount()) {
            ArrayList<AlgorithmFailure> failures = result.getFailures();
            ImagePreview image = new ImagePreview(failures.get(0).getKey(), 140);
            JLabel imageResult = new JLabel(
                    "Image Label: " + failures.get(0).getKey().getLabel().getValue()
                            + ", Guessed Label: " + failures.get(0).getValue().getValue()
            );

            JButton forwardButton = new JButton("");
            forwardButton.setIcon(new ImageIcon("res/right.png"));
            ((ImageIcon) forwardButton.getIcon()).setImage(((ImageIcon) forwardButton.getIcon()).getImage().getScaledInstance(140, 140, 0));
            forwardButton.setOpaque(false);
            forwardButton.setContentAreaFilled(false);
            forwardButton.setBorderPainted(false);
            forwardButton.addActionListener((ActionEvent e) -> {
                if (previewImageId < failures.size() - 1) {
                    image.setImage(failures.get((++previewImageId) % failures.size()).getKey());
                    imageResult.setText(
                            "Image Label: " + failures.get(previewImageId).getKey().getLabel().getValue()
                                    + ", Guessed Label: " + failures.get(previewImageId).getValue().getValue()
                    );
                    imageResult.repaint();
                    image.repaint();
                }
            });


            JButton backButton = new JButton("");
            backButton.setIcon(new ImageIcon("res/left.png"));
            ((ImageIcon) backButton.getIcon()).setImage(((ImageIcon) backButton.getIcon()).getImage().getScaledInstance(140, 140, 0));
            backButton.setOpaque(false);
            backButton.setContentAreaFilled(false);
            backButton.setBorderPainted(false);
            backButton.addActionListener((ActionEvent e) -> {
                if (previewImageId > 0) {
                    image.setImage(failures.get(--previewImageId).getKey());
                    imageResult.setText(
                            "Image Label: " + failures.get(previewImageId).getKey().getLabel().getValue()
                                    + ", Guessed Label: " + failures.get(previewImageId).getValue().getValue()
                    );
                    imageResult.repaint();
                    image.repaint();
                }
            });

            JPanel previewPanel = new JPanel();
            previewPanel.setLayout(new MigLayout("", "[140!]30[140!]0[140!]", ""));
            previewPanel.add(backButton, "grow");
            previewPanel.add(image);
            previewPanel.add(forwardButton, "grow, wrap 8");
            previewPanel.add(imageResult, "span 4, center");
            add(previewPanel);

            // Manouver using arrow keys.
            setFocusable(true);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        forwardButton.requestFocus(true);
                    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        backButton.doClick();
                    }
                }
            });
        }

        setLocationRelativeTo(workbench);
        pack();
        setVisible(true);
    }
}
