/**
 * <=========================================================================================>
 * File: IPrintable.java
 * Created: 16.12.2015
 * Author: HAUSWALD, Tom.
 * <=========================================================================================>
 */
package de.tuhh.swp.convert;

import de.tuhh.swp.algorithm.IntTargetValue;
import de.tuhh.swp.image.ImageDefinition;
import de.tuhh.swp.image.ImageValue;
import de.tuhh.swp.Workbench;

import java.util.ArrayList;

/**
 * TODO: Document this type.
 */
public class ImageConverter extends AbstractConverter<ImageValue[]> {
    // ===========================================================
    // Constants
    // ===========================================================

    private static final int MAGIC_NUMBER = 2051;

    // ===========================================================
    // Fields
    // ===========================================================

    private IntTargetValue[] labels;
    private Workbench workbench;

    // ===========================================================
    // Constructors
    // ===========================================================

    public ImageConverter(Workbench workbench, IntTargetValue[] labels) {
        this.labels = labels;
        this.workbench = workbench;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    ;;

    // ===========================================================
    // Override Methods
    // ===========================================================

    @Override
    public ImageValue[] toInternal(byte[] external) {

        int magicNumber = AbstractConverter.bytesToInt(external, 0);
        if (magicNumber != MAGIC_NUMBER) {
            System.err.println("Expected magic number " + MAGIC_NUMBER + ", but got " + magicNumber + ".");
            System.exit(-1);
        }

        int numImages = AbstractConverter.bytesToInt(external, 4);
        ImageValue[] images = new ImageValue[numImages];

        int width = AbstractConverter.bytesToInt(external, 12);
        int height = AbstractConverter.bytesToInt(external, 8);
        ImageDefinition definition = new ImageDefinition(width, height);
        ImageValue image;

        int x, y; // Coords relative to current image.
        int offset = 16; // Offset in byte array.

        for (int i = 0; i < numImages; ++i) {
            image = images[i] = new ImageValue(definition, labels[i]);
            for (y = 0; y < height; ++y) {
                for (x = 0; x < width; ++x) {
                    image.setPixel(x, y, (double) Byte.toUnsignedInt(external[offset++]));
                }
            }
            workbench.getDatabase().addRecord(image);
        }

        long count = 0;
        for (int label = 0; label <= 9; ++label) {
            count = getLabelledImages(label).size();
            Workbench.Debug.println("Retrieved " + count + " images labelled '" + label + "' from database.");
        }

        return images;
    }

    @Override
    public byte[] toExternal(ImageValue[] internal) {
        return null;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private ArrayList<ImageValue> getLabelledImages(int label) {
        return workbench.getDatabase().<ImageValue>select(ImageValue.class, "label.value = " + label).toList();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    ;;

}
