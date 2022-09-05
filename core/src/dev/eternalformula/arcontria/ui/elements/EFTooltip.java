package dev.eternalformula.arcontria.ui.elements;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import dev.eternalformula.arcontria.gfx.text.FontUtil;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.util.Assets;

public class EFTooltip extends UIElement {
	
	private String text;
	
	private BitmapFont font;
	
	public EFTooltip(String text) {
		this.text = text;
		
		this.font = Assets.get("fonts/Habbo.fnt", BitmapFont.class);
		
		TextureAtlas atlas = Assets.get("ui/elements/uiatlas.atlas", TextureAtlas.class);
		if (FontUtil.getWidth(font, text) <= (64 - 4f)) {
			setSkin(atlas.findRegion("tooltip"));
		}
		else {
			setSkin(atlas.findRegion("tooltip-long"));
		}
	}

	@Override
	public void onMouseHovered(int x, int y) {
	}

	@Override
	public void onMouseDrag(int x, int y) {
	}

	@Override
	public void onMouseWheelScrolled(int amount) {
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void draw(SpriteBatch uiBatch, float delta) {

		uiBatch.draw(skin, location.x, location.y);
		font.draw(uiBatch, text, location.x + 4f, location.y + 18f); 
		
	}

}
