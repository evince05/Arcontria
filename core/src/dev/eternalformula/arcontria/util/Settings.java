package dev.eternalformula.arcontria.util;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.databind.JsonNode;

import dev.eternalformula.arcontria.files.JsonUtil;

public class Settings {
	
	public static final String DEFAULT_ARCONTRIA_FOLDER = System.getenv("APPDATA")
			+ File.separator + "Arcontria";
	
	public int windowWidth;
	public int windowHeight;
	
	public float masterVolume;
	public float musicVolume;
	
	public Settings() {
		JsonNode node = JsonUtil.getRootNodeFromLocalFile
				(DEFAULT_ARCONTRIA_FOLDER + File.separator + "settings.config").get("settings");
		
		// Launch Prefs
		JsonNode launchPrefsNode = node.get("launch");
		this.windowWidth = launchPrefsNode.get("windowWidth").intValue();
		this.windowHeight = launchPrefsNode.get("windowHeight").intValue();
		
		Gdx.graphics.setWindowedMode(windowWidth, windowHeight);
		
		// Audio Settings
		JsonNode audioNode = node.get("audio");
		this.masterVolume = audioNode.get("masterVolume").floatValue();
		this.musicVolume = audioNode.get("musicVolume").floatValue();
		
		EFDebug.debug("Successfully loaded settings.");
	}

}
