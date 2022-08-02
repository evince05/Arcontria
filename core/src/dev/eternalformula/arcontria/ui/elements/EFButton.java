package dev.eternalformula.arcontria.ui.elements;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.gfx.text.FontUtil;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.ui.actions.ButtonClickAction;
import dev.eternalformula.arcontria.util.Assets;

public class EFButton extends UIElement {
	
	public static final int DEFAULT_MODE = 0;
	public static final int TOGGLE_MODE = 1;
	
	private TextureRegion clickedSkin;
	private String text;

	private BitmapFont font;
	
	private boolean isClicked;
	
	/**
	 * Determines the mode of the button.<br>
	 * <b>0</b> - Regular
	 * <b>1</b> - Toggle
	 */
	
	private int buttonMode;
	
	private ButtonClickAction btnClickAction;
		
	public EFButton(UIContainer container, int x, int y) {
		super(container);
		
		this.location = new Vector2(x, y);
		this.isClicked = false;
		
		this.clickedSkin = null;
		this.buttonMode = EFButton.DEFAULT_MODE;
		
		this.font = Assets.get("fonts/Habbo.fnt", BitmapFont.class);
	}
	
	@Override
	public void onMouseClicked(int x, int y, int button) {
		super.onMouseClicked(x, y, button);
		
		if (bounds.contains(x, y)) {
			
			if (buttonMode == EFButton.TOGGLE_MODE) {
				isClicked = !isClicked;
			}
			else {
				isClicked = true;
			}
			
			if (btnClickAction != null) {
				btnClickAction.onClick(x, y, button);
			}
		}
	}
	
	@Override
	public void onMouseReleased(int x, int y, int button) {
		super.onMouseReleased(x, y, button);
		
		Vector2 lastClickPos = ArcontriaGame.GAME.getSceneManager()
				.getInputHandler().getLastClickLocation();
		
		if (bounds.contains(x, y) || bounds.contains(lastClickPos.x, lastClickPos.y)) {
			
			// Release action only occurs when in default mode.
			if (buttonMode == EFButton.DEFAULT_MODE) {
				isClicked = false;
			}
			
		}
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
		
		if (isClicked) {
			uiBatch.draw(clickedSkin, location.x, location.y);
		}
		else {
			uiBatch.draw(skin, location.x, location.y);
		}
		
		if (text != null && !text.equals("")) {
			float width = FontUtil.getWidth(font, text);
			font.draw(uiBatch, text, location.x + skin.getRegionWidth() / 2f - width / 2f,
					location.y + skin.getRegionHeight() - 2f);
		}
	}
	
	/**
	 * Sets the skin to be used when the button is clicked.<br>
	 * For best results, this should be the same skin as the regular skin,<br>
	 * with only a difference in color.
	 * 
	 * @param skin The skin to be used
	 */
	
	public void setClickSkin(TextureRegion skin) {
		this.clickedSkin = skin;
	}
	
	public void setClicked(boolean isClicked) {
		this.isClicked = isClicked;
	}
	
	public void setClickAction(ButtonClickAction action) {
		this.btnClickAction = action;
	}
	
	/**
	 * Sets the mode of the button.
	 * @param buttonMode 0 = default, 1 = toggle.
	 */
	
	public void setButtonMode(int buttonMode) {
		this.buttonMode = buttonMode;
	}
	
	public void setText(String text) {
		this.text = text;
	}

}