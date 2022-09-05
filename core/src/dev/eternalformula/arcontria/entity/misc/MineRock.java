package dev.eternalformula.arcontria.entity.misc;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.boxes.Box;
import dev.eternalformula.arcontria.physics.boxes.BreakableObjHitbox;
import dev.eternalformula.arcontria.physics.boxes.EntityColliderBox;
import dev.eternalformula.arcontria.scenes.GameScene;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFConstants;

public class MineRock extends Entity {

	public static final int STONE = 0;
	public static final int ORE_1 = 1;
	public static final int ORE_2 = 2;
	
	private Animation<TextureRegion> animation;
	private Animation<TextureRegion> breakAnim; 
	private float elapsedTime;
	
	private Box colliderBox;
	private Box rockHitbox;
	
	private boolean isBeingDestroyed;
	
	public MineRock(World world, Vector2 location, int rockType) {
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
		frames.clear();
		
		TextureRegion breakReg = genMapScenery.findRegion("stone-break-anim");
		for (int i = 0; i < 5; i++) {
			frames.add(new TextureRegion(breakReg, i * 16, 0, 16, 16));
		}
		breakAnim = new Animation<TextureRegion>(0.15f, frames);
		breakAnim.setPlayMode(PlayMode.NORMAL);
		frames.clear();
		
		width = 1f;
		height = 1f;
		
		// Box
		this.colliderBox = new EntityColliderBox(world, this, BodyType.StaticBody, true);
		this.rockHitbox = new BreakableObjHitbox(world, this);
		
		currentAnimation = animation;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (currentAnimation.isAnimationFinished(elapsedTime)) {
			if (currentAnimation.equals(breakAnim)) {
				GameLevel level = ((GameScene) ArcontriaGame.getCurrentScene()).getLevel();
				level.removeEntity(this);
			}
			elapsedTime = 0f;
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float delta) {
		TextureRegion reg = currentAnimation.getKeyFrame(elapsedTime);
		float w = reg.getRegionWidth() / EFConstants.PPM;
		float h = reg.getRegionHeight() / EFConstants.PPM;
		
		batch.draw(reg, location.x, location.y, w, h);
		elapsedTime += delta;
	}
	
	
	/**
	 * Breaks the rock and yields ore.
	 */
	
	public void destroy() {
		if (!isBeingDestroyed) {
			isBeingDestroyed = true;
			elapsedTime = 0f;
			currentAnimation = breakAnim;
			
			Sound rockBreak = Assets.get("sfx/mines/rockbreak.wav", Sound.class);
			rockBreak.play(0.21f);
		}
	}
	
	@Override
	public void destroyBodies(World world) {
		world.destroyBody(colliderBox.getBody());
		world.destroyBody(rockHitbox.getBody());
	}

}
