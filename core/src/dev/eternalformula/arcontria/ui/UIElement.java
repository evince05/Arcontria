package dev.eternalformula.arcontria.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.util.EFDebug;

public abstract class UIElement {
	
	protected UIContainer container;
	
	protected boolean visible;
	protected boolean active;
	protected Rectangle bounds;
	
	protected TextureRegion skin;
	
	protected Vector2 location;
	protected Vector2 oldLocation;
	
	protected UIElement(UIContainer container) {
		this.container = container;
		this.visible = true;
		this.active = true;
		this.location = new Vector2(0f, 0f);
	}
	
	/**
	 * Container constructor.
	 */
	
	protected UIElement() {
		this.visible = true;
		this.active = true;
		this.location = new Vector2(0f, 0f);
	}

	/**
	 * Sets the skin of the UIElement and updates the bounds.
	 * @param skin The TextureRegion to be used.
	 */
	
	public void setSkin(TextureRegion skin) {
		this.skin = skin;
		this.bounds = new Rectangle(location.x, location.y, 
				skin.getRegionWidth(), skin.getRegionHeight());
	}
	
	public Vector2 getLocation() {
		return location;
	}
	
	public void setLocation(Vector2 location) {
		this.location = location;
	}
	
	public void setLocation(float x, float y) {
		this.location = new Vector2(x, y);
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean isVisible) {
		this.visible = isVisible;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean isActive) {
		this.active = isActive;
	}
	
	/**
	 * Handles when the mouse clicks on the UIElement.
	 * @param x The x location of the click.
	 * @param y The y location of the click.
	 * @param button The mouse button used during the click.
	 */
	
	public void onMouseClicked(int x, int y, int button) {
		if (container != null) {
			if (bounds.contains(x, y)) {
				container.focusedElement = this;
				
				if (container.hasParent()) {
					container.getParent().focusedElement = this;
				}
			}
		}
	}
	
	/**
	 * Handles when the mouse is released after a click on the UIElement.
	 * @param x The x location of the click.
	 * @param y The y location of the click.
	 * @param button The mouse button that was used during the initial click.
	 */
	
	public void onMouseReleased(int x, int y, int button) {
		
		/*
		 * if (container != null) {
		 *
			Vector2 lastClickPos = ArcontriaGame.GAME.getSceneManager()
					.getInputHandler().getLastClickLocation();
			if (bounds.contains(x, y) || bounds.contains(lastClickPos.x, lastClickPos.y)) {
				container.focusedElement = null;
			}
		}*/
	}
	
	/**
	 * Handles when the mouse is being hovered over the UIElement.
	 * @param x The x location of the mouse hover.
	 * @param y The y location of the mouse hover.
	 */
	
	public abstract void onMouseHovered(int x, int y);
	
	/**
	 * Handles when the mouse is being dragged across the UIElement.
	 * @param x The x location of the mouse.
	 * @param y The y location of the mouse.
	 */
	
	public abstract void onMouseDrag(int x, int y);
	
	
	/**
	 * Handles when a key is typed.
	 * @param key The key that was typed.
	 */
	
	public void onKeyTyped(char key) {
	}
	
	/**
	 * Updates the UIElememt
	 */
	
	public abstract void update(float delta);
	
	/**
	 * Draws the UIElement.
	 */
	
	public abstract void draw(SpriteBatch uiBatch, float delta);
	
}
