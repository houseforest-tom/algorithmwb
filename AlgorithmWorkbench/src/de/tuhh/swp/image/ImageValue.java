/**
 * <=========================================================================================>
 * File: ImageValue.java
 * Created: 08.12.2015
 * Author: HAUSWALD, Tom.
 * <=========================================================================================>
 */

package de.tuhh.swp.image;

import de.tuhh.swp.algorithm.IntTargetValue;
import org.garret.perst.Persistent;
import org.garret.perst.impl.ByteBuffer;

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
    private IntTargetValue label;

    // ===========================================================
    // Constructors
    // ===========================================================

    public ImageValue(ImageDefinition definition, IntTargetValue label) {
        this.definition = definition;
        this.pixels = new byte[definition.width * definition.height];
        this.label = label;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public double getPixel(int x, int y) {
        return (double)(Byte.toUnsignedInt(pixels[x + y * definition.width]));
    }

    public void setPixel(int x, int y, double pixel) {
        this.pixels[x + y * definition.width] = (byte)pixel;
    }

    public void setPixels(double[] pixels) {
        for (int i = 0; i < pixels.length; ++i) {
            this.pixels[i] = (byte)pixels[i];
        }
    }

    public double[] getPixels() {
        double[] arr = new double[pixels.length];
        for (int i = 0; i < pixels.length; ++i) {
            arr[i] = (double)(Byte.toUnsignedInt(pixels[i]));
        }
        return arr;
    }

    public ImageDefinition getDefinition() {
        return definition;
    }

    public IntTargetValue getLabel() {
        return label;
    }

    public void setLabel(IntTargetValue label) {
        this.label = label;
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
