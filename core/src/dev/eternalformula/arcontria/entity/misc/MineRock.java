package dev.eternalformula.arcontria.entity.misc;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFConstants;

public class MineRock extends Entity {

	public static final int STONE = 0;
	public static final int ORE_1 = 1;
	public static final int ORE_2 = 2;
	
	private Animation<TextureRegion> animation;
	private float elapsedTime;
	
	public MineRock(Vector2 location, int rockType) {
		super();
		setLocation(location);
		
		TextureAtlas genMapScenery = Assets.get("textures/maps/scenery/gen_map_scenery.atlas",
				TextureAtlas.class);
		
		Array<TextureRegion> frames = new Array<TextureRegion>();
		TextureRegion rockReg = null;
		if (rockType == 0) {
			rockReg = genMapScenery.findRegion("stone");
		}
		else if (rockType == 1) {
			rockReg = genMapScenery.findRegion("stone-ore1");
		}
		else if (rockType == 2) {
			rockReg = genMapScenery.findRegion("stone-ore2");
		}
		
		if (frames.size == 0) {
			// One frame
			animation = new Animation<TextureRegion>(1f, rockReg);
		}
		
		currentAnimation = animation;
		width = 1f;
		height = 1f;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (currentAnimation.isAnimationFinished(elapsedTime)) {
			elapsedTime = 0f;
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float delta) {
		TextureRegion reg = currentAnimation.getKeyFrame(elapsedTime, true);
		float w = reg.getRegionWidth() / EFConstants.PPM;
		float h = reg.getRegionHeight() / EFConstants.PPM;
		
		batch.draw(reg, location.x, location.y, w, h);
		elapsedTime += delta;
	}
	
	
	/**
	 * Breaks the rock and yields ore.
	 */
	
	public void destroy() {
		// TODO: Code this lol
	}

}
