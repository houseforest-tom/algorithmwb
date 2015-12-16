/**
 * <=========================================================================================>
 * File: IPrintable.java
 * Created: 16.12.2015
 * Author: HAUSWALD, Tom.
 * <=========================================================================================>
 */
package de.tuhh.swp;

import org.garret.perst.IterableIterator;
import org.garret.perst.Iterator;

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

    private byte[] labels;
    private Workbench workbench;

    // ===========================================================
    // Constructors
    // ===========================================================

    public ImageConverter(Workbench workbench, byte[] labels) {
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
        ImageValue image;

        int x, y; // Coords relative to current image.
        int offset = 16; // Offset in byte array.

        for (int i = 0; i < numImages; ++i) {
            ImageDefinition definition = new ImageDefinition(width, height);
            image = images[i] = new ImageValue(definition, labels[i]);
            for (y = 0; y < height; ++y) {
                for (x = 0; x < width; ++x) {
                    image.setPixel(x, y, external[offset++]);
                }
            }
            workbench.getDatabase().addRecord(image);
        }

        Iterator iter = workbench.getDatabase().<ImageValue>select(ImageValue.class, "label = 0");
        while (iter.hasNext()) {
            System.out.println("Retrieved image from DB w/ label: " + ((ImageValue) (iter.next())).getLabel());
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

    ;;

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    ;;

}
