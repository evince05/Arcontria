package dev.eternalformula.arcontria.objects.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Small utility class to record changes that have been made to the game
 * over time.
 * 
 * @author EternalFormula
 */

public class Changelog {
	
	public static final String GAME_VERSION = "Alpha 0.0.1";
	private FileHandle logFile;
	
	Changelog() {
		this.logFile = Gdx.files.internal("changelog.txt");
	}
	
	public static Changelog load() {
		return new Changelog();
	}
	/**
	 * Prints the changelog to the console.
	 */
	
	public void print() {
		System.out.println("Arcontria [" + GAME_VERSION + "] Changelog:");
		String changelog = logFile.readString();
		String[] lines = changelog.split("\\n\\r?");
		for (String line : lines) {
			System.out.println("\t" + line);
		}
	}

}
