/**
 * <=========================================================================================>
 * File: 	 AbstractConverter.java
 * Created:  08.12.2015
 * Author:   HAUSWALD, Tom.
 * <=========================================================================================>
 */

package de.tuhh.swp;

import java.io.File;

/**
 * TODO: Add type documentation here.
 */
public abstract class AbstractConverter<T> {

	public AbstractConverter() {
		// TODO Auto-generated constructor stub
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

	;;

	// ===========================================================
	// Override Methods
	// ===========================================================

	;;

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Convert a file in an external format to the targeted internal datatype.
	 */
	public abstract T toInternal(File file);

	/**
	 * Convert an internal datatype to an external format and store it in the
	 * specified file.
	 */
	public abstract void toExternal(T data, File file);

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	;;
}
