package de.tuhh.swp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom on 06.01.2016.
 */
public class Menu extends JMenuBar {

    private Workbench workbench;
    private HashMap<String, JMenu> submenus;
    private String labelsFilePath, imagesFilePath;
    private JPanel knnView, kmeanView;

    public Menu(Workbench workbench) {

        this.workbench = workbench;
        submenus = new HashMap<>();

        // Learning data dropdown.
        submenus.put("data", new JMenu("Learning Data"));
        {
            // Select labels file.
            JMenuItem labelsItem = new JMenuItem("Select Labels File", KeyEvent.VK_L);
            labelsItem.addActionListener((ActionEvent e) -> {
                selectLabelsFile();
            });
            submenus.get("data").add(labelsItem);

            // Select images file.
            JMenuItem imagesItem = new JMenuItem("Select Images File", KeyEvent.VK_I);
            imagesItem.addActionListener((ActionEvent e) -> {
                selectImagesFile();
            });
            submenus.get("data").add(imagesItem);

            // Update database.
            JMenuItem updateItem = new JMenuItem("Update Database", KeyEvent.VK_U);
            updateItem.addActionListener((ActionEvent e) -> {
                updateDatabase();
            });
            submenus.get("data").add(updateItem);
        }

        submenus.put("algos", new JMenu("Algorithms"));
        {
            // k-nearest-neighbour.
            JMenuItem knnItem = new JMenuItem("k-Nearest-Neighbour", KeyEvent.VK_N);
            knnItem.addActionListener((ActionEvent e) -> {
                openKNNView();
            });
            submenus.get("algos").add(knnItem);

            // k-mean.
            JMenuItem kmeanItem = new JMenuItem("k-Mean", KeyEvent.VK_M);
            kmeanItem.addActionListener((ActionEvent e) -> {
                openKMeanView();
            });
            submenus.get("algos").add(kmeanItem);
        }

        for (Map.Entry<String, JMenu> item : submenus.entrySet()) {
            add(item.getValue());
        }

        createAlgorithmViews();
    }

    private void selectLabelsFile() {
        JFileChooser chooser = new JFileChooser(new File("./"));
        chooser.setVisible(true);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            labelsFilePath = chooser.getSelectedFile().getAbsolutePath();
        }
    }

    private void selectImagesFile() {
        JFileChooser chooser = new JFileChooser(new File("./"));
        chooser.setVisible(true);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            imagesFilePath = chooser.getSelectedFile().getAbsolutePath();
        }
    }

    private void updateDatabase() {
        try {
            byte[] labelFile = Files.readAllBytes(Paths.get(labelsFilePath));
            byte[] labels = new LabelConverter().toInternal(labelFile);
            byte[] imagesFile = Files.readAllBytes(Paths.get(imagesFilePath));
            ImageValue[] images = new ImageConverter(workbench, labels).toInternal(imagesFile);
            workbench.setImages(images);
            System.out.println("Loaded " + images.length + " images.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openKNNView() {
        System.out.println("Opening KNN view.");
        workbench.setContent(knnView);
    }

    private void openKMeanView() {
        System.out.println("Opening KMean view.");
        workbench.setContent(kmeanView);
    }

    private void createAlgorithmViews() {
        knnView = createKNNView();
        kmeanView = createKMeanView();
    }

    private class CenteredLabel extends JLabel {
        public CenteredLabel(String text) {
            super(text);
            setAlignmentX(Component.CENTER_ALIGNMENT);
        }
    }

    private class CenteredButton extends JButton {
        public CenteredButton(String text, ActionListener onClick) {
            super(text);
            if (onClick != null)
                addActionListener(onClick);
            setAlignmentX(Component.CENTER_ALIGNMENT);
        }
    }

    private JPanel createKNNView() {
        JPanel view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        view.add(Box.createVerticalStrut(8));
        view.add(new CenteredLabel("k-Nearest-Neighbour Algorithm"));
        view.add(Box.createVerticalStrut(8));
        view.add(new SliderPanel("k", 1, 100, 20));
        view.add(Box.createVerticalStrut(8));
        view.add(new DistanceMeasureDropdown());
        view.add(Box.createVerticalStrut(20));
        view.add(new CenteredButton("Feed Learning Data", null));
        view.add(Box.createVerticalStrut(8));
        view.add(new CenteredButton("Evaluate Samples", null));
        view.add(Box.createVerticalStrut(8));
        return view;
    }

    private JPanel createKMeanView() {
        JPanel view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        view.add(Box.createVerticalStrut(8));
        view.add(new CenteredLabel("k-Mean Algorithm"));
        view.add(Box.createVerticalStrut(8));
        view.add(new SliderPanel("k", 1, 100, 20));
        view.add(Box.createVerticalStrut(8));
        view.add(new SliderPanel("Iterations", 1, 500, 20));
        view.add(Box.createVerticalStrut(8));
        view.add(new DistanceMeasureDropdown());
        view.add(Box.createVerticalStrut(20));
        view.add(new CenteredButton("Feed Learning Data", null));
        view.add(Box.createVerticalStrut(8));
        view.add(new CenteredButton("Evaluate Samples", null));
        view.add(Box.createVerticalStrut(8));
        return view;
    }
}
