package dev.eternalformula.arcontria.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

import dev.eternalformula.arcontria.util.EntityUtil;

public class HealthComponent implements Component {
	
	private float health;
	
	private float maxHealth;
	
	private boolean invincible;
	
	private boolean dead;
	
	private float damageCooldownTimer;
	
	private float damageCooldown = EntityUtil.DEFAULT_DAMAGE_COOLDOWN;
	
	public static final ComponentMapper<HealthComponent> Map = 
			ComponentMapper.getFor(HealthComponent.class);
	
	public HealthComponent() {
		this.maxHealth = 100f;
		this.health = maxHealth;
		this.invincible = false;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void damage(float dmgAmount) {
		
		if (health - dmgAmount >= 0f) {
			health -= dmgAmount;
		}
		else {
			health = 0f;
		}
		
		if (health <= 0f) {
			dead = true;
		}
		
		invincible = true;
		damageCooldownTimer = 0f;
	}
	
	public void heal(float healAmount) {
		
		if (health + healAmount <= maxHealth) {
			health += healAmount;
		}
		else {
			health = maxHealth;
		}
	}
	
	public float getHealth() {
		return health;
	}
	
	public void setHealth(float health) {
		this.health = health;
		if (health <= 0) {
			dead = true;
		}
		
	}
	
	public float getMaxHealth() {
		return maxHealth;
	}
	
	public void setMaxHealth(float maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public boolean isInvincible() {
		return invincible;
	}
	
	
}
