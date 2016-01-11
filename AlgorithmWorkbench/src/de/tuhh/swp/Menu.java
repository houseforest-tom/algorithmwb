package de.tuhh.swp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Tom on 06.01.2016.
 */
public class Menu extends JMenuBar {

    private Workbench workbench;
    private HashMap<String, JMenu> submenus;
    private String labelsFilePath, imagesFilePath;

    private void addMenuItem(String title, int shortcutKeycode, ActionListener listener) {

    }

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

            // Add to database.
            JMenuItem updateItem = new JMenuItem("Add to Database", KeyEvent.VK_A);
            updateItem.addActionListener((ActionEvent e) -> {
                updateDatabase();
            });
            submenus.get("data").add(updateItem);

            // Clear database.
            JMenuItem clearItem = new JMenuItem("Clear Database", KeyEvent.VK_C);
            clearItem.addActionListener((ActionEvent e) -> {
                clearDatabase();
            });
            submenus.get("data").add(clearItem);
        }

        submenus.put("algos", new JMenu("Algorithms"));
        {
            // k-nearest-neighbour.
            JMenuItem knnItem = new JMenuItem("k-Nearest-Neighbour", KeyEvent.VK_N);
            knnItem.addActionListener((ActionEvent e) -> {
                openAlgorithmView(() -> createKNNView());
            });
            submenus.get("algos").add(knnItem);

            // k-mean.
            JMenuItem kmeanItem = new JMenuItem("k-Mean", KeyEvent.VK_M);
            kmeanItem.addActionListener((ActionEvent e) -> {
                openAlgorithmView(() -> createKMeanView());
            });
            submenus.get("algos").add(kmeanItem);
        }

        for (Map.Entry<String, JMenu> item : submenus.entrySet()) {
            add(item.getValue());
        }
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
        if (labelsFilePath == null || !new File(labelsFilePath).exists()) {
            JOptionPane.showMessageDialog(workbench, "Couldn't add to database. Please specify a valid label file.");
        } else if (imagesFilePath == null || !new File(imagesFilePath).exists()) {
            JOptionPane.showMessageDialog(workbench, "Couldn't add to database. Please specify a valid images file.");
        } else try {
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

    private void clearDatabase() {
        workbench.getDatabase().dropTable(ImageValue.class);
    }

    private void openAlgorithmView(Callable<JPanel> viewCreationFunc) {
        if (workbench.getImages() != null) {
            try {
                workbench.setContent(viewCreationFunc.call());
            } catch (Exception e) {
                System.err.println("View creation failed.");
            }
        } else {
            JOptionPane.showMessageDialog(workbench, "Database uninitialized. Please load images first!");
        }
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
        components.put("performButton", new CenteredButton("Perform Algorithm", null));

        // -------------------------------

        // Add button listener.
        ((JButton) components.get("performButton")).addActionListener((ActionEvent event) -> {

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

        JPanel view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        view.add(Box.createVerticalStrut(8));
        view.add(new CenteredLabel("k-Nearest-Neighbour Algorithm"));
        for (Map.Entry<String, JComponent> component : components.entrySet()) {
            view.add(Box.createVerticalStrut(8));
            view.add(component.getValue());
        }
        return view;
    }

    private JPanel createKMeanView() {

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
        components.put("feedButton", new CenteredButton("Search Clusters", null));
        components.put("performButton", new CenteredButton("Perform Algorithm", null));

        // -------------------------------

        ImageValue[] images = workbench.getImages();
        LearningData learnset = new LearningData();
        for (int i = 0; i < (int) ((SliderPanel) components.get("learningSamples")).getSliderValue(); ++i) {
            learnset.add(images[i]);
        }

        // Add button listeners.
        ((JButton) components.get("feedButton")).addActionListener((ActionEvent event) -> {
            KMean kmean;
            workbench.setKMeanAlgorithm(kmean = new KMean(
                    (int) ((SliderPanel) components.get("k")).getSliderValue(),
                    images[0].getDefinition(),
                    ((ArrayDropdown<AbstractAlgorithm.DistanceMeasure>) components.get("distanceMeasure")).getSelection()
            ));

            System.out.println("Feeding k-Mean algorithm " + learnset.size() + " learning samples...");
            kmean.feed(
                    (int) ((SliderPanel) components.get("iterations")).getSliderValue(),
                    ((SliderPanel) components.get("minDelta")).getSliderValue(),
                    learnset,
                    ((ArrayDropdown<String>) components.get("initialClusters")).getSelection().equals("Random Generation")
            );
            System.out.println("Finished searching " + kmean.getClusters().length + " clusters, please assign labels.");

            JFrame popup = new JFrame("Assign k-Mean Clusters");
            popup.add(createClusterAssignmentView());
            popup.setLocationRelativeTo(this);
            popup.pack();
            popup.setVisible(true);
        });

        ((JButton) components.get("performButton")).addActionListener((ActionEvent event) -> {
            int attempts = (int) ((SliderPanel) components.get("evaluationSamples")).getSliderValue();
            int correctGuesses = 0;
            int offset = learnset.size();
            int end = Math.min(offset + attempts, images.length);
            attempts = end - offset; // Adjust attempt count.
            System.out.println("Evaluating " + attempts + " samples with k-Mean algorithm...");
            for (int i = offset; i < end; ++i) {
                if (workbench.getKMeanAlgorithm().evaluate(images[i]) == images[i].getLabel()) {
                    correctGuesses++;
                }
            }
            System.out.println("k-Mean guessed " + (double) correctGuesses / (double) (attempts) * 100.0 + "% correctly.");
        });

        JPanel view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        view.add(Box.createVerticalStrut(8));
        view.add(new CenteredLabel("k-Mean Algorithm"));
        for (Map.Entry<String, JComponent> component : components.entrySet()) {
            view.add(Box.createVerticalStrut(8));
            view.add(component.getValue());
        }
        return view;
    }

    private JPanel createClusterAssignmentView() {
        KMean kmean = workbench.getKMeanAlgorithm();
        JPanel view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));

        // Create image previews.
        ImagePreview[] previews = new ImagePreview[kmean.getClusters().length];
        for (int i = 0; i < previews.length; ++i) {
            previews[i] = new ImagePreview(kmean.getClusters()[i], 140);
            previews[i].enableHovering();
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
        return view;
    }
}
