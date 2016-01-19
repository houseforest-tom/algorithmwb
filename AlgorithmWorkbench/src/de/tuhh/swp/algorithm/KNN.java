/**
 * <=========================================================================================>
 * File: 	 KNN.java
 * Created:  08.12.2015
 * Author:   HAUSWALD, Tom.
 * <=========================================================================================>
 */

package de.tuhh.swp.algorithm;

import de.tuhh.swp.image.ImageDefinition;
import de.tuhh.swp.image.ImageValue;
import de.tuhh.swp.util.KdTree;

import javax.swing.*;
import java.util.List;

/**
 * TODO: Add type documentation here.
 */
public class KNN extends AbstractAlgorithm {

    // ===========================================================
    // Constants
    // ===========================================================

    ;;

    // ===========================================================
    // Fields
    // ===========================================================

    // k.
    private int k;

    // Stored label values.
    private KdTree<IntTargetValue> tree;

    // ===========================================================
    // Constructors
    // ===========================================================

    public KNN(int k, ImageDefinition definition, DistanceMeasure distanceMeasure) {
        super("k-Nearest-Neighbour");
        this.k = k;
        if (distanceMeasure == DistanceMeasure.Euclidean) {
            this.tree = new KdTree.SqrEuclid<>(definition.width * definition.height, null);
        } else {
            this.tree = new KdTree.Manhattan<>(definition.width * definition.height, null);
        }
    }

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

    public void feed(LearningData data) {
        for (Example sample : data) {
            tree.addPoint(sample.getImage().getPixels(), sample.getImage().getLabel());
        }
    }

    public IntTargetValue evaluate(ImageValue image) {

        List<KdTree.Entry<IntTargetValue>> neighbours = tree.nearestNeighbor(image.getPixels(), this.k, false);
        int[] labelCounts = new int[10];
        for (KdTree.Entry<IntTargetValue> label : neighbours) {
            ++labelCounts[label.value.getValue()];
        }

        IntTargetValue result = new IntTargetValue(IntTargetDefinition.LABEL, 0);
        for (int label = 1; label <= 9; ++label) {
            if (labelCounts[label] >= labelCounts[result.getValue()]) {
                result.setValue(label);
            }
        }

        return result;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    ;;
}
