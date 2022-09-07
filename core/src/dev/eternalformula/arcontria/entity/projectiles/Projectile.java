package dev.eternalformula.arcontria.entity.projectiles;

import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.boxes.ProjectileBox;

public abstract class Projectile extends Entity {
	
	protected ProjectileBox box;
	
	protected float damage;
	
	public Projectile(GameLevel level, float damage) {
		super(level);
		this.damage = damage;
	}
	
	public float getDamage() {
		return damage;
	}
	
	public ProjectileBox getBox() {
		return box;
	}
	
	public void destroy() {
		level.removeEntity(this);
	}
}
