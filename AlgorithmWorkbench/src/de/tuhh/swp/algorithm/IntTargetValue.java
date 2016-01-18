/**
 * <=========================================================================================>
 * File: 	 IntTargetValue.java
 * Created:  08.12.2015
 * Author:   HAUSWALD, Tom.
 * <=========================================================================================>
 */

package de.tuhh.swp.algorithm;

/**
 * TODO: Add type documentation here.
 */
public class IntTargetValue {

	// ===========================================================
	// Constants
	// ===========================================================

	;;

	// ===========================================================
	// Fields
	// ===========================================================

	private IntTargetDefinition definition;

	private int value;

	// ===========================================================
	// Constructors
	// ===========================================================

	public IntTargetValue(IntTargetDefinition definition, int value) {
		this.definition = definition;
		setValue(value);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public IntTargetDefinition getDefinition(){
		return definition;
	}

	public int getValue(){
		return value;
	}

	public void setDefinition(IntTargetDefinition definition){
		this.definition = definition;
		checkDefinition();
	}

	public void setValue(int value){
		this.value = value;
		checkDefinition();
	}

	// ===========================================================
	// Override Methods
	// ===========================================================

	;;

	// ===========================================================
	// Methods
	// ===========================================================

	private void checkDefinition() {
		if (!this.definition.isValidValue(this.value)) {
			System.err.println("Invalid value " + this.value + " does not satisfy target definition.");
			System.exit(-1);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	;;
}
