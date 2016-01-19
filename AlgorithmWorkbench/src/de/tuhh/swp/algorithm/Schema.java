package de.tuhh.swp.algorithm;

import de.tuhh.swp.image.ImageDefinition;

/**
 * Created by Tom on 19.01.2016.
 */
public class Schema {

    private IntTargetDefinition targetDefinition;
    private ImageDefinition sampleDefinition;

    public static final Schema DEFAULT_SCHEMA = new Schema(IntTargetDefinition.LABEL, new ImageDefinition(28, 28));

    public Schema(IntTargetDefinition targetDefinition, ImageDefinition sampleDefinition){
        this.targetDefinition = targetDefinition;
        this.sampleDefinition = sampleDefinition;
    }

    public IntTargetDefinition getTargetDefinition() {
        return targetDefinition;
    }

    public void setTargetDefinition(IntTargetDefinition targetDefinition) {
        this.targetDefinition = targetDefinition;
    }

    public ImageDefinition getSampleDefinition() {
        return sampleDefinition;
    }

    public void setSampleDefinition(ImageDefinition sampleDefinition) {
        this.sampleDefinition = sampleDefinition;
    }
}
