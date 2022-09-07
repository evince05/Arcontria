package dev.eternalformula.arcontria.ui.inventory;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.items.Item;

public class InventoryUI {

	public static void drawItem(SpriteBatch uiBatch, Item item, Vector2 location) {
		TextureRegion itemIcon = item.getMaterial().getIcon();
		uiBatch.draw(itemIcon, location.x, location.y);
	}
}
