package dev.eternalformula.arcontria.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.arcontria.gfx.EGFXUtil;
import dev.eternalformula.arcontria.gfx.text.FontUtil;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.util.EFConstants;

/**
 * A class used to simplify inventory rendering and management.
 * @author EternalFormula
 */

public class InventoryHandler {

	private GameLevel level;
	
	private boolean inventoryOpen;
	
	private Inventory inventory;
	
	private Texture texture;
	
	private BitmapFont font;
	
	public InventoryHandler(GameLevel level, Inventory inventory) {
		this.level = level;
		this.inventory = inventory;
		this.inventoryOpen = false;
		
		this.texture = new Texture(Gdx.files.internal("ui/inventory/inventory.png"));
		this.font = FontUtil.createBasicFont("fonts/alagard.ttf", 16);
	}
	
	public boolean isInventoryOpen() {
		return inventoryOpen;
	}
	
	public void open() {
		this.inventoryOpen = true;
	}
	
	public void close() {
		this.inventoryOpen = false;
	}
	
	public void toggle() {
		this.inventoryOpen = !inventoryOpen;
	}
	
	/**
	 * Handles inventory updating.
	 * @param delta The amount of time elapsed (in seconds) since the last draw call.
	 */
	
	public void update(float delta) {
	}
	
	/**
	 * Draws the inventory. This will only be called if the inventory is open.
	 * @param batch The batch used (this should always be the UI batch).
	 * @param delta The amount of time elapsed (in seconds) since the last draw call.
	 */
	
	public void draw(SpriteBatch batch, float delta) {
		
		batch.draw(texture, 0, 0, texture.getWidth() * EGFXUtil.RENDER_SCALE, texture.getHeight() * EGFXUtil.RENDER_SCALE);
		
		font.setColor(EFConstants.INVENTORY_TEXT_COLOR);
		font.getData().setScale(EGFXUtil.RENDER_SCALE);
		font.draw(batch, "Player", 51.5f * EGFXUtil.RENDER_SCALE, 159.5f * EGFXUtil.RENDER_SCALE);
		font.draw(batch, "Inventory", 125 * EGFXUtil.RENDER_SCALE, 159.5f * EGFXUtil.RENDER_SCALE);
		font.draw(batch, "Effects", 218.5f * EGFXUtil.RENDER_SCALE, 159.5f * EGFXUtil.RENDER_SCALE);
	}
}
