/**
 * <=========================================================================================>
 * File: 	 KMean.java
 * Created:  08.12.2015
 * Author:   HAUSWALD, Tom.
 * <=========================================================================================>
 */

package de.tuhh.swp;

import java.util.List;

/**
 * TODO: Add type documentation here.
 */
public class KMean extends AbstractAlgorithm {



	// ===========================================================
	// Constants
	// ===========================================================

	;;

	// ===========================================================
	// Fields
	// ===========================================================

	// k
	private int k;

	// Stored label values.
	private KdTree<KMeanImageValue> tree;

	// ===========================================================
	// Constructors
	// ===========================================================

	public KMean(int k, ImageDefinition imageDefinition, DistanceMeasure distanceMeasure) {
		this.k = k;
		if (distanceMeasure == DistanceMeasure.Euclidean) {
			this.tree = new KdTree.SqrEuclid<KMeanImageValue>(imageDefinition.height * imageDefinition.width, null);
		}
		else{
			this.tree = new KdTree.Manhattan<KMeanImageValue>(imageDefinition.height * imageDefinition.width, null);
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

	public void feed(int iterations, float error, LearningData learningData){
		if(learningData.size() < k) {
			//TODO
			System.exit(-1);
		}else{
			for (int i = 0; i < k; ++i){
				KMeanImageValue image = (KMeanImageValue) learningData.remove((int) (Math.random() * learningData.size()));
				tree.addPoint(image.getTreeLocation(), image);
			}
			int i;
			int iteration = 0;
			while(iteration++ < iterations){
				//Expectation
				for(i = 0; i < learningData.size(); ++i){
					KMeanImageValue sample = (KMeanImageValue) learningData.get(i);
					KMeanImageValue prototype = tree.nearestNeighbor(sample.getTreeLocation(), 1, false).get(0).value;
					sample.setPrototype(prototype);
				}
				//Maximization

				break;
			}
		}

	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	;;
}
