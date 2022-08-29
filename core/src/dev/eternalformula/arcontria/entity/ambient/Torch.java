package dev.eternalformula.arcontria.entity.ambient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFConstants;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.EFUtil;
import dev.eternalformula.arcontria.util.Strings;

public class Torch extends Entity {
	
	private Animation<TextureRegion> anim;
	private float elapsedTime;
	
	private Color lightColor;
	private Body lightBody;
	
	private PointLight light;
	private ParticleEffect pEffect;
	
	private float distanceResetTimer;
	private float distanceTime;
	
	private static final float DEFAULT_DISTANCE = 5f;
	
	public Torch(World world, RayHandler rayHandler, Vector2 location, 
			String torchId) {
		super();

		System.out.println("new torch");
		this.location = location;
		TextureAtlas atlas = Assets.get("textures/maps/scenery/gen_map_scenery.atlas", TextureAtlas.class);
		
		if (torchId.equalsIgnoreCase("mineTorchDefault")) {
			
			// Creates animation.
			TextureRegion region = atlas.findRegion("torch");
			
			Array<TextureRegion> frames = new Array<TextureRegion>();
			
			// Note: Will need conditional clauses for non (16x32) size torched.
			int frameCount = region.getRegionWidth() / (int) EFConstants.PPM;
			for (int i = 0; i < frameCount; i++) {
				frames.add(new TextureRegion(region, 16 * i, 0, 16, 32));
			}
			anim = new Animation<TextureRegion>(0.125f, frames);
			frames.clear();
			
			lightColor = EFUtil.getColorFromRGB(204f, 112f, 27f);
			this.width = 1f;
			this.height = 2f;

			// Light
			this.lightBody = createLightBody(world);
			lightBody.setActive(false);
			light = new PointLight(rayHandler, 20, lightColor, DEFAULT_DISTANCE, 0, 0);
			light.setSoftnessLength(1f);
			light.setXray(true);
			light.attachToBody(lightBody);
			distanceResetTimer = MathUtils.random(0.5f);
			// Particles
			pEffect = new ParticleEffect();
			pEffect.load(Gdx.files.internal("data/particles/smoke/smoke.particle"), 
					Gdx.files.internal("data/particles/smoke"));
			pEffect.setPosition(lightBody.getPosition().x, lightBody.getPosition().y);
			pEffect.start();
			
		}
		else {
			EFDebug.warn("Could not find torch with id \"" + torchId + "\". Returning null.");
			return;
		}
	}
	
	private Body createLightBody(World world) {
		Body body;
		BodyDef bdef = new BodyDef();
		bdef.position.set(location.x + 0.5f, location.y + 1.625f);
		bdef.type = BodyType.StaticBody;
		bdef.fixedRotation = true;
		body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		// Collider? idk
		shape.setAsBox(0.125f, 0.125f);
		body.createFixture(shape, 1f);
		shape.dispose();
		return body;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		pEffect.update(delta);
		if (pEffect.isComplete()) {
			pEffect.reset();
		}
		
		distanceTime += delta;
		if (distanceTime >= distanceResetTimer) {
			light.setDistance(MathUtils.random(DEFAULT_DISTANCE - 0.5f, DEFAULT_DISTANCE + 0.5f));
			distanceTime = 0f;
			distanceResetTimer = MathUtils.random(0.5f);
		}
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
		elapsedTime += delta;
		
		TextureRegion currentFrame = anim.getKeyFrame(elapsedTime, true);
		float w = currentFrame.getRegionWidth() / EFConstants.PPM;
		float h = currentFrame.getRegionHeight() / EFConstants.PPM;
		
		batch.draw(currentFrame, location.x, location.y, w, h);
		
		pEffect.draw(batch);
	}

}
