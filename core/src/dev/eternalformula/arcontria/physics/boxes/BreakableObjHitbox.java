package dev.eternalformula.arcontria.physics.boxes;

import java.util.UUID;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.physics.B2DUtil;
import dev.eternalformula.arcontria.physics.PhysicsConstants.PhysicsCategory;

public class BreakableObjHitbox extends Box {

	private Entity entity;
	
	public BreakableObjHitbox(World world, Entity entity) {
		this.entity = entity;
		float x = entity.getLocation().x + entity.getWidth() / 2f;
		float y = entity.getLocation().y + entity.getHeight() / 2f;
		
		this.body = B2DUtil.createBody(world, x, y, entity.getWidth(), entity.getHeight(),
				BodyType.StaticBody, PhysicsCategory.BREAKABLEOBJ_HITBOX, this);
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
