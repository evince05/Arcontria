package dev.eternalformula.arcontria.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
	
	/**
	 * hhehehehehehehe assman
	 * @author EternalFormula
	 */
	
	public static final AssetManager assMan = new AssetManager();

	/**
	 * Loads the assets and lets the assman do its thaaaaang.
	 */
	
	public static void load() {
		
		// UI Textures.
		assMan.load("ui/elements/uiatlas.atlas", TextureAtlas.class);
		assMan.load("ui/charcreator/charcreator.atlas", TextureAtlas.class);
		assMan.load("fonts/Habbo.fnt", BitmapFont.class);
	}
	
	public static void dispose() {
		assMan.dispose();
	}
	
	public static <T> T get(String fileName, Class<T> type) {
		return assMan.get(fileName, type);
	}
}
