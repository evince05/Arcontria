package dev.eternalformula.arcontria.util;

import com.badlogic.gdx.graphics.Color;

public class EFUtil {

	public static Color getColorFromRGB(float r, float g, float b) {
		return new Color(r / 255f, g / 255f, b / 255f, 1.0f);	
	}
	
	public static Color getColorFromRGBA(float r, float g, float b, float a) {
		return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
	}
}
