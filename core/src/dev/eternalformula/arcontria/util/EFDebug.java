package dev.eternalformula.arcontria.util;

import com.badlogic.gdx.Gdx;

public class EFDebug {
	
	public static boolean debugBox2D = true;
	
	public static void warn(String msg) {
		Gdx.app.debug("WARNING", msg);
	}
	
	public static void debug(String msg) {
		Gdx.app.debug("DEBUG", msg);
	}
	
	public static void error(String msg) {
		Gdx.app.debug("ERROR", msg);
	}
	
	public static void info(String msg) {
		Gdx.app.debug("INFO", msg);
	}
}
