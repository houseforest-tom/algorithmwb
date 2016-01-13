/**
 * <=========================================================================================>
 * File: 	 LearningData.java
 * Created:  08.12.2015
 * Author:   HAUSWALD, Tom.
 * <=========================================================================================>
 */

package de.tuhh.swp.algorithm;

import de.tuhh.swp.image.ImageValue;

import java.util.ArrayList;

/**
 * TODO: Add type documentation here.
 */
public class LearningData extends ArrayList<ImageValue> {

    public LearningData() {
        // TODO Auto-generated constructor stub
    }

    public int getSampleCount(byte label) {
        int count = 0;
        for (ImageValue image : this) {
            if (image.getLabel() == label) {
                ++count;
            }
        }
        return count;
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

    ;;

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
