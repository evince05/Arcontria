package dev.eternalformula.arcontria.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Strings {
	
	/**
	 * Returns a string representation of a Vector2 object.
	 * @param vec The vector
	 */
	
	public static String vec2(Vector2 vec) {
		return "[" + vec.x + ", " + vec.y + "]";
	}
	
	/**
	 * Returns a string representation of a x and y coordinate
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	
	public static String vec2(float x, float y) {
		return "[" + x + ", " + y + "]";
	}
	
	/**
	 * Returns a string representation of a Vector3 object.
	 * @param vec The vector
	 */
	
	public static String vec3(Vector3 vec) {
		return "[" + vec.x + ", " + vec.y + ", " + vec.z + "]";
	}
	
	/**
	 * Returns a string representation of a x, y and z coordinate.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param z The z coordinate
	 */
	
	public static String vec3(float x, float y, float z) {
		return "[" + x + ", " + y + ", " + z + "]";
	}
	
	/**
	 * Formats a color as a vec3 string in the format [r, g, b].
	 * @param color The color to be formatted
	 */
	
	public static String formatColor(Color color) {
		return "[" + color.r + ", " + color.g + ", " + color.b + ", " + color.a + "]";
	}
	
	/**
	 * Puts quotes around the given string.
	 * @param str The string to be formatted
	 */
	
	public static String inQuotations(String str) {
		return "\"" + str + "\"";
	}
	
	/**
	 * Determines whether the string "str" contains the text "containedText".<br>
	 * Not case-sensitive.
	 * 
	 * @param str The string
	 * @param containedText The text to be checked in the string "str"
	 * @return True if the string "str" contains a non-case-sensitive match of <br>
	 * 		the string "containedText", otherwise false.
	 */
	public static boolean containsIgnoreCase(String str, String containedText) {
		return str.toLowerCase().contains(containedText.toLowerCase());
		
	}
}
