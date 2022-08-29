package dev.eternalformula.arcontria.physics.boxes;

import java.util.UUID;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.physics.B2DUtil;
import dev.eternalformula.arcontria.physics.PhysicsConstants.PhysicsCategory;

public class EntityHitbox extends Box {
	
	private Entity entity;
	
	public EntityHitbox(World world, Entity entity) {
		this.entity = entity;
		this.body = B2DUtil.createBody(world, entity.getLocation().x + 0.5f,
				entity.getLocation().y + 1f, entity.getWidth(), entity.getHeight(), 
				BodyType.DynamicBody, PhysicsCategory.ENTITY_HITBOX, this);
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
