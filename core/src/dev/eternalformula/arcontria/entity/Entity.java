package dev.eternalformula.arcontria.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
	
	protected Animation<TextureRegion> currentAnimation;
	
	/**
	 * Determines whether the entity should be rendered.
	 */
	protected boolean visible;
	
	protected Vector2 location;
	
	public float getX() {
		return location.x;
	}
	
	public void setX(float x) {
		this.location.x = x;
	}
	
	public float getY() {
		return location.y;
	}
	
	public void setY(float y) {
		this.location.y = y;
	}
	
	public Vector2 getLocation() {
		return location;
	}
	
	public void setLocation(Vector2 location) {
		this.location = location;
	}
	
	public Animation<TextureRegion> getCurrentAnimation() {
		return currentAnimation;
	}
	
	public void setCurrentAnimation(Animation<TextureRegion> animation) {
		this.currentAnimation = animation;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * Sets the visibility of the entity.
	 * @param visible True if the entity should be rendered, otherwise false.
	 */
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public abstract void draw(SpriteBatch batch, float delta);
	
	public abstract void update(float delta);

}
