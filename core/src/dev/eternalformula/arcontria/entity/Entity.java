package dev.eternalformula.arcontria.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import dev.eternalformula.arcontria.level.GameLevel;

public abstract class Entity {
	
	protected Animation<TextureRegion> currentAnimation;
	
	// Physics Stuff! :)
	
	/**
	 * Determines whether the entity should be rendered.
	 */
	protected boolean visible;
	
	protected Vector2 location;
	protected float width;
	protected float height;
	
	protected GameLevel level;
	
	protected Entity(GameLevel level) {
		this.level = level;
		this.location = new Vector2(0, 0);
	}
	
	protected Entity() {
		this.level = null;
		this.location = new Vector2(0, 0);
	}
	
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
	
	public float getWidth() {
		return width;
	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	public Vector2 getLocation() {
		return location;
	}
	
	public void setLocation(Vector2 location) {
		this.location = location;
		//this.body.setTransform(location, 0);
	}
	
	public void setLocation(float x, float y) {
		this.location = new Vector2(x, y);
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
	
	public void update(float delta) {
		
		/*
		if (body != null) {
			this.location.x = body.getPosition().x - width / 2f;
			this.location.y = body.getPosition().y - height / 2f;
		}
		*/
		
	}
	
	public void setLevel(GameLevel level) {
		this.level = level;
	}

	/**
	 * Breaks the rock and yields ore.
	 */
	public abstract void destroyBodies(World world);
}
