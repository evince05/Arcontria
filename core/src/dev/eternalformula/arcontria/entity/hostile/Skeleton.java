package dev.eternalformula.arcontria.entity.hostile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.entity.LivingEntity;

public class Skeleton extends LivingEntity {

	private Animation<TextureRegion> walkUp;
	private Animation<TextureRegion> walkLeft;
	private Animation<TextureRegion> walkRight;
	private Animation<TextureRegion> walkDown;
	
	private Animation<TextureRegion> idleUp;
	private Animation<TextureRegion> idleLeft;
	private Animation<TextureRegion> idleRight;
	private Animation<TextureRegion> idleDown;
	
	private static final float BASE_SPEED = 0.8f;
	private TextureAtlas atlas;
	
	public Skeleton() {
		this.location = new Vector2(10f, 10f);
		this.speed = BASE_SPEED;
		
		this.health = 25f;
		this.maxHealth = 25f;
		
		init();
	}
	
	private void init() {
		this.atlas = new TextureAtlas(Gdx.files.internal
				("textures/entities/skeleton/base_skeleton/base_skeleton.atlas"));
		
		Array<TextureRegion> frames = new Array<>();
		TextureRegion region = atlas.findRegion("down");
		
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		walkDown = new Animation<TextureRegion>(0.125f, frames);
		idleDown = new Animation<TextureRegion>(1f, frames.get(0));
		frames.clear();
		
		region = atlas.findRegion("up");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		walkUp = new Animation<TextureRegion>(0.125f, frames);
		idleUp = new Animation<TextureRegion>(1f, frames.get(0));
		frames.clear();
		frames.clear();
		currentAnimation = idleDown;
	}
	
	@Override
	public void moveLeft(float delta) {
	}

	@Override
	public void moveRight(float delta) {
	}

	@Override
	public void moveUp(float delta) {
	}

	@Override
	public void moveDown(float delta) {
	}
	
	public void dispose() {
		atlas.dispose();
	}

}
