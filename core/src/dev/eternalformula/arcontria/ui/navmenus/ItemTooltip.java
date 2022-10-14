package dev.eternalformula.arcontria.ui.navmenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.gfx.text.FontUtil;
import dev.eternalformula.arcontria.items.Item;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.EFUtil;
import dev.eternalformula.arcontria.util.Strings;

public class ItemTooltip extends UIElement {
	
	private ItemDisplayBox itemDisplayBox;
	private BitmapFont font;
	
	public static ItemTooltip createTooltip(ItemDisplayBox itemDisplayBox) {
		return new ItemTooltip(itemDisplayBox);
	}
	
	private ItemTooltip(ItemDisplayBox itemDisplayBox) {
		this.itemDisplayBox = itemDisplayBox;
		
		this.font = Assets.get("fonts/pixelfj-8.fnt", BitmapFont.class);
		TextureRegion skin = buildRegion(itemDisplayBox.getItem());
		setSkin(skin);
	}
	
	private ItemTooltip(UIContainer parent, int x, int y) {
		super(parent);
		this.location = new Vector2(x, y);
	}
	
	/**
	 * Builds the ItemTooltip's enclosing TextureRegion.
	 * TODO: Wrap lore lines. Add List<String> wrapText(font, string text, int lineWidth) method to Strings.java
	 */
	private TextureRegion buildRegion(Item item) {
		
		TextureRegion tooltipTex = Assets.get("ui/elements/uiatlas.atlas",
				TextureAtlas.class).findRegion("itemtooltip");
		
		long startTime = System.currentTimeMillis();
		
		// Gets the text's width in px.
		float textWidth = FontUtil.getWidth(font, item.getMaterial().getDisplayName());
		float textHeight = FontUtil.getHeight(font, item.getMaterial().getDisplayName());
		
		float loreWidth = 0f;
		
		// Ensures that the lore width is that of the largest lore line
		for (String lore : item.getMaterial().getLore()) {
			float w = FontUtil.getWidth(font, lore);
			if (w > loreWidth) {
				loreWidth = w;
			}
		}
		
		float loreHeight = 0f;
		float loreSpacing = 4f * (item.getMaterial().getLore().size() - 1);
		
		// Gets the sum of each lore height
		for (String lore : item.getMaterial().getLore()) {
			float h = FontUtil.getHeight(font, lore);
			loreHeight += h;
		}
				
		// Gets the bigger of the two values
		float minWidth = textWidth >= loreWidth ? textWidth : loreWidth;
		
		// 8 comes from the width of each corner
		// 2 * xOffset
		
		float texWidth = minWidth + 8 + 2 * 3;
		
		EFDebug.info("item=" + item.getMaterial().getDisplayName() + Strings.vec2(textHeight, loreHeight));
		float texHeight = textHeight + loreHeight + loreSpacing + 18f;
				
		// Initializes the pixmap
		Pixmap pixmap = new Pixmap((int) texWidth, (int) texHeight, Format.RGBA8888);
		
		// Gets the corner pixmaps.
		
		int regX = tooltipTex.getRegionX();
		int regY = tooltipTex.getRegionY();
		
		EFDebug.info("Reg Loc: " + Strings.vec2(regX, regY));
		
		TextureRegion blc = new TextureRegion(tooltipTex, 0, 8, 4, 4);
		Pixmap blcPix = extractPixmapFromTextureRegion(blc);
		
		TextureRegion brc = new TextureRegion(tooltipTex, 8, 8, 4, 4);
		Pixmap brcPix = extractPixmapFromTextureRegion(brc);
		
		TextureRegion ulc = new TextureRegion(tooltipTex, 0, 0, 4, 4);
		Pixmap ulcPix = extractPixmapFromTextureRegion(ulc);
		
		// coords are relative to the specified texreg in the constructor.
		// coords are also from top left, rather than bottom left
		TextureRegion urc = new TextureRegion(tooltipTex, 8, 0, 4, 4);
		Pixmap urcPix = extractPixmapFromTextureRegion(urc);
		
		// Gets the side pixmaps.
		TextureRegion ls = new TextureRegion(tooltipTex, 0, 4, 4, 4);
		Pixmap lsPix = extractPixmapFromTextureRegion(ls);
		//PixmapIO.writePNG(Gdx.files.external("documents/testmap.png"), lsPix);
		
		TextureRegion rs = new TextureRegion(tooltipTex, 8, 4, 4, 4);
		Pixmap rsPix = extractPixmapFromTextureRegion(rs);
		
		TextureRegion us = new TextureRegion(tooltipTex, 4, 0, 4, 4);
		Pixmap usPix = extractPixmapFromTextureRegion(us);
		
		TextureRegion ds = new TextureRegion(tooltipTex, 4, 8, 4, 4);
		Pixmap dsPix = extractPixmapFromTextureRegion(ds);
		
		TextureRegion center = new TextureRegion(tooltipTex, 4, 4, 4, 4);
		Pixmap centerPix = extractPixmapFromTextureRegion(center);
		
		// Draws each of the corners (holy crap this is a lot of code for some versatility).
		pixmap.setFilter(Filter.NearestNeighbour);
		pixmap.drawPixmap(ulcPix, 0, 0);
		pixmap.drawPixmap(urcPix, (int) texWidth - urcPix.getWidth(),
				0);
		pixmap.drawPixmap(blcPix, 0, (int) texHeight - blcPix.getHeight());
		pixmap.drawPixmap(brcPix, (int) texWidth - brcPix.getWidth(),
				(int) texHeight - brcPix.getHeight());
		
		// Guess what? Even more code! Also, happy thanksgiving :) - EF, 2022-10-10
		pixmap.drawPixmap(lsPix, 0, 0, 4, 4, 0, 4, 4, (int) texHeight - 8);
		pixmap.drawPixmap(rsPix, 0, 0, 4, 4, (int) texWidth - 4, 4, 4, (int) texHeight - 8);
		pixmap.drawPixmap(usPix, 0, 0, 4, 4, 4, 0, (int) texWidth - 8, 4);
		pixmap.drawPixmap(dsPix, 0, 0, 4, 4, 4, (int) texHeight - 4, (int) texWidth - 8, 4);
		pixmap.drawPixmap(centerPix, 0, 0, 4, 4, 4, 4, (int) texWidth - 8, (int) texHeight - 8);	
		
		EFDebug.info("Pixmap Dims: " + Strings.vec2(texWidth, texHeight));
		
		EFDebug.info("Item Text Width: " + textWidth);
		
		long e = System.currentTimeMillis();
		double elapsedTime = (e - startTime) / 1000D;
		
		PixmapIO.writePNG(Gdx.files.external("Documents\\testpix.png"), pixmap);
		EFDebug.info("Holy crap, this took " + elapsedTime + "s");
		
		// Gets rid of all the crap
		lsPix.dispose();
		rsPix.dispose();
		usPix.dispose();
		dsPix.dispose();
		centerPix.dispose();
		
		// Returns the monstrosity that has been created.
		return new TextureRegion(new Texture(pixmap));
	}

	@Override
	public void onMouseHovered(int x, int y) {
		// TODO Auto-generated method stub
		
	}
	
	public Pixmap extractPixmapFromTextureRegion(TextureRegion textureRegion) {
	    TextureData textureData = textureRegion.getTexture().getTextureData();
	    if (!textureData.isPrepared()) {
	        textureData.prepare();
	    }
	    Pixmap pixmap = new Pixmap(
	            textureRegion.getRegionWidth(),
	            textureRegion.getRegionHeight(),
	            textureData.getFormat()
	    );
	    pixmap.drawPixmap(
	            textureData.consumePixmap(), // The other Pixmap
	            0, // The target x-coordinate (top left corner)
	            0, // The target y-coordinate (top left corner)
	            textureRegion.getRegionX(), // The source x-coordinate (top left corner)
	            textureRegion.getRegionY(), // The source y-coordinate (top left corner)
	            textureRegion.getRegionWidth(), // The width of the area from the other Pixmap in pixels
	            textureRegion.getRegionHeight() // The height of the area from the other Pixmap in pixels
	    );
	    return pixmap;
	}

	@Override
	public void onMouseDrag(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseWheelScrolled(int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void draw(SpriteBatch uiBatch, float delta) {
		float x = location.x;
		float y = location.y;
		
		float texWidth = skin.getRegionWidth();
		float texHeight = skin.getRegionHeight();
		
		if (location.x + texWidth >= 320f) {
			if (location.x - texWidth >= 10f) { // 10f is a threshold number for nice side margins
				x = location.x - texWidth;
			}
			else {
				x = 10f;
			}
		}
		if (location.y + texHeight >= 180f) {
			y = location.y - texHeight - 18f;
		}
		uiBatch.draw(skin, x, y);
		
		// Draws the info
		Item item = itemDisplayBox.getItem();
		font.setColor(EFUtil.getColorFromRGB(168, 102, 22));
		font.draw(uiBatch, item.getMaterial().getDisplayName(), x + 6f, y + texHeight - 6f);
		
		font.setColor(Color.WHITE);
		float loreY = y + texHeight - 16f;
		for (String loreLine : item.getMaterial().getLore()) {
			font.draw(uiBatch, loreLine, x + 6f, loreY);
			loreY -= 10f;
		}
		
	}

}
