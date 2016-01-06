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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

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
    private LearningData learnset;

    private KMean kmean;
    private ImagePreview[] kmeanClusterViews;
    private JMenuBar menubar;
    private JPanel content;

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

        // Create empty learnset.
        learnset = new LearningData();

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
            setImages(db.getRecords(ImageValue.class).toList().toArray(new ImageValue[count]));
            System.out.println("Loaded " + count + " images.");
        }

        initialize();

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
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        content = new JPanel();
        content.setLayout(new BorderLayout());

        knnButton = new JButton("Perform KNN");
        knnButton.addActionListener((ActionEvent event) -> {
            KNN knn = new KNN(5, images[0].getDefinition(), KNN.DistanceMeasure.Manhattan);
            System.out.println("Created set of learning data.");
            System.out.println("Feeding KNN algorithm.");
            knn.feed(learnset);

            System.out.println("Evaluating test samples.");
            int attempts = 128;
            int correctGuesses = 0;
            int index;
            for (int i = 0; i < attempts; ++i) {
                index = (int) (Math.random() * images.length);
                if (knn.evaluate(images[index]) == images[index].getLabel()) {
                    correctGuesses++;
                }
                System.out.println("Finished evaluation (" + i + ").");
            }
            System.out.println("Guessed " + (double) correctGuesses / (double) (attempts) * 100.0 + "% correctly.");
        });
        setComponentPosition(knnButton, WINDOW_WIDTH * 0.125f, WINDOW_HEIGHT * 0.04f);
        setComponentSize(knnButton, WINDOW_WIDTH * 0.75f, WINDOW_HEIGHT * 0.03f);
        content.add(knnButton, BorderLayout.NORTH);

        int k = 20;
        if (images != null && images.length > 0) {
            kmean = new KMean(k, images[0].getDefinition(), KNN.DistanceMeasure.Manhattan);
        }

        kmeanButton = new JButton("Perform KMean");
        kmeanButton.addActionListener((ActionEvent event) -> {
            if (kmean == null) {
                kmean = new KMean(k, images[0].getDefinition(), KNN.DistanceMeasure.Manhattan);
            }
            kmean.iterate(learnset);
            if (kmeanClusterViews == null) {
                kmeanClusterViews = new ImagePreview[k];
                for (int i = 0; i < k; ++i) {
                    kmeanClusterViews[i] = new ImagePreview(kmean.getClusters()[i]);
                    setComponentPosition(
                            kmeanClusterViews[i],
                            WINDOW_WIDTH * (0.125f + (i % 5) * 0.75f / 5),
                            WINDOW_HEIGHT * 0.27f + (i / 5) * WINDOW_WIDTH * 0.75f / 5
                    );
                    setComponentSize(kmeanClusterViews[i], WINDOW_WIDTH * 0.75f / 5, WINDOW_WIDTH * 0.75f / 5);
                    add(kmeanClusterViews[i]);
                }
            }
            for (int i = 0; i < k; ++i) {
                kmeanClusterViews[i].repaint();
            }
        });
        setComponentPosition(kmeanButton, WINDOW_WIDTH * 0.125f, WINDOW_HEIGHT * 0.19f);
        setComponentSize(kmeanButton, WINDOW_WIDTH * 0.75f, WINDOW_HEIGHT * 0.03f);
        content.add(kmeanButton, BorderLayout.SOUTH);

        // Setup menu bar.
        menubar = new Menu(this);
        setJMenuBar(menubar);

        content.setVisible(true);
        add(content);
        pack();
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

    public void setImages(ImageValue[] images) {
        this.images = images;
        for (ImageValue image : this.images) {
            learnset.add(image);
        }
    }

    public JPanel getContent() {
        return content;
    }

    public void setContent(JPanel content) {
        this.remove(this.content);
        this.content = content;
        this.content.setVisible(true);
        this.add(content);
        this.pack();
        this.repaint();
    }

    // ===========================================================
    // Override Methods
    // ===========================================================


    // ===========================================================
    // Methods
    // ===========================================================

    public void setComponentPosition(JComponent component, float x, float y) {
        component.setLocation((int) x, (int) y);
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
