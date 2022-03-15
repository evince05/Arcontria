package dev.eternalformula.arcontria.physics.boxes;

import java.util.UUID;

import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.B2DUtil;
import dev.eternalformula.arcontria.physics.PhysicsConstants.PhysicsCategory;

public class EntityAttackBox extends Box {
	
	private Entity entity;
	
	public EntityAttackBox(GameLevel level, Entity entity) {
		this.entity = entity;
		this.body = B2DUtil.createBody(level.getWorld(), entity.getLocation().x + 0.5f,
				entity.getLocation().y, 0, 0, PhysicsCategory.ENTITY_ATTACKBOX);
		this.id = UUID.randomUUID();
		body.setUserData(this);
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public UUID getId() {
		return id;
	}

}
