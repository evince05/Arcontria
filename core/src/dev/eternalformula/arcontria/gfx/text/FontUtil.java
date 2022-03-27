package dev.eternalformula.arcontria.gfx.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import dev.eternalformula.arcontria.util.EFConstants;

public class FontUtil {
	
	/**
	 * Creates a font from the given .ttf file.
	 * @param ttfFile The font file
	 * @param size The font size
	 * @return A font created from the specified file.
	 */
	
	public static BitmapFont createFont(String ttfFile, int size) {
		
		if (!ttfFile.endsWith(".ttf")) {
			return null;
		}
		
		FreeTypeFontGenerator ttfGenerator = new FreeTypeFontGenerator(Gdx.files.internal(ttfFile));
		FreeTypeFontParameter params = new FreeTypeFontParameter();
		params.genMipMaps = true;
		params.color = Color.WHITE;
		params.size = size;
		params.borderColor = Color.BLACK;
		params.borderWidth = 0.8f;
		
		BitmapFont f = ttfGenerator.generateFont(params);
		f.getData().setScale(size / 256f);
		ttfGenerator.dispose();
		return f;
	}
	
	public static BitmapFont createBasicFont(String ttfFile, int size) {
		
		if (!ttfFile.endsWith(".ttf")) {
			return null;
		}
		
		FreeTypeFontGenerator ttfGenerator = new FreeTypeFontGenerator(Gdx.files.internal(ttfFile));
		FreeTypeFontParameter params = new FreeTypeFontParameter();
		params.genMipMaps = true;
		params.color = Color.WHITE;
		params.size = size;
		
		BitmapFont f = ttfGenerator.generateFont(params);
		f.getData().setScale(size / 256f);
		ttfGenerator.dispose();
		return f;
	}

}
