package dev.eternalformula.arcontria.gfx.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.Hinting;

import dev.eternalformula.arcontria.util.Assets;

public class FontUtil {
	
	private static final GlyphLayout LAYOUT = new GlyphLayout();
	
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
		params.mono = true;
		params.hinting = Hinting.None;
		
		BitmapFont f = ttfGenerator.generateFont(params);
		ttfGenerator.dispose();
		return f;
	}
	
	public static float getWidth(BitmapFont font, String text) {
		LAYOUT.setText(font, text);
		return LAYOUT.width;
	}
	
	public static float getHeight(BitmapFont font, String text) {
		LAYOUT.setText(font, text);
		return LAYOUT.height;
	}

}
