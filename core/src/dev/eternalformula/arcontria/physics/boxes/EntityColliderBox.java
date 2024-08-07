package dev.eternalformula.arcontria.physics.boxes;

import java.util.UUID;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.physics.B2DUtil;
import dev.eternalformula.arcontria.physics.PhysicsConstants.PhysicsCategory;

public class EntityColliderBox extends Box {
	
	private Entity entity;
	
	public EntityColliderBox(World world, Entity entity) {
		this.entity = entity;
		this.body = B2DUtil.createBody(world, entity.getLocation().x + 0.5f,
				entity.getLocation().y, entity.getWidth(), entity.getHeight() / 8f,
				BodyType.DynamicBody, PhysicsCategory.ENTITY_COLLIDER, this);
		this.id = UUID.randomUUID();
		body.setUserData(this);
	}
	
	public EntityColliderBox(World world, Entity entity, BodyType bodyType) {
		this.entity = entity;
		this.body = B2DUtil.createBody(world, entity.getLocation().x + 0.5f,
				entity.getLocation().y, entity.getWidth(), entity.getHeight() / 8f,
				bodyType, PhysicsCategory.ENTITY_COLLIDER, this);
		this.id = UUID.randomUUID();
		body.setUserData(this);
	}
	
	/**
	 * EntityColliderBox
	 * @param surroundEntity True if the box should surround the entire entity.
	 */
	
	public EntityColliderBox(World world, Entity entity, BodyType bodyType, boolean surroundEntity) {
		this.entity = entity;
		this.id = UUID.randomUUID();
		
		float height = entity.getHeight();
		if (!surroundEntity) {
			height = entity.getHeight() / 8f;
		}
		
		this.body = B2DUtil.createBody(world, entity.getLocation().x + entity.getWidth() / 2f,
				entity.getLocation().y + entity.getHeight() / 2f, entity.getWidth(), height,
				bodyType, PhysicsCategory.ENTITY_COLLIDER, this);
		body.setUserData(this);
	}
	
	
	public Entity getEntity() {
		return entity;
	}
	
	public UUID getId() {
		return id;
	}

}
