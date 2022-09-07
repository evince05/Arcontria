package dev.eternalformula.arcontria.entity.projectiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.PointLight;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.boxes.ProjectileBox;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFUtil;

/**
 * Deconstructor Projectile
 * @author EternalFormula
 */

public class DeconProjectile extends BasicProjectile {

	private Body lightBody;
	private PointLight light;
	
	private boolean isLightRemoved;
	
	public DeconProjectile(GameLevel level, Vector2 location) {
		super(level);
		
		this.location = location;
		
		this.texture = Assets.get("textures/entities/projectiles/projectiles.atlas",
				TextureAtlas.class).findRegion("deconstructor_orb");
		
		this.width = 1/4f;
		this.height = 1/4f;
		
		this.projBox = new ProjectileBox(level, this);
		
		Color lightColor = EFUtil.getColorFromRGBA(57, 106, 125, 140);
		
		// Creates the light
		this.lightBody = createLightBody(level.getWorld());
		lightBody.setActive(false);
		light = new PointLight(level.getRayHandler(), 20, lightColor, 1f, 0, 0);
		light.setSoftnessLength(1f);
		light.setXray(true);
		light.attachToBody(lightBody);
		
	}
	
	private Body createLightBody(World world) {
		Body body;
		BodyDef bdef = new BodyDef();
		bdef.position.set(location.x + width / 2f, location.y + height / 2f);
		bdef.type = BodyType.StaticBody;
		bdef.fixedRotation = true;
		body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		// Collider? idk
		shape.setAsBox(1f, 1f);
		body.createFixture(shape, 1f);
		shape.dispose();
		return body;
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
		super.draw(batch, delta);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		lightBody.setTransform(location.x + width / 2f, location.y + height / 2f, 0f);
		
		
	}

	@Override
	public void destroyBodies(World world) {
		super.destroyBodies(world);
		
		if (!isLightRemoved) {
			world.destroyBody(lightBody);
			light.remove();
			isLightRemoved = true;
		}
		
		
		
	}

	
}
