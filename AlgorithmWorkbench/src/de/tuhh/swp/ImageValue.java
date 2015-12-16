/**
 * <=========================================================================================>
 * File: ImageValue.java
 * Created: 08.12.2015
 * Author: HAUSWALD, Tom.
 * <=========================================================================================>
 */

package de.tuhh.swp;

import org.garret.perst.Persistent;

/**
 * TODO: Add type documentation here.
 */
public class ImageValue extends Persistent {

    // ===========================================================
    // Constants
    // ===========================================================

    ;;

    // ===========================================================
    // Fields
    // ===========================================================

    private ImageDefinition definition;
    private byte[] pixels;
    private byte label;

    // ===========================================================
    // Constructors
    // ===========================================================

    public ImageValue(ImageDefinition definition, byte label) {
        this.definition = definition;
        this.pixels = new byte[definition.width * definition.height];
        this.label = label;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public byte getPixel(int x, int y) {
        return pixels[x + y * definition.width];
    }

    public void setPixel(int x, int y, byte pixel) {
        pixels[x + y * definition.width] = pixel;
    }

    public void setPixels(byte[] pixels) {
        if (pixels.length != this.pixels.length) {
            System.err.println("Expected " + this.pixels.length + " pixel values, but got " + pixels.length);
            System.exit(-1);
        }

        for (int i = 0; i < pixels.length; ++i) {
            this.pixels[i] = pixels[i];
        }
    }

    public byte[] getPixels() {
        return pixels;
    }

    public ImageDefinition getDefinition() {
        return definition;
    }

    public byte getLabel(){
        return label;
    }

    // ===========================================================
    // Override Methods
    // ===========================================================

    ;;

    // ===========================================================
    // Methods
    // ===========================================================

    ;;

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    ;;
}
