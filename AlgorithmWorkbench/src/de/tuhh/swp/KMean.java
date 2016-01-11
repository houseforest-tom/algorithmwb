/**
 * <=========================================================================================>
 * File: 	 KMean.java
 * Created:  08.12.2015
 * Author:   HAUSWALD, Tom.
 * <=========================================================================================>
 */

package de.tuhh.swp;

import java.util.Arrays;
import java.util.LinkedList;

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

    public class KMeanCluster extends ImageValue {
        private LinkedList<ImageValue> children;

        public KMeanCluster(ImageDefinition imageDefinition) {
            super(imageDefinition, (byte) 0xff);
            this.children = new LinkedList<>();
        }

        public void randomizeLocation(double min, double max) {
            double[] location = new double[getDefinition().width * getDefinition().height];
            for (int i = 0; i < location.length; ++i) {
                location[i] = min + (max - min) * Math.random();
            }
            setPixels(location);
        }

        public LinkedList<ImageValue> getChildren() {
            return children;
        }

        public double updateLocation() {
            double[] location = getPixels();
            double[] prevLocation = Arrays.copyOf(location, location.length);

            if (children.size() > 0) {
                // Reset location to zero.
                int i = 0;
                for (i = 0; i < location.length; ++i) {
                    location[i] = 0.0;
                }

                // Sum up children locations.
                double[] childLocation;
                for (ImageValue child : children) {
                    childLocation = child.getPixels();
                    for (i = 0; i < location.length; ++i) {
                        location[i] += childLocation[i];
                    }
                }

                // Average.
                for (i = 0; i < location.length; ++i) {
                    location[i] /= children.size();
                }
            }

            // Return distance that the cluster moved.
            return dist(prevLocation, location);
        }
    }

    // k <=> Number of clusters to look for.
    private int k;

    // Clusters.
    private KMeanCluster[] clusters;

    // Way to measure the distance between data points.
    private DistanceMeasure distanceMeasure;

    // ===========================================================
    // Constructors
    // ===========================================================

    // Initialize k clusters at random positions.
    public KMean(int k, ImageDefinition imageDefinition, DistanceMeasure distanceMeasure) {
        this.k = k;
        this.distanceMeasure = distanceMeasure;
        this.clusters = new KMeanCluster[k];
        for (int i = 0; i < k; ++i) {
            this.clusters[i] = new KMeanCluster(imageDefinition);
        }
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public KMeanCluster[] getClusters() {
        return clusters;
    }

    // ===========================================================
    // Override Methods
    // ===========================================================

    ;;

    // ===========================================================
    // Methods
    // ===========================================================

    // Compute the distance between points a and b.
    private double dist(double[] a, double[] b) {
        if (a.length != b.length) {
            //TODO:
            System.exit(-1);
        }

        double distance = 0.0;
        if (distanceMeasure == DistanceMeasure.Euclidean) {
            for (int i = 0; i < a.length; ++i) {
                distance += (a[i] - b[i]) * (a[i] - b[i]);
            }
            distance = Math.sqrt(distance);
        } else {
            for (int i = 0; i < a.length; i++) {
                double diff = (a[i] - b[i]);
                if (!Double.isNaN(diff)) {
                    distance += (diff < 0) ? -diff : diff;
                }
            }
        }
        return distance;
    }

    public void iterate(LearningData learningData) {
        int clusterId;               // Current cluster #.
        int nearestClusterId;        // Nearest cluster #.
        double minClusterDistance;   // Distance to nearest already checked cluster.
        double clusterDistance;      // Distance to currently checked cluster.

        // Remove all children from clusters.
        for (KMeanCluster cluster : clusters) {
            cluster.getChildren().clear();
        }

        for (ImageValue sample : learningData) {
            minClusterDistance = Double.MAX_VALUE;

            // Find nearest cluster.
            nearestClusterId = -1;
            for (clusterId = 0; clusterId < k; ++clusterId) {
                clusterDistance = dist(sample.getPixels(), clusters[clusterId].getPixels());
                if (clusterDistance < minClusterDistance) {
                    nearestClusterId = clusterId;
                    minClusterDistance = clusterDistance;
                }
            }

            // Add sample to nearest cluster.
            clusters[nearestClusterId].getChildren().add(sample);
        }

        for (clusterId = 0; clusterId < k; ++clusterId) {
            // Reposition clusters.
            clusters[clusterId].updateLocation();
        }
    }

    /**
     * Feeds the KMean algorithm with its specified learning samples.
     *
     * @param iterations   Requested number of iterations.
     * @param error        Minimum cluster movement per iterations. (Early-out)
     * @param learningData Learning samples to use.
     * @param rng          Whether to randomly place the initial clusters.
     *                     If set to false, random samples from the learnset will be used for initialization.
     */
    public void feed(int iterations, double error, LearningData learningData, boolean rng) {
        int iteration;               // Current iteration #.
        int clusterId;               // Current cluster #.
        int nearestClusterId;        // Nearest cluster #.
        double minClusterDistance;   // Distance to nearest already checked cluster.
        double clusterDistance;      // Distance to currently checked cluster.
        boolean earlyOut;            // Flag determining whether the algorithm will finish after the current iteration.

        for (int i = 0; i < this.clusters.length; ++i) {
            if (rng) {
                this.clusters[i].randomizeLocation(0, 255);
            } else {
                double[] px = learningData.get((int) (Math.random() * learningData.size())).getPixels();
                this.clusters[i].setPixels(Arrays.copyOf(px, px.length));
            }
        }

        for (iteration = 0; iteration < iterations; ++iteration) {
            //System.out.println("KMean Iteration " + (iteration + 1) + ".");

            // Remove all children from clusters.
            for (KMeanCluster cluster : clusters) {
                cluster.getChildren().clear();
            }

            for (ImageValue sample : learningData) {
                minClusterDistance = Double.MAX_VALUE;

                // Find nearest cluster.
                nearestClusterId = -1;
                for (clusterId = 0; clusterId < k; ++clusterId) {
                    clusterDistance = dist(sample.getPixels(), clusters[clusterId].getPixels());
                    if (clusterDistance < minClusterDistance) {
                        nearestClusterId = clusterId;
                        minClusterDistance = clusterDistance;
                    }
                }

                // Add sample to nearest cluster.
                clusters[nearestClusterId].getChildren().add(sample);
            }

            // Reposition clusters and determine early-out condition.
            earlyOut = true;
            for (clusterId = 0; clusterId < k; ++clusterId) {
                if (clusters[clusterId].updateLocation() > error) {
                    earlyOut = false;
                }
            }

            if (earlyOut) {
                //System.out.println("KMean early-out condition met.");
                break;
            }
        }

        //System.out.println("KMean algorithm finished.");
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    ;;
}
