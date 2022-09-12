package dev.eternalformula.arcontria.util;

public class EntityUtil {
	
	/**
	 * The default amount of time in which an entity is invincible after it has been
	 * attacked.
	 */
	
	public static final float DEFAULT_DAMAGE_COOLDOWN = 0.6f;
	
	public static String getDirection(int direction) {
		switch (direction) {
		case 1:
			return "up";
		case 2:
			return "left";
		case 3:
			return "right";
		case 4:
			return "down";
		default:
			return "none";
		}
	}
	

}
