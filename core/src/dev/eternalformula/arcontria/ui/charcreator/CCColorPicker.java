package dev.eternalformula.arcontria.ui.charcreator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.ui.elements.EFSlider;
import dev.eternalformula.arcontria.util.Assets;

public class CCColorPicker extends UIContainer {
	
	private EFSlider redSlider;
	private EFSlider greenSlider;
	private EFSlider blueSlider;
	private ColorDisplayBox displayBox;
	
	public UIElement focusedElement;
	
	private BitmapFont font;
	
	private Color color;
	
	public CCColorPicker(int x, int y) {
		super();
		
		this.location = new Vector2(x, y);
		
		TextureAtlas uiAtlas = Assets.get("ui/elements/uiatlas.atlas", TextureAtlas.class);
		setSkin(uiAtlas.findRegion("colorpickerpane"));
		
		this.focusedElement = null;
		
		this.color = new Color(0f, 0f, 0f, 1f);
		
		this.font = Assets.get("fonts/Habbo.fnt", BitmapFont.class);
		
		this.redSlider = new EFSlider(this, x + 7, y + 63);
		this.greenSlider = new EFSlider(this, x + 7, y + 50);
		this.blueSlider = new EFSlider(this, x + 7, y + 37);
		this.displayBox = new ColorDisplayBox(this, x + 41, y + 8);
		
		addChildren(displayBox, redSlider, greenSlider, blueSlider);
		
	}

	@Override
	public void onMouseClicked(int x, int y, int button) {
		children.forEach(e -> {
			e.onMouseClicked(x, y, button);
		});
	}

	@Override
	public void onMouseReleased(int x, int y, int button) {
		children.forEach(e -> {
			e.onMouseReleased(x, y, button);
		});
	}

	@Override
	public void onMouseHovered(int x, int y) {
		children.forEach(e -> {
			e.onMouseHovered(x, y);
		});
	}
	
	@Override
	public void onMouseDrag(int x, int y) {
		children.forEach(e -> {
			e.onMouseDrag(x, y);
		});
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		
		Color displayColor = new Color(redSlider.getCurrentPercent(),
				greenSlider.getCurrentPercent(), blueSlider.getCurrentPercent(), 1f);
		
		this.color = displayColor;
		displayBox.setColor(displayColor);
		
	}

	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		super.draw(uiBatch, delta);
		
		font.draw(uiBatch, "R: " + Math.round(color.r * 255f), location.x + 63, location.y + 75);
		font.draw(uiBatch, "G: " + Math.round(color.g * 255f), location.x + 63, location.y + 62);
		font.draw(uiBatch, "B: " + Math.round(color.b * 255f), location.x + 63, location.y + 49);
	}
	
	/**
	 * Gets the color that is currently selected in the ColorPicker.
	 */
	
	public Color getColor() {
		return color;
	}

}
