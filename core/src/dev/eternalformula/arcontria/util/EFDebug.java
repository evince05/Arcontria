package dev.eternalformula.arcontria.util;

import com.badlogic.gdx.Gdx;

public class EFDebug {
	
	public static boolean debugBox2D = false;
	public static boolean debugEnabled = true;
	public static boolean mapDebugEnabled = false;
	
	private float deltaTime;
	
	private long lastTime;
	
	public EFDebug() {
		lastTime = System.currentTimeMillis();
	}
	
	public void update(float delta) {
		deltaTime += delta;
		
		if ((System.currentTimeMillis() - lastTime) / 1000 >= 2) {
			EFDebug.info("Running " + Gdx.graphics.getFramesPerSecond() + 
					" FPS. Heap Size: " + Gdx.app.getJavaHeap() / 1000000f + "MB");
			
			lastTime = System.currentTimeMillis();
		}
	}
	
	public static void warn(String msg) {
		Gdx.app.debug("WARNING", msg);
	}
	
	public static void debug(String msg) {
		if (debugEnabled) {
			Gdx.app.debug("DEBUG", msg);
		}
	}
	
	public static void error(String msg) {
		Gdx.app.debug("ERROR", msg);
	}
	
	public static void info(String msg) {
		Gdx.app.debug("INFO", msg);
	}
}
