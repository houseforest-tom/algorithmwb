package de.tuhh.swp;

/**
 * Created by Tom on 04.01.2016.
 */
public class KMeanImageValue extends ImageValue {
    private ImageValue prototype = null;
    public KMeanImageValue(ImageDefinition imageDefinition){
        super(imageDefinition, (byte) 0);
    }
    public void setPrototype(ImageValue prototype){
        this.prototype = prototype;
    }
    public ImageValue getPrototype(){
        return prototype;
    }
}
