/**
 * <=========================================================================================>
 * File: IPrintable.java
 * Created: 16.12.2015
 * Author: HAUSWALD, Tom.
 * <=========================================================================================>
 */
package de.tuhh.swp;

import org.garret.perst.Database;
import org.garret.perst.Storage;
import org.garret.perst.StorageFactory;
import org.garret.perst.IterableIterator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

/**
 * TODO: Document this type.
 */
public class Workbench extends JFrame {

    // Perst stuff.
    private Database db;
    private Storage store;

    // GUI items.
    private JButton loadLabelsButton;
    private JButton loadImagesButton;
    private JButton loadButton;

    // Selected resource paths.
    private String labelsFilePath;
    private String imagesFilePath;

    // Loaded images.
    private ImageValue[] images;

    // Image preview.
    private ImagePreview preview;
    int previewImageId = 0;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Workbench wb = new Workbench();
                wb.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the application.
     */
    public Workbench() {
        initialize();

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            if (images != null) {
                preview.setImage(images[previewImageId++]);
                preview.repaint();
            }
        }, 0, 250, TimeUnit.MILLISECONDS);

        String dbPath = "images.dbs";
        store = StorageFactory.getInstance().createStorage();
        store.open(dbPath, 1024);
        db = new Database(store, false);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                store.close();
                int i = JOptionPane.showConfirmDialog(null, "Seguro que quiere salir?");
                if (i == 0)
                    System.exit(0);//cierra aplicacion
            }
        });
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int width = screenWidth / 2;
        int height = (int) (width / 16.0f * 9.0f);

        setBounds((screenWidth - width) / 2, (screenHeight - height) / 2, width, height);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("Load Training Data:"));

        // Load labels file.
        loadLabelsButton = new JButton("Select Labels");
        loadLabelsButton.addActionListener((ActionEvent event) -> {
            JFileChooser chooser = new JFileChooser(new File("./"));
            chooser.setVisible(true);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                labelsFilePath = chooser.getSelectedFile().getAbsolutePath();
            }
        });

        setComponentSize(loadLabelsButton, 140, 32);
        add(loadLabelsButton);

        // Load images file.
        loadImagesButton = new JButton("Select Images");
        loadImagesButton.addActionListener((ActionEvent event) -> {
            JFileChooser chooser = new JFileChooser(new File("./"));
            chooser.setVisible(true);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                imagesFilePath = chooser.getSelectedFile().getAbsolutePath();
            }
        });
        setComponentSize(loadImagesButton, 140, 32);
        add(loadImagesButton);

        // Load the training samples using the specified labels and images files.
        loadButton = new JButton("Load");
        loadButton.addActionListener((ActionEvent event) -> {
            try {
                byte[] labelFile = Files.readAllBytes(Paths.get(labelsFilePath));
                byte[] labels = new LabelConverter().toInternal(labelFile);
                byte[] imagesFile = Files.readAllBytes(Paths.get(imagesFilePath));
                images = new ImageConverter(this, labels).toInternal(imagesFile);
                System.out.println("Loaded " + images.length + " images.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        setComponentSize(loadButton, 80, 32);
        add(loadButton);

        preview = new ImagePreview(null);
        setComponentSize(preview, 256, 256);
        preview.setVisible(true);
        add(preview);
    }

    // ===========================================================
    // Constants
    // ===========================================================

    ;;

    // ===========================================================
    // Fields
    // ===========================================================

    ;;

    // ===========================================================
    // Constructors
    // ===========================================================

    ;;

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public Database getDatabase() {
        return db;
    }

    public Storage getStore() {
        return store;
    }

    // ===========================================================
    // Override Methods
    // ===========================================================


    // ===========================================================
    // Methods
    // ===========================================================

    public void setComponentSize(JComponent component, int width, int height) {
        Dimension dim = new Dimension(width, height);
        component.setPreferredSize(dim);
        component.setSize(dim);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    ;;

}
