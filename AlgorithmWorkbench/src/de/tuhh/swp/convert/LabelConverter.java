/**
 * <=========================================================================================>
 * File: IPrintable.java
 * Created: 16.12.2015
 * Author: HAUSWALD, Tom.
 * <=========================================================================================>
 */
package de.tuhh.swp.convert;

import de.tuhh.swp.convert.AbstractConverter;

/**
 * TODO: Document this type.
 */
public class LabelConverter extends AbstractConverter<byte[]>
{

	// ===========================================================
	// Constants
	// ===========================================================

	private static final int MAGIC_NUMBER = 2049;

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

	@Override
	public byte[] toInternal( byte[] external )
	{
		int magicNumber = AbstractConverter.bytesToInt( external, 0 );
		if( magicNumber != MAGIC_NUMBER )
		{
			System.err.println( "Labels file has incorrect magic number " + magicNumber + " should be " + MAGIC_NUMBER + "." );
		}

		int numItems = AbstractConverter.bytesToInt( external, 4 );
		System.out.println( "Loading labels for " + numItems + " training samples." );

		byte[] labels = new byte[numItems];
		for( int item = 0; item < numItems; ++item )
		{
			labels[item] = external[8 + item];
		}

		return labels;
	}

	@Override
	public byte[] toExternal( byte[] internal )
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
