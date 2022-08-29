package dev.eternalformula.arcontria.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.RayHandler;
import dev.eternalformula.arcontria.entity.ambient.Lamppost;
import dev.eternalformula.arcontria.entity.ambient.Torch;
import dev.eternalformula.arcontria.physics.PhysicsConstants;

public class MapEntityBuilder {
	
	private World world;
	private RayHandler rayHandler;
	
	public MapEntityBuilder(World world, RayHandler rayHandler) {
		this.world = world;
		this.rayHandler = rayHandler;
	}
	
	/**
	 * Creates a new map entity from the given data.
	 * @param level An instance of the GameLevel.
	 * @param entityId The id of the entity.
	 * @param region The TextureRegion of the entity.
	 * @param x The x location of the entity.
	 * @param y The y location of the entity.
	 * @param width The width (in world units) of the entity.
	 * @param height The height (in world units) of the entity.
	 * @param props The MapProperties of the entity.
	 * @return A new entity created from the given data.
	 */
	
	public Entity createEntity(int entityId, TextureRegion region,
			float x, float y, float width, float height, MapProperties props) {
		
		Entity entity = null;
		
		if (entityId == 0) {
			// Lamppost
			entity = new Lamppost(world, rayHandler, region, x, y, entityId);
		}
		
		if (entityId == 1) {
			// Torch
			entity = createAnimatedEntity(entityId, 
					new Vector2(x, y), width, height, props);
		}
		
		if (entity != null) {
			float colliderOffsetX = props.get("colliderOffsetX", 0f, float.class);
			float colliderOffsetY = props.get("colliderOffsetY", 0f, float.class);
			float colliderWidth = props.get("colliderWidth", 0f, float.class);
			float colliderHeight = props.get("colliderHeight", 0f, float.class);
			
			/*entity.setCollider(createCustomColliderBody(level.getWorld(), x, y,
					colliderWidth, colliderHeight,
					colliderOffsetX, colliderOffsetY));
					*/
		}
		
		return entity;
		
	}
	
	private Entity createAnimatedEntity(int entityId,
			Vector2 location, float width, float height, MapProperties props) {
		
		
		if (entityId == 1) {
			// Torch
			String torchId = props.get("torchId", String.class);
			Torch t = new Torch(world, rayHandler, location, torchId);
			
			return t;
			
		}
		return null;
	}
	
	private static Body createCustomColliderBody(World world, float x, float y,
			float colliderWidth, float colliderHeight,
			float colliderOffsetX, float colliderOffsetY) {
		
		Body body;
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(new Vector2(x + colliderOffsetX, y + colliderOffsetY));
		def.fixedRotation = true;
		body = world.createBody(def);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(colliderWidth / 2f, colliderHeight / 2f);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 1.0f;
		
		// It is assumed that these colliders are all mapobject colliders.
		fdef.filter.categoryBits = PhysicsConstants.BIT_MAPOBJECT_COLLIDER; 
		body.createFixture(fdef);
		
		shape.dispose();
		return body;
	}

}
