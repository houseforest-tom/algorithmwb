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

    // Loaded images.
    private ImageValue[] images;

    // Algorithm instances.
    private KNN knn;
    private KMean kmean;

    // Menu bar.
    private JMenuBar menubar;

    // Content panel.
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

        int k = 20;
        if (images != null && images.length > 0) {
            kmean = new KMean(k, images[0].getDefinition(), KNN.DistanceMeasure.Manhattan);
        }

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

    public ImageValue[] getImages() {
        return images;
    }

    public void setImages(ImageValue[] images) {
        this.images = images;
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

    public void setKNNAlgorithm(KNN knn) {
        this.knn = knn;
    }

    public KNN getKNNAlgorithm() {
        return knn;
    }

    public void setKMeanAlgorithm(KMean kmean) {
        this.kmean = kmean;
    }

    public KMean getKMeanAlgorithm() {
        return kmean;
    }

    // ===========================================================
    // Override Methods
    // ===========================================================


    // ===========================================================
    // Methods
    // ===========================================================

    public static void setComponentPosition(JComponent component, float x, float y) {
        component.setLocation((int) x, (int) y);
    }

    public static void setComponentSize(JComponent component, float width, float height) {
        Dimension dim = new Dimension((int) width, (int) height);
        component.setPreferredSize(dim);
        component.setSize(dim);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    ;;
}
