package de.tuhh.swp.gui.frame;

import de.tuhh.swp.Workbench;
import de.tuhh.swp.algorithm.KMean;
import de.tuhh.swp.gui.preview.ClusterPreview;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Tom on 13.01.2016.
 */
public class KMeanClusterAssignmentFrame extends JFrame {

    private Workbench workbench;

    public KMeanClusterAssignmentFrame(Workbench workbench) {

        super("Assign k-Mean Clusters");
        this.workbench = workbench;
        setLocationRelativeTo(workbench);
        setLayout(new MigLayout());

        KMean kmean = workbench.getKMeanAlgorithm();
        JPanel view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));

        // Create cluster previews.
        ClusterPreview[] previews = new ClusterPreview[kmean.getClusters().length];
        for (int i = 0; i < previews.length; ++i) {
            previews[i] = new ClusterPreview(kmean.getClusters()[i]);
        }

        // Position preview frames.
        int rowLen = 5;
        JPanel[] rows = new JPanel[(int) Math.ceil(kmean.getClusters().length / (float) rowLen)];
        for (int row = 0; row < rows.length; ++row) {
            rows[row] = new JPanel();
            rows[row].setLayout(new FlowLayout());
            for (int i = 0; i < rowLen && (row * rowLen + i) < previews.length; ++i) {
                rows[row].add(previews[row * rowLen + i]);
                previews[row * rowLen + i].repaint();
            }
            view.add(rows[row]);
        }

        view.setVisible(true);
        add(view, "wrap 8");
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener((ActionEvent e) -> confirm());
        add(confirmButton, "center, w 140!");

        // Window was closed.
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                confirm();
            }
        });

        pack();
    }

    // Confirm assigned cluster labels.
    private void confirm() {
        boolean complete = true;
        KMean kmean = workbench.getKMeanAlgorithm();
        for (int i = 0; i < kmean.getClusters().length; ++i) {
            if (!kmean.getClusters()[i].isAssigned()) {
                complete = false;
            }
        }
        if (complete) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(workbench, "Please assign labels to remaining clusters.", "Assignments missing!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
