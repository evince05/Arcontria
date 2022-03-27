package dev.eternalformula.arcontria.physics.boxes;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.PhysicsConstants;
import dev.eternalformula.arcontria.physics.PhysicsConstants.PhysicsCategory;

public class CustomEntityColliderBox extends Box {
	
	private Body body;
	private Entity entity;
	
	public CustomEntityColliderBox(GameLevel level, Entity entity,
			float xOffset, float yOffset, float width, float height,
			BodyType bodyType) {
		
		this.entity = entity;
		
		Body body;
		BodyDef def = new BodyDef();
		def.type = bodyType;
		def.position.set(entity.getLocation().x + xOffset, entity.getLocation().y + yOffset);
		def.fixedRotation = true;
		body = level.getWorld().createBody(def);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2f, height / 2f);
		
		PhysicsCategory category = PhysicsCategory.ENTITY_COLLIDER;
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 1.0f;
		fdef.filter.categoryBits = category.getCBits();
		fdef.filter.maskBits = category.getMBits();
		
		body.createFixture(fdef).setUserData(this);
		shape.dispose();
		
		this.body = body;
	}
	
	public Body getBody() {
		return body;
	}
	
	public Entity getEntity() {
		return entity;
	}
}
