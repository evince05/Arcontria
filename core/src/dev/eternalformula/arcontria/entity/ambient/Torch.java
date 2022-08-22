package dev.eternalformula.arcontria.entity.ambient;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import box2dLight.PointLight;
import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFConstants;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.EFUtil;

public class Torch extends Entity {

	private String torchId;
	
	private Animation<TextureRegion> anim;
	private float elapsedTime;
	
	private Color lightColor;
	private Body lightBody;
	private PointLight light;
	
	public Torch(String torchId) {
		super();
		
		this.torchId = torchId;
		
		TextureAtlas atlas = Assets.get("textures/maps/scenery/gen_map_scenery.atlas", TextureAtlas.class);
		
		if (atlas.findRegion(torchId) != null) {
			
			// Creates animation.
			TextureRegion region = atlas.findRegion(torchId);
			
			Array<TextureRegion> frames = new Array<TextureRegion>();
			
			// Note: Will need conditional clauses for non (16x32) size torched.
			int frameCount = region.getRegionWidth() / (int) EFConstants.PPM;
			for (int i = 0; i < frameCount; i++) {
				frames.add(new TextureRegion(region, frameCount * i, 0, 16, 32));
			}
			anim = new Animation<TextureRegion>(0.125f, frames);
			frames.clear();
			
			if (torchId.equalsIgnoreCase("mineTorchDefault")) {
				lightColor = EFUtil.getColorFromRGB(204f, 112f, 27f);
				this.width = 1f;
				this.height = 2f;
			}

			this.lightBody = createLightBody();
			lightBody.setActive(false);
			light = new PointLight(level.getRayHandler(), 150, lightColor, 8, 0, 0);
			light.setSoftnessLength(0f);
			light.setXray(true);
			light.attachToBody(lightBody);
		}
		else {
			EFDebug.warn("Could not find torch with id \"" + torchId + "\". Returning null.");
			return;
		}
	}
	
	private Body createLightBody() {
		Body body;
		BodyDef bdef = new BodyDef();
		bdef.position.set(location.x + 0.5f, location.y + 1.625f);
		bdef.type = BodyType.StaticBody;
		bdef.fixedRotation = true;
		body = level.getWorld().createBody(bdef);
		
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
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
		elapsedTime += delta;
		
		TextureRegion currentFrame = anim.getKeyFrame(elapsedTime, true);
		float w = currentFrame.getRegionWidth() / EFConstants.PPM;
		float h = currentFrame.getRegionHeight() / EFConstants.PPM;
		
		batch.draw(currentFrame, location.x, location.y, w, h);
	}

}
