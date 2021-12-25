package dev.eternalformula.arcontria.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.eternalformula.arcontria.util.EFConstants;

public abstract class LivingEntity extends Entity {
	
	protected float health;
	protected float maxHealth;
	
	/**
	 * Default speed is 0f.
	 */
	protected float speed;
	private float elapsedTime;
	
	public void heal(float amount) {
		if (health + amount >= maxHealth) {
			this.health = maxHealth;
		}
		else {
			this.health += amount;
		}
	}
	
	public void damage(float amount) {
		if (health - amount <= 0) {
			this.health = 0;
		}
		else {
			health -= amount;
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float delta) {
		elapsedTime += Gdx.graphics.getDeltaTime();
		TextureRegion texRegion = currentAnimation.getKeyFrame(elapsedTime, true);
		float width = texRegion.getRegionWidth() / EFConstants.PPM;
		float height = texRegion.getRegionHeight() / EFConstants.PPM;
		batch.draw(texRegion, location.x, location.y, width, height);
	}

	@Override
	public void update(float delta) {
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(float maxHealth) {
		this.maxHealth = maxHealth;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public abstract void moveLeft(float delta);
	public abstract void moveRight(float delta);
	public abstract void moveUp(float delta);
	public abstract void moveDown(float delta);

}
