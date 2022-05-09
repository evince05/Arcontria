package dev.eternalformula.arcontria.entity.hostile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.B2DUtil;
import dev.eternalformula.arcontria.physics.PhysicsConstants.PhysicsCategory;
import dev.eternalformula.arcontria.physics.boxes.EntityAttackBox;
import dev.eternalformula.arcontria.physics.boxes.EntityColliderBox;
import dev.eternalformula.arcontria.physics.boxes.EntityHitbox;
import dev.eternalformula.arcontria.util.EFDebug;

public class Skeleton extends HostileEntity {

	private Animation<TextureRegion> walkUp;
	private Animation<TextureRegion> walkLeft;
	private Animation<TextureRegion> walkRight;
	private Animation<TextureRegion> walkDown;
	
	private Animation<TextureRegion> idleUp;
	private Animation<TextureRegion> idleLeft;
	private Animation<TextureRegion> idleRight;
	private Animation<TextureRegion> idleDown;
	
	private static final float BASE_SPEED = 1.8f;
	private TextureAtlas atlas;
	
	
	
	public Skeleton(GameLevel level) {
		super(level, level.getPlayer(), new Vector2(10f, 7f));
		this.speed = BASE_SPEED;
		
		this.health = 25f;
		this.maxHealth = 25f;
		
		this.width = 1f;
		this.height = 2f;
		
		//this.body = B2DUtil.createEntityCollider(level.getWorld(), this, BodyType.DynamicBody, PhysicsCategory.ENTITY_COLLIDER);
		
		EFDebug.debug("Reminder: Hitboxes now control body movement.");
		init();
	}
	
	private void init() {
		this.atlas = new TextureAtlas(Gdx.files.internal
				("textures/entities/skeleton/base_skeleton/base_skeleton.atlas"));
		
		Array<TextureRegion> frames = new Array<>();
		TextureRegion region = atlas.findRegion("walkdown");
		
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		walkDown = new Animation<TextureRegion>(0.125f, frames);
		idleDown = new Animation<TextureRegion>(1f, frames.get(0));
		frames.clear();
		
		region = atlas.findRegion("walkup");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		walkUp = new Animation<TextureRegion>(0.125f, frames);
		idleUp = new Animation<TextureRegion>(1f, frames.get(0));
		frames.clear();
		
		region = atlas.findRegion("walkleft");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		walkLeft = new Animation<TextureRegion>(0.125f, frames);
		idleLeft = new Animation<TextureRegion>(1f, frames.get(0));
		frames.clear();
		
		region = atlas.findRegion("walkright");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		walkRight = new Animation<TextureRegion>(0.125f, frames);
		idleRight = new Animation<TextureRegion>(1f, frames.get(0));
		frames.clear();
		currentAnimation = walkRight;
		
		direction = 3;
		
		this.hitbox = new EntityHitbox(level, this);
		this.colliderBox = new EntityColliderBox(level, this);
	}
	
	public void dispose() {
		atlas.dispose();
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		// Animation Control
		if (isMoving) {
			if (direction == 1) {
				currentAnimation = walkUp;
			}
			else if (direction == 2) {
				currentAnimation = walkLeft;
			}
			else if (direction == 3) {
				currentAnimation = walkRight;
			}
			else {
				currentAnimation = walkDown;
			}
		}
		
		if (hitbox != null) {
			this.location.x = hitbox.getBody().getPosition().x - width / 2f;
			this.location.y = hitbox.getBody().getPosition().y - height / 2f;
		}
	}

}
