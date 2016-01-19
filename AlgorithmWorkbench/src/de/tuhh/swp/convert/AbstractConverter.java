/**
 * <=========================================================================================>
 * File: AbstractConverter.java
 * Created: 08.12.2015
 * Author: HAUSWALD, Tom.
 * <=========================================================================================>
 */

package de.tuhh.swp.convert;

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
    public abstract T toInternal(byte[] external);

    /**
     * Convert an internal datatype to an external format.
     */
    public abstract byte[] toExternal(T internal);

    public static int bytesToInt(byte[] bytes, int offset) {
        return bytes[offset + 3] & 0xFF |
                (bytes[offset + 2] & 0xFF) << 8 |
                (bytes[offset + 1] & 0xFF) << 16 |
                (bytes[offset] & 0xFF) << 24;
    }

    public static byte[] intToBytes(int n) {
        return new byte[]{
                (byte) ((n >> 24) & 0xFF),
                (byte) ((n >> 16) & 0xFF),
                (byte) ((n >> 8) & 0xFF),
                (byte) (n & 0xFF)
        };
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    ;;
}
