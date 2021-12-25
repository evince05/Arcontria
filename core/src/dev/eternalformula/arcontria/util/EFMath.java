package dev.eternalformula.arcontria.util;

import java.math.BigDecimal;

public class EFMath {
	
	/**
	 * Generates a random float within a range.
	 * @param min Min value.
	 * @param max Max value.
	 * @return A random float within the specified range.
	 */
	
	public static float randomFloat(float min, float max) {
		return min + (float) Math.random() * (max - min);
	}
	
	/**
	 * Rounds a float to the given number of decimal places.
	 * @param value the float to be rounded
	 * @param scale The number of decimal places.
	 */
	
	public static float round(float value, int scale) {
	    int pow = 10;
	    for (int i = 1; i < scale; i++) {
	        pow *= 10;
	    }
	    float tmp = value * pow;
	    float tmpSub = tmp - (int) tmp;

	    return ( (float) ( (int) (
	            value >= 0
	            ? (tmpSub >= 0.5f ? tmp + 1 : tmp)
	            : (tmpSub >= -0.5f ? tmp : tmp - 1)
	            ) ) ) / pow;

	    // Below will only handles +ve values
	    // return ( (float) ( (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) ) ) / pow;
	}

}
