package dev.eternalformula.arcontria.ui.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.util.Assets;

public class EFSlider extends UIElement {

	private float width;
	private float currentValue;
	
	private TextureRegion dialSkin;
	
	private static final float DIAL_OFFSET_X = -2;
	private static final float DIAL_OFFSET_Y = -2; 
	
	public EFSlider(UIContainer container, int x, int y) {
		super(container);
		
		// Loads textures.
		TextureAtlas uiAtlas = Assets.get("ui/elements/uiatlas.atlas", TextureAtlas.class);
		
		this.location = new Vector2(x, y);
		setSkin(uiAtlas.findRegion("sliderpane"));
		
		this.dialSkin = new TextureRegion(uiAtlas.findRegion("sliderdial"));
		this.currentValue = 0f;
		
		this.width = skin.getRegionWidth();
	}
	
	@Override
	public void onMouseClicked(int x, int y, int button) {
		super.onMouseClicked(x, y, button);
		
		if (bounds.contains(x, y)) {
			
			float localX = x - location.x;
			
			if (localX >= 0 && localX <= width) {
				currentValue = localX;
			}
		}
		
		
	}

	@Override
	public void onMouseReleased(int x, int y, int button) {
		super.onMouseReleased(x, y, button);
	}

	@Override
	public void onMouseHovered(int x, int y) {
	}
	
	@Override
	public void onMouseDrag(int x, int y) {
		
		if (container.focusedElement != null) {
			Vector2 lastClickPos = ArcontriaGame.GAME.getSceneManager()
					.getInputHandler().getLastClickLocation();
			
			if ((bounds.contains(x, y) && container.focusedElement.equals(this)) || 
					(bounds.contains(lastClickPos.x, lastClickPos.y))) {
				float localX = x - location.x;
				
				if (localX >= 0 && localX <= width) {
					currentValue = localX;
				}
			}
		}
		
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		uiBatch.draw(skin, location.x, location.y);
		
		float drawX = location.x + currentValue + DIAL_OFFSET_X;
		uiBatch.draw(dialSkin, drawX, location.y + DIAL_OFFSET_Y);
	}
	
	public float getCurrentPercent() {
		return currentValue / width;
	}

}
