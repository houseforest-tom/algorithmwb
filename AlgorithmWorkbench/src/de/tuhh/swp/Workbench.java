/**
 * <=========================================================================================>
 * File: IPrintable.java
 * Created: 16.12.2015
 * Author: HAUSWALD, Tom.
 * <=========================================================================================>
 */
package de.tuhh.swp;

import de.tuhh.swp.algorithm.*;
import de.tuhh.swp.gui.frame.AlgorithmResultsFrame;
import de.tuhh.swp.image.ImageValue;
import net.miginfocom.swing.MigLayout;
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
        if (dbExists) Workbench.Debug.println("Using existing database " + dbPath);
        else Workbench.Debug.println("Creating new database " + dbPath);
        store = StorageFactory.getInstance().createStorage();
        store.open(dbPath, 1024);
        db = new Database(store, false);
        if (dbExists) {
            int count = db.getRecords(ImageValue.class).size();
            setImages(db.getRecords(ImageValue.class).toList().toArray(new ImageValue[count]));
            JOptionPane.showMessageDialog(this, "Loaded " + count + " images.");
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
        content.setLayout(new MigLayout());

        ImageIcon tuhhLogo = new ImageIcon("res/tuhh.png");
        tuhhLogo.setImage(tuhhLogo.getImage().getScaledInstance(440, 120, Image.SCALE_SMOOTH));
        content.add(Box.createVerticalStrut(0), "wrap 6");
        content.add(new JLabel(tuhhLogo), "wrap 30");
        content.add(new JLabel("Gruppe B1.1"), "center, wrap 6");
        content.add(new JLabel("HELLWEGE, Janina     GIERTZSCH, Fabian     HAUSWALD, Tom"), "center, wrap 6");

        // Setup menu bar.
        menubar = new de.tuhh.swp.gui.Menu(this);
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

    public void performAlgorithmTestRun(
            AbstractAlgorithm algorithm,
            LearningData learningData,
            int evaluationSampleOffset,
            int evaluationSampleCount) {

        int attempts = evaluationSampleCount;
        int correctAttemptCount = 0;
        int offset = evaluationSampleOffset;
        int end = Math.min(offset + attempts, images.length);
        attempts = end - offset; // Adjust attempt count.

        // Construct empty algorithm result.
        AlgorithmResult result = new AlgorithmResult(
                attempts,
                learningData
        );

        Workbench.Debug.println("Evaluating " + attempts + " samples with " + algorithm.getName() + " algorithm...");
        IntTargetValue guessedLabel;
        for (int i = offset; i < end; ++i) {
            guessedLabel = new IntTargetValue(IntTargetDefinition.LABEL, 0);
            guessedLabel.setValue(algorithm.evaluate(images[i]).getValue());
            if (guessedLabel.getValue() == images[i].getLabel().getValue()) {
                correctAttemptCount++;
            } else {
                result.addFailure(new AlgorithmFailure(images[i], guessedLabel));
            }
        }

        // Update correct attempts.
        result.setCorrectAttemptCount(correctAttemptCount);


        // Open results page.
        new AlgorithmResultsFrame(algorithm, result, this).setVisible(true);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public static final class Debug {

        // Enable debug prints.
        public static final boolean ENABLED = false;

        public static final void println(String ln) {
            if (Debug.ENABLED) System.out.println(ln);
        }
    }
}
