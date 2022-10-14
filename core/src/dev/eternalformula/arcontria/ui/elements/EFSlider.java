package dev.eternalformula.arcontria.ui.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFDebug;

/**
 * An <b><u>EFSlider</b></u> is a UI slider that allows the user to select a value in the range [0, 1.0].<br>
 * If the slider uses {@link EFSlider#SLIDER_HORIZONTAL}, the slider's selection value will change<br>
 * as the user moves the slider dial along the x axis, with 0.0 being on the leftmost side, and 1.0 on<br>
 * the rightmost side.<br>
 * <br>
 * If the slider uses {@link EFSlider#SLIDER_VERTICAL}, the slider's selection value will change<br>
 * as the user moves the slider dial along the y axis, with 0.0 being on the top side, and 1.0 on<br>
 * the bottom side.<br>
 * 
 * @author EternalFormula
 */
public class EFSlider extends UIElement {

	public static final int SLIDER_HORIZONTAL = 0;
	public static final int SLIDER_VERTICAL = 1;
	
	private int sliderType;
	
	private float width;
	private float height;
	
	private float currentValue;
	
	private TextureRegion dialSkin;
	
	private float DIAL_OFFSET_X = -2;
	private float DIAL_OFFSET_Y = -2; 
	
	/**
	 * Constructs an EFSlider
	 * @param container The container to which the EFSlider will belong
	 * @param x The x location of the EFSlider
	 * @param y The y location of the EFSlider
	 * @param sliderType 0 for horizontal, 1 for vertical.<br>
	 * <t>Other inputs will result in {@link EFSlider#SLIDER_HORIZONTAL}.
	 */
	
	public EFSlider(UIContainer container, int x, int y, int sliderType) {
		super(container);
		
		// Converts the slider to its default (horizontal slider) if the input value is too great.
		this.sliderType = sliderType > 1 ? 0 : sliderType;
		
		// Loads textures.
		TextureAtlas uiAtlas = Assets.get("ui/elements/uiatlas.atlas", TextureAtlas.class);
		
		this.location = new Vector2(x, y);
		
		if (sliderType == EFSlider.SLIDER_HORIZONTAL) {
			// Horizontal Slider
			setSkin(uiAtlas.findRegion("sliderpaneHor"));
			this.dialSkin = new TextureRegion(uiAtlas.findRegion("sliderdialHor"));
			DIAL_OFFSET_X = -2;
			DIAL_OFFSET_Y = -2;
		}
		else {
			// Vertical Slider
			setSkin(uiAtlas.findRegion("sliderpaneVer"));
			this.dialSkin = new TextureRegion(uiAtlas.findRegion("sliderdialVer"));
			DIAL_OFFSET_X = -1;
			DIAL_OFFSET_Y = -1;
		}
		
		this.currentValue = 0f;
		
		this.width = skin.getRegionWidth();
		this.height = skin.getRegionHeight();
	}
	
	/**
	 * Constructs an {@link EFSlider} with the default sliderType,
	 * {@link EFSlider#SLIDER_HORIZONTAL}.
	 * 
	 * Refer to {@link EFSlider#EFSlider(UIContainer, int, int, int)} for
	 * documentation.
	 */
	
	public EFSlider(UIContainer container, int x, int y) {
		this(container, x, y, EFSlider.SLIDER_HORIZONTAL);
	}
	
	@Override
	public void onMouseClicked(int x, int y, int button) {
		super.onMouseClicked(x, y, button);
		
		if (sliderType == EFSlider.SLIDER_HORIZONTAL) {
			if (bounds.contains(x, y)) {
				
				float localX = x - location.x;
				
				if (localX >= 0 && localX <= width) {
					currentValue = localX;
				}
			}
		}
		else {
			// Vertical slider
			if (bounds.contains(x, y)) {
				float localY = location.y - y + height;
				
				if (localY >= 0 && localY <= height) {
					currentValue = localY;
				}
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
	public void onMouseWheelScrolled(int amount) {
		if (container.focusedElement != null) {
			EFDebug.info("Slider container has focus");
			if (container.focusedElement.equals(this)) {
				if (sliderType == EFSlider.SLIDER_VERTICAL) {
					
					if (Math.signum(currentValue) == 1) {
						// heading down
						if (currentValue + amount <= height) {
							currentValue += amount;
						}
						else {
							currentValue = height;
						}
					}
					else {
						// Heading up;
						if (currentValue - amount >= 0) {
							currentValue -= amount;
						}
						else {
							currentValue = 0;
						}
					}
					currentValue += amount;
				}
				
			}
		}
	}
	
	@Override
	public void onMouseDrag(int x, int y) {
		if (container.focusedElement != null) {
			Vector2 lastClickPos = ArcontriaGame.GAME.getSceneManager()
					.getInputHandler().getLastClickLocation();
			
			if ((bounds.contains(x, y) && container.focusedElement.equals(this)) || 
					(bounds.contains(lastClickPos.x, lastClickPos.y))) {
				
				// Element is being dragged
				if (sliderType == EFSlider.SLIDER_HORIZONTAL) {
					float localX = x - location.x;
					
					if (localX >= 0 && localX <= width) {
						currentValue = localX;
					}
				}
				else {
					// Vertical slider
					float localY = location.y - y + height;
					if (localY >= 0 && localY <= height) {
						currentValue = localY;
					}
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
		
		if (sliderType == EFSlider.SLIDER_HORIZONTAL) {
			float drawX = location.x + currentValue + DIAL_OFFSET_X;
			uiBatch.draw(dialSkin, drawX, location.y + DIAL_OFFSET_Y);
		}
		else {
			// Vertical slider
			float drawX = location.x + DIAL_OFFSET_X;
			float drawY = location.y + height - currentValue + DIAL_OFFSET_Y;
			uiBatch.draw(dialSkin, drawX, drawY);
		}
		
	}
	
	public float getCurrentPercent() {
		return sliderType == EFSlider.SLIDER_HORIZONTAL ?
				currentValue / width : currentValue / height;
	}
	
	public void setCurrentPercent(float percent) {
		if (sliderType == EFSlider.SLIDER_HORIZONTAL) {
			// Horizontal
			this.currentValue = percent * width;
		}
		else {
			// Vertical
			this.currentValue = percent * height;
		}
		
	}

}
