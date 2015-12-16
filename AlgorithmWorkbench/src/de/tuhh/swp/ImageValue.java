/**
 * <=========================================================================================>
 * File: ImageValue.java
 * Created: 08.12.2015
 * Author: HAUSWALD, Tom.
 * <=========================================================================================>
 */

package de.tuhh.swp;

/**
 * TODO: Add type documentation here.
 */
public class ImageValue
{

	// ===========================================================
	// Constants
	// ===========================================================

	;;

	// ===========================================================
	// Fields
	// ===========================================================

	private ImageDefinition definition;
	private char[] pixels;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ImageValue( ImageDefinition definition )
	{
		this.definition = definition;
		this.pixels = new char[definition.width * definition.height];
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public char getPixel( int x, int y )
	{
		return pixels[x + y * definition.width];
	}

	public void setPixel( int x, int y, char pixel )
	{
		pixels[x + y * definition.width] = pixel;
	}

	public void setPixels( char[] pixels )
	{
		if( pixels.length != this.pixels.length )
		{
			System.err.println( "Expected " + this.pixels.length + " pixel values, but got " + pixels.length );
			System.exit( -1 );
		}

		for( int i = 0; i < pixels.length; ++i )
		{
			this.pixels[i] = pixels[i];
		}
	}

	// ===========================================================
	// Override Methods
	// ===========================================================

	;;

	// ===========================================================
	// Methods
	// ===========================================================

	;;

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	;;
}
