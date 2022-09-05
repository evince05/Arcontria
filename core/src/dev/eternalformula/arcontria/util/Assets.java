package dev.eternalformula.arcontria.util;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
	
	private AssetManager assMan;
	public static Assets instance;
	
	/**
	 * hhehehehehehehe assman
	 * @author EternalFormula
	 */
	
	public Assets() {
		assMan = new AssetManager();
		setupManager();
		instance = this;
	}

	/**
	 * Sets up the asset manager to support custom loaders.<br>
	 * This must be called in order to loda the game.
	 */
	
	public void setupManager() {
	}
	
	/**
	 * Loads the assets and lets the assman do its thaaaaang.
	 */
	
	public void loadDefaultAssets() {
		
		// UI Textures.
		instance.assMan.load("ui/elements/uiatlas.atlas", TextureAtlas.class);
		instance.assMan.load("ui/charcreator/charcreator.atlas", TextureAtlas.class);
		instance.assMan.load("fonts/Habbo.fnt", BitmapFont.class);
		instance.assMan.load("textures/entities/bosses/udp.atlas", TextureAtlas.class);
		instance.assMan.load("sfx/mines/rockbreak.wav", Sound.class);
	}
	
	public static <T> void load(String fileName, Class<T> type) {
		instance.assMan.load(fileName, type);
	}
	
	public static <T, P extends AssetLoaderParameters<T>> void setLoader
			(Class<T> type, AssetLoader<T, P> loader) {
		instance.assMan.setLoader(type, loader);
	}
	
	public static void updateInstance() {
		System.out.println("No assman? " + instance.assMan == null);
		instance.update();
	}
	
	public void update() {
		while (!instance.assMan.update()) { 
			EFDebug.info("[AssMan] Loading assets (" + instance.assMan.getProgress() * 100f + "%)!");
		}
		EFDebug.info("[AssMan] Asset Manager has finished loading!");
	}
	
	public void dispose() {
		assMan.dispose();
	}
	
	public static <T> T get(String fileName, Class<T> type) {
		return instance.assMan.get(fileName, type);
	}
}
