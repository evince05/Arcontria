package dev.eternalformula.arcontria.ui.charcreator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.gfx.text.FontUtil;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.elements.EFButton;
import dev.eternalformula.arcontria.util.Assets;

public class CCItemSelector extends UIContainer {
	
	private EFButton frontArrow;
	private EFButton backArrow;
	private CCItemDisplayBox displayBox;
	
	private BitmapFont font;
	
	private String itemName;
	private int itemId;
	
	public CCItemSelector(UIContainer parent, int x, int y) {
		super(parent);
		
		this.location = new Vector2(x, y);
		
		// Atlases
		TextureAtlas uiAtlas = Assets.get("ui/elements/uiatlas.atlas", TextureAtlas.class);
		TextureAtlas ccAtlas = Assets.get("ui/charcreator/charcreator.atlas", TextureAtlas.class);
		TextureRegion directionBtns = uiAtlas.findRegion("directionbuttons");
		
		this.font = Assets.get("fonts/Habbo.fnt", BitmapFont.class);
		
		this.backArrow = new EFButton(this, x - 13 , y + 4);
		backArrow.setSkin(new TextureRegion(directionBtns, 0, 0, 10, 10));
		backArrow.setClickSkin(new TextureRegion(directionBtns, 20, 0, 10, 10)); 
		
		this.frontArrow = new EFButton(this, x + 23, y + 4);
		frontArrow.setSkin(new TextureRegion(directionBtns, 10, 0, 10, 10));
		frontArrow.setClickSkin(new TextureRegion(directionBtns, 30, 0, 10, 10));
		
		this.displayBox = new CCItemDisplayBox(this);
		displayBox.setSkin(ccAtlas.findRegion("selectiondisplay"));
		displayBox.setLocation(x, y); 
		
		this.itemName = "";
		
		addChildren(backArrow, frontArrow, displayBox);
	}
	
	/**
	 * Sets the string to display under the ItemSelector.
	 * @param itemName The name of the item type.
	 */
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public void setItemColor(Color color) {
		displayBox.setItemColor(color);
	}
	
	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		super.draw(uiBatch, delta);
		
		if (!itemName.equals("")) {
			String displayText = itemName + " " + itemId;
			float textWidth = FontUtil.getWidth(font, displayText);
			
			font.draw(uiBatch, displayText, location.x + 10 - textWidth / 2f,
					location.y + 2);
		}
	}

}
