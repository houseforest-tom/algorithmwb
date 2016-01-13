package de.tuhh.swp;

import java.util.HashMap;

/**
 * Created by Tom on 13.01.2016.
 */
public class AlgorithmFailure extends HashMap.SimpleEntry<ImageValue, Byte> {
    public AlgorithmFailure(ImageValue image, Byte guessedLabel) {
        super(image, guessedLabel);
    }
}
