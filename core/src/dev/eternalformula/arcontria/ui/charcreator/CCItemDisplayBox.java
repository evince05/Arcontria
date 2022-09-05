package dev.eternalformula.arcontria.ui.charcreator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.UIElement;

public class CCItemDisplayBox extends UIElement {
	
	private ShaderProgram shader;
	private TextureRegion itemTex;
	private Color tintColor;
	
	public CCItemDisplayBox(UIContainer container, ShaderProgram shader) {
		super(container);
		
		this.shader = shader;
		this.tintColor = Color.WHITE;
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
		uiBatch.setColor(tintColor);
		
		uiBatch.setShader(shader);
		if (itemTex != null) {
			uiBatch.draw(itemTex, location.x + 2, location.y + 2);
		}
		uiBatch.setColor(Color.WHITE);
		uiBatch.setShader(null);
	}
	
	public void setItemColor(Color color) {
		this.tintColor = color;
	}
	
	public void setItemTexture(TextureRegion itemTex) {
		this.itemTex = itemTex;
	}

}
