package dev.eternalformula.arcontria.util;

import com.badlogic.gdx.math.Vector2;

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
	
	/**
	 * Returns the angle (in degrees) between two points.
	 * @param pointA The first point, in the form (x, y).
	 * @param pointB The second point, in the form (x, y).
	 */
	
	public static float getAngle(Vector2 pointA, Vector2 pointB) {
		float angle = (float) Math.toDegrees(Math.atan2(pointB.y - pointA.y, pointB.x - pointA.x));
		
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}
	
	/**
	 * Parses a Vector2 from a string in the format [x, y].
	 * @param str The string to be parsed.
	 * @return A vector2 parsed from the string.
	 */
	
	public static Vector2 vec2FromString(String str) {
		Vector2 vec = new Vector2();
		String unbracket = str.substring(1, str.length() - 1);
		String[] coords = unbracket.split(",");
		
		vec.x = Float.valueOf(coords[0]);
		vec.y = Float.valueOf(coords[1]);
		
		return vec;
	}
	
	/**
	 * Rounds the specified number to the nearest fraction of x. Eg:<br>
	 * <code> roundToNearestXth(1.38f, 1/2f)</code> would return <code>1.5f</code>
	 * @param num The number to round
	 * @param x The number to round to, usually expressed as a fraction.
	 */
	
	public static float roundToNearestXth(float num, float x) {
		float reciprocal = 1f / x;
		return Math.round(num * reciprocal) / reciprocal;
	}
	
	/**
	 * Rounds the specified number to the nearest 16th. Used mainly to adjust the camera for pixels.
	 * @param num The number to round
	 */
	
	public static float roundToNearest16th(float num) {
		return roundToNearestXth(num, 1/16f);
	}
}
