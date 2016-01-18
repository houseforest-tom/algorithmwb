package de.tuhh.swp.algorithm;

import de.tuhh.swp.image.ImageValue;

import java.util.HashMap;

/**
 * Created by Tom on 13.01.2016.
 */
public class AlgorithmFailure extends HashMap.SimpleEntry<ImageValue, IntTargetValue> {
    public AlgorithmFailure(ImageValue image, IntTargetValue guessedLabel) {
        super(image, guessedLabel);
    }
}
