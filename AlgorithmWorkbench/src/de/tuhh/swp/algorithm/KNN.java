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

	// Image definition of samples.
	private ImageDefinition definition;

	// Stored label values.
	private KdTree<Byte> tree;

	// ===========================================================
	// Constructors
	// ===========================================================

	public KNN(int k, ImageDefinition definition, DistanceMeasure distanceMeasure) {
		super("k-Nearest-Neighbour");
		this.k = k;
		if(distanceMeasure == DistanceMeasure.Euclidean){
			this.tree = new KdTree.SqrEuclid<Byte>(definition.width * definition.height, null);
		} else {
			this.tree = new KdTree.Manhattan<Byte>(definition.width * definition.height, null);
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

	public void feed(LearningData data){
		for(ImageValue sample : data){
			tree.addPoint(sample.getPixels(), sample.getLabel());
		}
	}

	public byte evaluate(ImageValue image){
		List<KdTree.Entry<Byte>> neighbours = tree.nearestNeighbor(image.getPixels(), this.k, false);
		int[] labelCounts = new int[10];
		for(KdTree.Entry<Byte> label : neighbours){
			++labelCounts[label.value];
		}

		byte result = 0;
		for(byte label = 1; label <= 9; ++label){
			if(labelCounts[label] >= labelCounts[result]){
				result = label;
			}
		}

		return result;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	;;
}
