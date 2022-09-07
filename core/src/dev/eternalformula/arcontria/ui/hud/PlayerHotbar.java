package dev.eternalformula.arcontria.ui.hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.inventory.PlayerInventory;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.ui.inventory.InventoryUI;
import dev.eternalformula.arcontria.util.Assets;

public class PlayerHotbar extends UIElement {
	
	public static final int SIZE = 10;
	
	private PlayerInventory inventory;
	
	private int currentItem;
	
	PlayerHotbar(PlayerInventory inventory) {
		this.inventory = inventory;
		
		TextureRegion skin = Assets.get("ui/inventory/gameui.atlas", 
				TextureAtlas.class).findRegion("hotbar");
		setSkin(skin);
		currentItem = 0;
		
		location = new Vector2(160f - skin.getRegionWidth() / 2f,
				4f);
	}

	@Override
	public void onMouseHovered(int x, int y) {
	}

	@Override
	public void onMouseDrag(int x, int y) {
	}
	
	@Override
	public void onMouseWheelScrolled(int amount) {
		if (currentItem + amount >= 0 && currentItem + amount < SIZE) {
			currentItem += amount;
			System.out.println("Current Item: " + currentItem);
		}
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		uiBatch.draw(skin, location.x, location.y);
		
		for (int hotbarItem = 0; hotbarItem < inventory.getRowWidth(); hotbarItem++) {
			if (inventory.getItem(hotbarItem) != null) {
				
				// Draws the item at the specified hotbar slot
				InventoryUI.drawItem(uiBatch, inventory.getItem(hotbarItem),
						new Vector2(location.x + 17 * hotbarItem + 2f, location.y + 2f));
			}
			
		}
		
		// Draws the selection box over the current item
		Rectangle rect = new Rectangle(location.x + 1f + 17f * currentItem, location.y + 1f, 18f, 18f);
		//EGFXUtil.drawColorRect(uiBatch, Color.RED, rect, false);
	}
	
	public int getCurrentItem() {
		return currentItem;
	}
	
	public void setCurrentItem(int item) {
		this.currentItem = item;
	}
	
	public void setInventory(PlayerInventory inv) {
		this.inventory = inv;
	}

}
