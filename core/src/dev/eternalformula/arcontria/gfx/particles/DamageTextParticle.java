package dev.eternalformula.arcontria.gfx.particles;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.util.EFMath;
import dev.eternalformula.arcontria.util.Strings;

/**
 * DamageTextParticles are particles that are emitted from an entity upon being damaged by the player.
 * These particles show how much damage the player has dealt to said entity.
 */

public class DamageTextParticle extends Particle {
	
	// Physics Variables
	
	// Time (Seconds)
	private static final float MIN_TIME = 0.8f;
	private static final float MAX_TIME = 1.3f;
	
	private static final float MIN_DISTANCE = 2f;
	private static final float MAX_DISTANCE = 3f;
	
	/**
	 * The angle of launching relative to the entity.
	 */
	private int angle;
	
	private float damage;
	private String damageStr;
	
	private float distance;
	
	private float speedX;
	private float speedY;
	
	private float deltaX;
	private float deltaY;
	
	private float targetX;
	private float targetY;
	
	private float maxTime;
	
	public DamageTextParticle(Vector2 origin, float damage) {
		this.position = new Vector2(origin);
		this.damage = damage;
		this.damageStr = String.valueOf(damage);
		this.maxTime = EFMath.randomFloat(MIN_TIME, MAX_TIME);
		this.distance = (float) ThreadLocalRandom.current().nextInt((int) MIN_DISTANCE, (int) MAX_DISTANCE);
		this.angle = ThreadLocalRandom.current().nextInt(0, 360);
		solveDeltas();
	}
	
	private void solveDeltas() {
		this.deltaX = Math.round(distance * Math.cos(Math.toRadians(angle)));
		this.deltaY = Math.round(distance * Math.sin(Math.toRadians(angle)));
		
		this.targetX = position.x + deltaX;
		this.targetY = position.y + deltaY;
		
		System.out.println("Delta:" + Strings.vec2(targetX, targetY));
		
		// Calculate X and Y speed
		this.speedX = deltaX / maxTime;
		this.speedY = deltaY / maxTime;
	}

	public Vector2 getPosition() {
		return position;
	}
	
	@Override
	public void draw(SpriteBatch batch, float delta) {
		ParticleManager.rend.rect(position.x, position.y, 16, 16);
	}
	
	@Override
	public void update(float delta) {
		if (Math.round(position.x) != Math.round(targetX)) {
			// Move on X
			position.x += speedX * delta;
		}
		
		if (Math.round(position.y) != Math.round(targetY)) {
			// Move on Y
			position.y += speedY * delta;
		}
	}
}
