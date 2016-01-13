package de.tuhh.swp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Tom on 06.01.2016.
 */
public class Menu extends JMenuBar {

    private Workbench workbench;
    private HashMap<String, JMenu> submenus;
    private String labelsFilePath, imagesFilePath;

    private void addMenuItem(String menu, String item, int shortcutKeycode, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(item, shortcutKeycode);
        menuItem.addActionListener(listener);
        submenus.get(menu).add(menuItem);
    }

    public Menu(Workbench workbench) {

        this.workbench = workbench;
        submenus = new HashMap<>();

        // Learning data dropdown.
        submenus.put("data", new JMenu("Learning Data"));
        {
            // Select labels.
            addMenuItem("data", "Select Labels File", KeyEvent.VK_L, (ActionEvent e) -> {
                selectLabelsFile();
            });

            // Select hand-drawn images.
            addMenuItem("data", "Select Images File", KeyEvent.VK_I, (ActionEvent e) -> {
                selectImagesFile();
            });

            // Add selected files.
            addMenuItem("data", "Add to database", KeyEvent.VK_A, (ActionEvent e) -> {
                updateDatabase();
            });

            // Clear complete database.
            addMenuItem("data", "Clear database", KeyEvent.VK_C, (ActionEvent e) -> {
                clearDatabase();
            });
        }

        // Algorithm dropdown.
        submenus.put("algos", new JMenu("Algorithms"));
        {
            // k-nearest-neighbour.
            addMenuItem("algos", "k-Nearest-Neighbour", KeyEvent.VK_N, (ActionEvent e) -> {
                openAlgorithmView(() -> new KNNSettingsPanel(workbench));
            });

            // k-mean.
            addMenuItem("algos", "k-Mean", KeyEvent.VK_M, (ActionEvent e) -> {
                openAlgorithmView(() -> new KMeanSettingsPanel(workbench));
            });
        }

        // Construct menu.
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
}
