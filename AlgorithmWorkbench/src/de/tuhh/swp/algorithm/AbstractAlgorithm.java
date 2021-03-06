/**
 * <=========================================================================================>
 * File: 	 AbstractAlgorithm.java
 * Created:  08.12.2015
 * Author:   HAUSWALD, Tom.
 * <=========================================================================================>
 */

package de.tuhh.swp.algorithm;

import de.tuhh.swp.image.ImageValue;

/**
 * TODO: Add type documentation here.
 */
public abstract class AbstractAlgorithm {

    private String name;

    public AbstractAlgorithm(String name) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    // ===========================================================
    // Override Methods
    // ===========================================================

    ;;

    // ===========================================================
    // Methods
    // ===========================================================

    public abstract void feed(LearningData learningData);

    public abstract IntTargetValue evaluate(ImageValue image);

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public enum DistanceMeasure {
        Manhattan,
        Euclidean
    }
}
