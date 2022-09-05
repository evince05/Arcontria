package dev.eternalformula.arcontria.ui.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.ui.actions.ButtonClickAction;

public class EFCheckBox extends UIElement {
	
	private boolean isChecked;
	private TextureRegion checkedSkin;
	
	private ButtonClickAction btnClickAction;
	
	public EFCheckBox(UIContainer container) {
		super(container);
		
		this.isChecked = false;
		this.checkedSkin = null;
		
		this.btnClickAction = null;
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
		
		if (isChecked) {
			uiBatch.draw(checkedSkin, location.x, location.y);
		}
		else {
			uiBatch.draw(skin, location.x, location.y);
		}
	}
	
	@Override
	public void onMouseClicked(int x, int y, int button) {
		super.onMouseClicked(x, y, button);
		
		if (bounds.contains(x, y)) {
			isChecked = !isChecked;
			
			if (btnClickAction != null) {
				btnClickAction.onClick(x, y, button);
			}
		}
		
	}
	
	/**
	 * Gets the status (checked/unchecked) of the box.
	 */
	
	public boolean getValue() {
		return isChecked;
	}
	
	public void setValue(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
	public void setCheckedSkin(TextureRegion checkedSkin) {
		this.checkedSkin = checkedSkin;
	}
	
	public void setClickAction(ButtonClickAction action) {
		this.btnClickAction = action;
	}

}
