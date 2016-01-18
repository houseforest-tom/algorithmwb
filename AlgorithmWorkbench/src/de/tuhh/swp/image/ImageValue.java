/**
 * <=========================================================================================>
 * File: ImageValue.java
 * Created: 08.12.2015
 * Author: HAUSWALD, Tom.
 * <=========================================================================================>
 */

package de.tuhh.swp.image;

import de.tuhh.swp.algorithm.IntTargetValue;
import de.tuhh.swp.image.ImageDefinition;
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
    private double[] pixels;
    private IntTargetValue label;

    // ===========================================================
    // Constructors
    // ===========================================================

    public ImageValue(ImageDefinition definition, IntTargetValue label) {
        this.definition = definition;
        this.pixels = new double[definition.width * definition.height];
        this.label = label;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public double getPixel(int x, int y) {
        return pixels[x + y * definition.width];
    }

    public void setPixel(int x, int y, double pixel) {
        pixels[x + y * definition.width] = pixel;
    }

    public void setPixels(double[] pixels) {
        this.pixels = pixels;
    }

    public double[] getPixels() {
        return pixels;
    }

    public ImageDefinition getDefinition() {
        return definition;
    }

    public IntTargetValue getLabel(){
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
