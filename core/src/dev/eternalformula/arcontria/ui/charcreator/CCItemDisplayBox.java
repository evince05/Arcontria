package dev.eternalformula.arcontria.ui.charcreator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.UIElement;

public class CCItemDisplayBox extends UIElement {

	private TextureRegion itemTex;
	private Color tintColor;
	
	public CCItemDisplayBox(UIContainer container) {
		super(container);
	}
	
	@Override
	public void onMouseHovered(int x, int y) {
	}

	@Override
	public void onMouseDrag(int x, int y) {
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		uiBatch.draw(skin, location.x, location.y);
		uiBatch.setColor(tintColor);
		
		if (itemTex != null) {
			uiBatch.draw(itemTex, location.x + 2, location.y + 2);
		}
		uiBatch.setColor(Color.WHITE);
	}
	
	public void setItemTexture(TextureRegion itemTexture) {
		this.itemTex = itemTexture;
	}
	
	public void setItemColor(Color color) {
		this.tintColor = color;
	}

}
