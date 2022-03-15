package dev.eternalformula.arcontria.entity.ambient;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import box2dLight.PointLight;
import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.gfx.lighting.DaylightHandler;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.PhysicsConstants;
import dev.eternalformula.arcontria.util.EFConstants;

public class Lamppost extends Entity {
	
	public static final int FOREST_LAMPPOST = 0;
	private TextureRegion texRegion;
	private final int lamppostType;
	
	private float brightness;
	private Color lightColor;
	
	private Body lightBody;
	private PointLight light;
	
	public Lamppost(GameLevel level, TextureRegion region, float x, float y, int type) {
		super(level);
		
		this.lamppostType = type;
		this.texRegion = region;
		this.location = new Vector2(x, y);
		this.lightColor = new Color(169 / 255f, 119 / 255f, 50 / 255f, 1f);
		
		this.lightBody = createLightBody();
		lightBody.setActive(false);
		light = new PointLight(level.getRayHandler(), 150, lightColor, 8, 0, 0);
		light.setSoftnessLength(0f);
		light.setXray(true);
		light.attachToBody(lightBody);
	}
	
	private Body createLightBody() {
		Body body;
		BodyDef bdef = new BodyDef();
		bdef.position.set(location.x + 0.5f, location.y + 1.625f);
		bdef.type = BodyType.StaticBody;
		bdef.fixedRotation = true;
		body = level.getWorld().createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		
		if (lamppostType == 0) {
			shape.setAsBox(0.125f, 0.125f);//, 
					//new Vector2(location.x + 0.5f, location.y), 0f);
		}
		
		body.createFixture(shape, 1.0f);
		shape.dispose();
		return body;
	}
	
	/**
	 * Handles updating for the brightness of the light.
	 */
	
	@Override
	public void update(float delta) {
		DaylightHandler dh = level.getDaylightHandler();
		if (dh.isDay()) {
			if (light.isActive()) {
				light.setActive(false);
			}
			return;
		}
		else {
			light.setActive(true);
		}
		
		if ((dh.getWorldTime() >= DaylightHandler.SUNSET_START_TIME &&
				dh.getWorldTime() <= DaylightHandler.SUNSET_END_TIME)) {
			
			// calculate light
			float sunsetTimeLength = DaylightHandler.SUNSET_END_TIME - 
					DaylightHandler.SUNSET_START_TIME;
			float percentageElapsed = (dh.getWorldTime() - DaylightHandler.SUNSET_START_TIME) / sunsetTimeLength;
			
			lightColor.a = percentageElapsed;
			light.setColor(lightColor);
		}
		else if ((dh.getWorldTime() >= DaylightHandler.SUNRISE_START_TIME &&
				dh.getWorldTime() <= DaylightHandler.SUNRISE_END_TIME)) {
			
			// Must be in sunrise
			float sunriseTimeLength = DaylightHandler.SUNRISE_END_TIME -
					DaylightHandler.SUNRISE_START_TIME;
			float percentageElapsed = (dh.getWorldTime() - DaylightHandler.SUNRISE_START_TIME) / sunriseTimeLength;
			lightColor.a = 1.0f - percentageElapsed;
			light.setColor(lightColor);
		}
		return;
	}


	@Override
	public void draw(SpriteBatch batch, float delta) {
		float width = texRegion.getRegionWidth() / EFConstants.PPM;
		float height = texRegion.getRegionHeight() / EFConstants.PPM;
		batch.draw(texRegion, location.x, location.y, width, height);
	}
	
	public Color getLightColor() {
		return lightColor;
	}

}
