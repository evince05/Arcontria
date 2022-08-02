package dev.eternalformula.arcontria.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import dev.eternalformula.arcontria.level.maps.EFTiledMap;
import dev.eternalformula.arcontria.util.loaders.EFTiledMapLoader;

public class Assets {
	
	/**
	 * hhehehehehehehe assman
	 * @author EternalFormula
	 */
	
	public static final AssetManager assMan = new AssetManager();

	/**
	 * Sets up the asset manager to support custom loaders.<br>
	 * This must be called in order to loda the game.
	 */
	
	public static void setupManager() {
		assMan.setLoader(EFTiledMap.class, 
				new EFTiledMapLoader(new InternalFileHandleResolver()));
	}
	
	/**
	 * Loads the assets and lets the assman do its thaaaaang.
	 */
	
	public static void load() {
		
		// UI Textures.
		assMan.load("ui/elements/uiatlas.atlas", TextureAtlas.class);
		assMan.load("ui/charcreator/charcreator.atlas", TextureAtlas.class);
		assMan.load("fonts/Habbo.fnt", BitmapFont.class);
		assMan.load("maps/data/menumap.tmx", EFTiledMap.class);
	}
	
	public static void dispose() {
		assMan.dispose();
	}
	
	public static <T> T get(String fileName, Class<T> type) {
		return assMan.get(fileName, type);
	}
}
