package dev.eternalformula.arcontria.entity.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.combat.DamageSource;
import dev.eternalformula.arcontria.entity.LivingEntity;
import dev.eternalformula.arcontria.gfx.particles.DamageTextParticle;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.boxes.EntityColliderBox;
import dev.eternalformula.arcontria.physics.boxes.EntityHitbox;
import dev.eternalformula.arcontria.util.EFConstants;

/**
 * The Dummy is a non-hostile entity found in the Dojo. It allows the player to test
 * different weapons against it, showing them the damage output as a result.
 * 
 * @author EternalFormula
 */

public class Dummy extends LivingEntity {

	private Animation<TextureRegion> hitLeft;
	private Animation<TextureRegion> hitRight;
	private Animation<TextureRegion> idle;
	
	private boolean isHit;
	private int hitTintTimer;
	
	private float hurtAnimationTimer;
	
	public Dummy(GameLevel level) {
		super(level);
		
		this.width = 1f;
		this.height = 2f;
		
		this.colliderBox = new EntityColliderBox(level, this, BodyType.StaticBody);
		this.hitbox = new EntityHitbox(level, this);
		
		initAnimations();
		
	}
	
	private void initAnimations() {
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/animations/dummy.atlas"));
		
		Array<TextureRegion> frames = new Array<TextureRegion>();
		TextureRegion region = atlas.findRegion("hit-left");

		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		hitLeft = new Animation<TextureRegion>(0.125f, frames);
		frames.clear();
		
		// Hit right
		region = atlas.findRegion("hit-right");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		hitRight = new Animation<TextureRegion>(0.125f, frames);
		idle = new Animation<TextureRegion>(1f, frames.get(0));
		frames.clear();
		
		//atlas.dispose();
		
		currentAnimation = idle;
	}
	
	@Override
	public void update(float delta) {
		
		if (isHit) {
			hitTintTimer++;
			if (hitTintTimer >= 6) {
				isHit = false;
				hitTintTimer = 0;
			}
		}
		
		if (currentAnimation.equals(hitLeft) || currentAnimation.equals(hitRight)) {
			hurtAnimationTimer += delta;
			
			if (currentAnimation.isAnimationFinished(hurtAnimationTimer)) {
				currentAnimation = idle;
				hurtAnimationTimer = 0f;
			}
		}
	}
	
	@Override
	public void damage(DamageSource source, float amount, boolean isCritStrike) {
		
		// No damage will occur to the dummy. Instead, a damage text particle is spawned.
		if (isCritStrike) {
			amount += 2;
		}
		
		//level.getParticleHandler().spawnParticle(new DamageTextParticle(location, amount, isCritStrike));
		isHit = true;
		
		Vector2 sourceLoc = source.getEntity().getLocation();
		if (sourceLoc.x > location.x) {
			currentAnimation = hitLeft;
		}
		else {
			currentAnimation = hitRight;
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float delta) {
		elapsedTime += Gdx.graphics.getDeltaTime();
		TextureRegion texRegion = currentAnimation.getKeyFrame(elapsedTime, true);
		float w = texRegion.getRegionWidth() / EFConstants.PPM;
		float h = texRegion.getRegionHeight() / EFConstants.PPM;
		
		if (isHit) {
			batch.setColor(EFConstants.ENTITY_HURT_COLOR);
		}
		batch.draw(texRegion, location.x, location.y, w, h);
		batch.setColor(Color.WHITE);
	}

}
