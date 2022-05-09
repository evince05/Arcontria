package dev.eternalformula.arcontria.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.combat.DamageSource;
import dev.eternalformula.arcontria.gfx.particles.DamageTextParticle;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.boxes.Box;
import dev.eternalformula.arcontria.util.EFConstants;
import dev.eternalformula.arcontria.util.EFDebug;

public abstract class LivingEntity extends Entity {
	
	protected float health;
	protected float maxHealth;
	
	protected int direction; // 1 = up; 2 = left; 3 = right; 4 = down;
	protected boolean isMoving;
	
	// PHYSICS :)
	protected Box hitbox;
	protected Box colliderBox;
	
	/**
	 * Default speed is 0f.
	 */
	protected float speed;
	protected float elapsedTime;
	
	protected int lastAnimationDirection;
	
	public LivingEntity(GameLevel level) {
		super(level);
		this.direction = 4;
		this.lastAnimationDirection = direction;
		
		EFDebug.debug("(LivingEntity.java:34) Remember: Body positioning is now controlled by"
				+ " hitboxes in \n\tLivingEntity's update method.");
	}
	
	public void heal(float amount) {
		if (health + amount >= maxHealth) {
			this.health = maxHealth;
		}
		else {
			this.health += amount;
		}
	}
	
	public void damage(DamageSource source, float amount, boolean isCritStrike) {
		
		float damageAmount = amount;
		if (health - amount <= 0) {
			damageAmount = health;
			this.health = 0;
		}
		else {
			health -= amount;
		}
		
		level.getParticleHandler().spawnParticle(new DamageTextParticle(location, damageAmount, isCritStrike));
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
	public void setLocation(Vector2 location) {
		this.location = location;
		this.hitbox.getBody().setTransform(location.x + width / 2f,
				location.y + height / 2f, 0);
		// height / 8 is the default entity colliderbox height, so the center has to be height / 16.
		this.colliderBox.getBody().setTransform(location.x + width / 2f, location.y + height / 16f, 0);
	}
	
	@Override
	public void setLocation(float x, float y) {
		setLocation(new Vector2(x, y));
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (currentAnimation.isAnimationFinished(elapsedTime)) {
			elapsedTime = 0f;
		}
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
	
	public void move(float horizontalVelocity, float verticalVelocity) {
		
		// TODO: Do for each body.
		colliderBox.setLinearVelocity(horizontalVelocity, verticalVelocity);
		
		Vector2 hitboxPos = colliderBox.getBody().getPosition();
		hitboxPos.y = hitboxPos.y - height / 16f + height / 2f;
		
		this.hitbox.getBody().setTransform(hitboxPos, 0);
		this.location.x = hitboxPos.x - width / 2f;
		this.location.y = hitboxPos.y - height / 2f;
		
		/*
		this.location.x += horizontalVelocity * delta;
		this.location.y += verticalVelocity * delta;
		*/
		isMoving = true;
	}		
		
		/* 
		this.location.x += horizontalVelocity * delta;
		this.location.y += verticalVelocity * delta;
		*/
	
	public void setMotile(boolean motility) {
		hitbox.setLinearVelocity(0, 0);
		colliderBox.setLinearVelocity(0, 0);
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public int getLastAnimationDirection() {
		return lastAnimationDirection;
	}
}
