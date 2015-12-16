/**
 * <=========================================================================================>
 * File: IPrintable.java
 * Created: 16.12.2015
 * Author: HAUSWALD, Tom.
 * <=========================================================================================>
 */
package de.tuhh.swp;

/**
 * TODO: Document this type.
 */
public class ImageConverter extends AbstractConverter<ImageValue>
{
	// ===========================================================
	// Constants
	// ===========================================================

	;;

	// ===========================================================
	// Fields
	// ===========================================================

	private ImageDefinition definition;

	// ===========================================================
	// Constructors
	// ===========================================================

	;;

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setImageDefinition( ImageDefinition definition )
	{
		this.definition = definition;
	}

	// ===========================================================
	// Override Methods
	// ===========================================================

	@Override
	public ImageValue toInternal( byte[] external )
	{
		ImageValue img = new ImageValue( definition );

		return img;
	}

	@Override
	public byte[] toExternal( ImageValue internal )
	{
		// TODO Auto-generated method stub
		return null;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	;;

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	;;

}
