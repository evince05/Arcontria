package dev.eternalformula.arcontria.entity.player;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.entity.LivingEntity;
import dev.eternalformula.arcontria.input.Controllable;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.B2DUtil;
import dev.eternalformula.arcontria.util.EFConstants;

public class Player extends LivingEntity implements Controllable {
	
	private TextureAtlas atlas;
	
	private Animation<TextureRegion> walkUp;
	private Animation<TextureRegion> walkDown;
	private Animation<TextureRegion> walkLeft;
	private Animation<TextureRegion> walkRight;

	private Animation<TextureRegion> idleUp; // to conserve RAM, perhaps replace with using walkUp's first frame only?
	private Animation<TextureRegion> idleDown; // ^
	private Animation<TextureRegion> idleLeft; // ^
	private Animation<TextureRegion> idleRight; // ^
	
	private Animation<TextureRegion> attackingUp;
	private Animation<TextureRegion> attackingDown;
	private Animation<TextureRegion> attackingLeft;
	private Animation<TextureRegion> attackingRight;
	
	private Animation<TextureRegion> weaponAnimationUp;
	private Animation<TextureRegion> weaponAnimationDown;
	private Animation<TextureRegion> weaponAnimationLeft;
	private Animation<TextureRegion> weaponAnimationRight;
	
	private Animation<TextureRegion> weaponAnimation;
	
	private int direction; // 1 = up; 2 = left; 3 = right; 4 = down;

	private String name;
	private UUID uuid;
	
	private boolean isMoving;
	private boolean isAttacking;
	
	private float attackingTime;
	
	private Sound sound;
	private Sound meleeSound;
	
	private float soundTimer; // footstep noise should be every 1/8 of a second.
	
	private static final float BASE_SPEED = 2f;
	
	/**
	 * Creates a basic player object.
	 * @param name The name of the player.
	 * @param uuid The UUID of the player.
	 * @param 
	 */
	
	private Player(GameLevel level, String name, UUID uuid) {
		super(level);
		this.name = name;
		this.uuid = uuid;
		this.speed = BASE_SPEED;
		
		this.location = new Vector2(0f, 0f);
		this.sound = Gdx.audio.newSound(Gdx.files.internal("sfx/footsteps/grass2.ogg"));
		this.meleeSound = Gdx.audio.newSound(Gdx.files.internal("sfx/weapons/swing-air-woosh.wav"));
		
		this.width = 1f;
		this.height = 2f;
		
		init();
	}
	
	public static Player create(GameLevel level, String name, UUID uuid) {
		return new Player(level, name, uuid);
	}
	
	private void init() {
		this.atlas = new TextureAtlas(Gdx.files.internal("textures/entities/player/player.atlas"));
		
		Array<TextureRegion> frames = new Array<>();
		TextureRegion region = atlas.findRegion("up");
		
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		walkUp = new Animation<TextureRegion>(0.125f, frames);
		idleUp = new Animation<TextureRegion>(1f, frames.get(0));
 
		frames.clear();
		
		region = atlas.findRegion("down");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		walkDown = new Animation<TextureRegion>(0.125f, frames);
		idleDown = new Animation<TextureRegion>(1f, frames.get(0));
		frames.clear();
		
		region = atlas.findRegion("left");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		walkLeft = new Animation<TextureRegion>(0.125f, frames);
		idleLeft = new Animation<TextureRegion>(1f, frames.get(0));
		frames.clear();
		
		region = atlas.findRegion("right");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		walkRight = new Animation<TextureRegion>(0.125f, frames);
		idleRight = new Animation<TextureRegion>(1f, frames.get(0));
		frames.clear();
		
		region = atlas.findRegion("melee_down");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		attackingDown = new Animation<TextureRegion>(0.0625f, frames);
		frames.clear();
		
		region = atlas.findRegion("melee_up");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		attackingUp = new Animation<TextureRegion>(0.0625f, frames);
		frames.clear();

		region = atlas.findRegion("melee_left");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		attackingLeft = new Animation<TextureRegion>(0.0625f, frames);
		frames.clear();
		
		region = atlas.findRegion("melee_right");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		attackingRight = new Animation<TextureRegion>(0.0625f, frames);
		frames.clear();
		
		// Weapon
		atlas = new TextureAtlas(Gdx.files.internal("textures/animations/player_weapons.atlas"));
		
		region = atlas.findRegion("rusty_sword_down");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 32, 0, 32, 32));
		}
		weaponAnimationDown = new Animation<TextureRegion>(0.0625f, frames);
		frames.clear();
		
		region = atlas.findRegion("rusty_sword_up");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 34, 0, 34, 34));
		}
		weaponAnimationUp = new Animation<TextureRegion>(0.0625f, frames);
		frames.clear();
		
		region = atlas.findRegion("rusty_sword_left");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 36, 0, 36, 32));
		}
		weaponAnimationLeft = new Animation<TextureRegion>(0.0625f, frames);
		frames.clear();
		
		region = atlas.findRegion("rusty_sword_right");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 36, 0, 36, 32));
		}
		weaponAnimationRight = new Animation<TextureRegion>(0.0625f, frames);
		frames.clear();
		
		this.currentAnimation = idleDown;
		this.direction = 4;
		this.isMoving = false;
		
		// Physics stuff :)
		this.body = B2DUtil.createBodyForEntity(level.getWorld(), this, BodyType.DynamicBody);
	}
	
	@Override
	public void moveLeft(float delta) {
		this.location.x -= speed * delta;
		isMoving = true;
	}

	@Override
	public void moveRight(float delta) {
		this.location.x += speed * delta;
		isMoving = true;
	}

	@Override
	public void moveUp(float delta) {
		this.location.y += speed * delta;
		isMoving = true;
	}

	@Override
	public void moveDown(float delta) {
		this.location.y -= speed * delta;
		isMoving = true;
	}
	
	public void move(float delta, float horizontalVelocity, float verticalVelocity) {
		body.setLinearVelocity(horizontalVelocity, verticalVelocity);
		isMoving = true;
		
		this.location.x += horizontalVelocity * delta;
		this.location.y += verticalVelocity * delta;
	}

	@Override
	public void handleInput(float delta) {
		isMoving = false;
		
		if (isAttacking) {
			attackingTime += delta;
			if (currentAnimation.isAnimationFinished(attackingTime)) {
				isAttacking = false;
				attackingTime = 0f;
			}
		}
		
		float horizontalForce = 0;
		float verticalForce = 0;
		
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			direction = 1;
			verticalForce = speed;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			direction = 4;
			verticalForce = -speed;
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			direction = 2;
			horizontalForce = -speed;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			direction = 3;
			horizontalForce = speed;
		}
		
		if (Math.abs(horizontalForce) > 0 || Math.abs(verticalForce) > 0) {
			move(delta, horizontalForce, verticalForce);
		}
		else {
			body.setLinearVelocity(0f, 0f);
		}
		
		
		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			isAttacking = true;
			setWeaponAnimation();
			meleeSound.play(0.3f);
			
			//ArcontriaGame.GAME.getScene().getLevel().spawnParticle(new DamageTextParticle(location, 10));
		}
		
		if (isMoving) {
			soundTimer += delta;
			if (soundTimer >= 0.28f) {
				soundTimer -= 0.28f;
				sound.play(0.15f);
			}
		}
		
		
		currentAnimation = getAnimation();
		//System.out.println("is currentanim null? " + currentAnimation == null);
	}
	
	/**
	 * Gets the applicable animation.
	 */
	
	private Animation<TextureRegion> getAnimation() {
		
		if (isAttacking) {
			switch (direction) {
			case 1:
				return attackingUp;
			case 2:
				return attackingLeft;
			case 3:
				return attackingRight;
			case 4:
				return attackingDown;
			}
		}
		else {
			if (isMoving) {
				switch (direction) {
				case 1:
					return walkUp;
				case 2:
					return walkLeft;
				case 3:
					return walkRight;
				case 4:
					return walkDown;
				}
			}
			else {
				switch (direction) {
				case 1:
					return idleUp;
				case 2:
					return idleLeft;
				case 3:
					return idleRight;
				case 4:
					return idleDown;
				}
			}
		}
		
		return idleDown;
	}
	
	private void setWeaponAnimation() {
		if (direction == 1) {
			weaponAnimation = weaponAnimationUp;
		}
		else if (direction == 2) {
			weaponAnimation = weaponAnimationLeft;
		}
		else if (direction == 3) {
			weaponAnimation = weaponAnimationRight;
		}
		else if (direction == 4) {
			weaponAnimation = weaponAnimationDown;
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float delta) {
		super.draw(batch, delta);
	
		if (isAttacking) {
			TextureRegion texRegion = weaponAnimation.getKeyFrame(attackingTime, true);
			float width = texRegion.getRegionWidth() / EFConstants.PPM;
			float height = texRegion.getRegionHeight() / EFConstants.PPM;
			float xOffset = (texRegion.getRegionWidth() / (EFConstants.PPM * 2f)) - 0.5f; // the 0.5f is half of the player's width
			batch.draw(texRegion, location.x - xOffset, location.y, width, height);
		}
		
	}
	
	public void dispose() {
		sound.dispose();
		atlas.dispose();
	}
}
