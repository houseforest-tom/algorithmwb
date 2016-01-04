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

    // GUI window dimensions.
    public static final int WINDOW_WIDTH = 384;
    public static final int WINDOW_HEIGHT = 640;

    // Perst stuff.
    private Database db;
    private Storage store;

    // GUI items.
    private JButton loadLabelsButton;
    private JButton loadImagesButton;
    private JButton loadButton;
    private JButton knnButton;
    private JButton kmeanButton;

    // Selected resource paths.
    private String labelsFilePath;
    private String imagesFilePath;

    // Loaded images.
    private ImageValue[] images;

    // Image preview.
    private ImagePreview preview;
    private JLabel previewLabel;
    int previewImageId = 0;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Workbench wb = new Workbench();
                wb.setVisible(true);
                wb.setResizable(false);
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

        // Preview loaded images.
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            if (images != null) {
                ImageValue img = images[previewImageId++];
                preview.setImage(img);
                preview.repaint();
                previewLabel.setText("Labelled as: " + (int)img.getLabel());
            }
        }, 0, 500, TimeUnit.MILLISECONDS);

        // Create new or open existing database.
        String dbPath = "res/images.dbs";
        boolean dbExists = new File(dbPath).exists();

        if (dbExists) System.out.println("Using existing database " + dbPath);
        else System.out.println("Creating new database " + dbPath);
        store = StorageFactory.getInstance().createStorage();
        store.open(dbPath, 1024);
        db = new Database(store, false);
        if (dbExists) {
            int count = db.getRecords(ImageValue.class).size();
            images = db.getRecords(ImageValue.class).toList().toArray(new ImageValue[count]);
            System.out.println("Loaded " + count + " images.");
        }


        // Window was closed.
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                store.close();
                System.exit(0);
            }
        });
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        setTitle("Algorithm Workbench");
        setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load labels file.
        loadLabelsButton = new JButton("Select Labels");
        loadLabelsButton.addActionListener((ActionEvent event) -> {
            JFileChooser chooser = new JFileChooser(new File("./"));
            chooser.setVisible(true);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                labelsFilePath = chooser.getSelectedFile().getAbsolutePath();
            }
        });
        setComponentPosition(loadLabelsButton, WINDOW_WIDTH * 0.125f, WINDOW_HEIGHT * 0.03f);
        setComponentSize(loadLabelsButton, WINDOW_WIDTH * 0.75f, WINDOW_HEIGHT * 0.03f);
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
        setComponentPosition(loadImagesButton, WINDOW_WIDTH * 0.125f, WINDOW_HEIGHT * 0.07f);
        setComponentSize(loadImagesButton, WINDOW_WIDTH * 0.75f, WINDOW_HEIGHT * 0.03f);
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
        setComponentPosition(loadButton, WINDOW_WIDTH * 0.125f, WINDOW_HEIGHT * 0.11f);
        setComponentSize(loadButton, WINDOW_WIDTH * 0.75f, WINDOW_HEIGHT * 0.03f);
        add(loadButton);

        preview = new ImagePreview(null);
        setComponentPosition(preview, WINDOW_WIDTH * 0.125f, WINDOW_HEIGHT * 0.92f - WINDOW_WIDTH * 0.75f);
        setComponentSize(preview, WINDOW_WIDTH * 0.75f, WINDOW_WIDTH * 0.75f);
        add(preview);

        previewLabel = new JLabel("Labelled as: -");
        setComponentPosition(previewLabel, WINDOW_WIDTH * 0.38f, WINDOW_HEIGHT * 0.92f);
        setComponentSize(previewLabel, WINDOW_WIDTH * 0.75f, 16);
        add(previewLabel);

        knnButton = new JButton("Perform KNN");
        knnButton.addActionListener((ActionEvent event) -> {
            KNN knn = new KNN(5, images[0].getDefinition(), KNN.DistanceMeasure.Manhattan);
            LearningData learnset = new LearningData();
            for(int i=0; i<60000; ++i){
                learnset.add(images[i]);
            }
            System.out.println("Created set of learning data.");
            knn.feed(learnset);
            System.out.println("Fed KD-tree.");
            int attempts = 128;
            int correctGuesses = 0;
            int index;
            for(int i=0; i<attempts; ++i){
                index = (int)(Math.random() * images.length);
                if(knn.evaluate(images[index]) == images[index].getLabel()){
                    correctGuesses++;
                }
                System.out.println("Finished evaluation #" + i);
            }
            System.out.println("Guessed " + (double)correctGuesses / (double)(attempts) * 100.0 + "% correctly.");
        });
        setComponentPosition(knnButton, WINDOW_WIDTH * 0.125f, WINDOW_HEIGHT * 0.15f);
        setComponentSize(knnButton, WINDOW_WIDTH * 0.75f, WINDOW_HEIGHT * 0.03f);
        add(knnButton);

        kmeanButton = new JButton("Perform KMean");
        kmeanButton.addActionListener((ActionEvent event) -> {
            KMean kmean = new KMean(20, images[0].getDefinition(), KNN.DistanceMeasure.Manhattan);
            LearningData learnset = new LearningData();
            for(int i=0; i<60000; ++i){
                KMeanImageValue image = new KMeanImageValue(images[i].getDefinition());
                image.setPixels(images[i].getPixels());
                learnset.add(image);
            }
            System.out.println("Created set of learning data.");
            kmean.feed(0,0,learnset);
            System.out.println("Fed KMean Algorithm.");
        });
        setComponentPosition(kmeanButton, WINDOW_WIDTH * 0.125f, WINDOW_HEIGHT * 0.19f);
        setComponentSize(kmeanButton, WINDOW_WIDTH * 0.75f, WINDOW_HEIGHT * 0.03f);
        add(kmeanButton);
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

    public void setComponentPosition(JComponent component, float x, float y) {
        component.setLocation((int)x, (int)y);
    }
    public void setComponentSize(JComponent component, float width, float height) {
        Dimension dim = new Dimension((int) width, (int) height);
        component.setPreferredSize(dim);
        component.setSize(dim);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    ;;
}
