package dev.eternalformula.arcontria.combat;

import dev.eternalformula.arcontria.entity.Entity;

public class DamageSource {
	
	private Entity entity;
	
	public DamageSource(Entity entity) {
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return entity;
	}

}
