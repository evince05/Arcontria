package dev.eternalformula.arcontria.ui.navmenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.items.Item;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.util.Assets;

public class ItemDisplayBox extends UIElement {
	
	private Item item;
	private ItemTooltip itemTooltip;
	private boolean shouldDisplayInfo;
	
	public ItemDisplayBox(UIContainer parent, int x, int y) {
		super(parent);
		this.location = new Vector2(x, y);
		
		TextureAtlas uiAtlas = Assets.get("ui/elements/uiatlas.atlas", TextureAtlas.class);
		TextureRegion skin = new TextureRegion(uiAtlas.findRegion("itembox"), 0, 0, 18, 18);
		setSkin(skin);
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setItem(Item item) {
		this.item = item;
		this.itemTooltip = ItemTooltip.createTooltip(this);
	}

	@Override
	public void onMouseHovered(int x, int y) {
		if (bounds.contains(x, y)) {
			hoverTime += Gdx.graphics.getDeltaTime();
		}
		else {
			hoverTime = 0f;
		}
		
		if (hoverTime >= 0.1f) {
			shouldDisplayInfo = true;
		}
		else {
			shouldDisplayInfo = false;
		}
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
	protected void draw(SpriteBatch uiBatch, float delta) {
		uiBatch.draw(skin, location.x, location.y);
		
		if (item != null) {
			TextureRegion itemIcon = item.getMaterial().getIcon();
			
			float scaleX = 1f;
			float scaleY = 1f;
			
			if (shouldDisplayInfo) {
				scaleX = 1.0625f;
				scaleY = 1.0625f;
				
				if (itemTooltip != null) {
					itemTooltip.setLocation(location.x + 18f, location.y + 18f);
					itemTooltip.draw(uiBatch, delta);
				}
			}
			
			uiBatch.draw(itemIcon, location.x + 1f, location.y + 1f, 0.0625f, 0.0625f, 
					16f, 16f, scaleX, scaleY, 0f);
			
			BitmapFont font = Assets.get("fonts/pixelfj-8.fnt", BitmapFont.class);
			font.draw(uiBatch, String.valueOf(item.getAmount()), location.x + 14f, location.y + 4);
			
		}
	}

}
