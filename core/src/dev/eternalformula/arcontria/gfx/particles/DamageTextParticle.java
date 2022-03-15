package dev.eternalformula.arcontria.gfx.particles;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.EFMath;
import dev.eternalformula.arcontria.util.Strings;

/**
 * DamageTextParticles are particles that are emitted from an entity upon being damaged by the player.
 * These particles show how much damage the player has dealt to said entity.
 */

public class DamageTextParticle extends Particle {
	
	// Physics Variables
	
	// Time (Seconds)
	private static final float MIN_TIME = 0.5f;
	private static final float MAX_TIME = 0.6f;
	
	private static final float MIN_DISTANCE = 3.0f;
	private static final float MAX_DISTANCE = 3.0f;
	
	private static final float LIFE_SPAN = 0.75f;
	
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
	private float alpha;
	
	private float fadeSpeed;
	private Color fontColor;
	
	public DamageTextParticle(Vector2 origin, float damage) {
		this.position = new Vector2(origin);
		this.damage = damage;
		this.damageStr = String.valueOf(Math.round(damage));
		this.maxTime = EFMath.randomFloat(MIN_TIME, MAX_TIME);
		this.distance = ThreadLocalRandom.current().nextFloat() * (MAX_DISTANCE - MIN_DISTANCE) + MIN_DISTANCE;
		EFDebug.debug("Particle Distance: " + distance);
		this.angle = ThreadLocalRandom.current().nextInt(0, 360);
		this.fontColor = new Color(1f, 1f, 1f, 1f);
		solveDeltas();
	}
	
	private void solveDeltas() {
		this.deltaX = distance * (float) Math.cos(Math.toRadians(angle));
		this.deltaY = distance * (float) Math.sin(Math.toRadians(angle));
		
		EFDebug.debug("Particle Angle: " + angle);
		EFDebug.debug("Particle Deltas: " + Strings.vec2(deltaX, deltaY));
		
		this.targetX = position.x + deltaX;
		this.targetY = position.y + deltaY;
		
		// Calculate X and Y speed
		this.speedX = deltaX / maxTime;
		this.speedY = deltaY / maxTime;
		
		this.fadeSpeed = 1.0f / LIFE_SPAN;
	}

	public Vector2 getPosition() {
		return position;
	}
	
	@Override
	public void draw(ParticleHandler handler, SpriteBatch batch, float delta) {
		handler.getTextParticleFont().setColor(fontColor);
		handler.getTextParticleFont().draw(batch, damageStr, position.x, position.y);
	}
	
	@Override
	public void update(ParticleHandler handler, float delta) {
		Vector2 oldPos = new Vector2(position);
		
		
		/* TODO Remove old code
		if (Math.round(position.x) <= Math.round(targetX)) {
			// Move on X
			position.x += speedX * delta;
		}
		
		// TODO: Fix Rounding Check
		if (Math.round(position.y) <= Math.round(targetY)) {
			// Move on Y
			position.y += speedY * delta;
		}*/
		
		if (position.x < targetX) {
			// needs to move right.
			
			position.x += speedX * delta;
			if (position.x > targetX) {
				
				// stops movement
				position.x = targetX;
			}
		}
		else if (position.x > targetX) {
			// needs to move left (speed SHOULD be negative here)
			position.x += speedX * delta;
			if (position.x < targetX) {
				position.x = targetX;
			}
		}
		
		if (position.y < targetY) {
			// needs to move up.
			
			position.y += speedY * delta;
			if (position.y > targetY) {
				
				// stops movement
				position.y = targetY;
			}
		}
		else if (position.y > targetY) {
			// needs to move down (speed SHOULD be negative here)
			position.y += speedY * delta;
			if (position.y < targetY) {
				position.y = targetY;
			}
		}
		
		
		
		if (position.x == oldPos.x && position.y == oldPos.y) {
			// No movement.
			float alpha = fontColor.a - fadeSpeed / 60;
			
			if (alpha <= 0) {
				handler.removeParticle(this);
			}
			else {
				fontColor.a = alpha;
			}
			
		}
	}
}
