package dev.eternalformula.arcontria.physics.boxes;

import java.util.UUID;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.B2DUtil;
import dev.eternalformula.arcontria.physics.PhysicsConstants.PhysicsCategory;

public class EntityColliderBox extends Box {
	
	private Entity entity;
	
	public EntityColliderBox(GameLevel level, Entity entity) {
		this.entity = entity;
		this.body = B2DUtil.createBody(level.getWorld(), entity.getLocation().x + 0.5f,
				entity.getLocation().y, entity.getWidth(), entity.getHeight() / 8f,
				BodyType.DynamicBody, PhysicsCategory.ENTITY_COLLIDER, this);
		this.id = UUID.randomUUID();
		body.setUserData(this);
	}
	
	public EntityColliderBox(GameLevel level, Entity entity, BodyType bodyType) {
		this.entity = entity;
		this.body = B2DUtil.createBody(level.getWorld(), entity.getLocation().x + 0.5f,
				entity.getLocation().y, entity.getWidth(), entity.getHeight() / 8f,
				bodyType, PhysicsCategory.ENTITY_COLLIDER, this);
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