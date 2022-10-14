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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.entity.LivingEntity;
import dev.eternalformula.arcontria.entity.projectiles.DeconProjectile;
import dev.eternalformula.arcontria.input.Controllable;
import dev.eternalformula.arcontria.inventory.PlayerInventory;
import dev.eternalformula.arcontria.items.Item;
import dev.eternalformula.arcontria.items.Material;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.boxes.PlayerAttackBox;
import dev.eternalformula.arcontria.physics.boxes.PlayerColliderBox;
import dev.eternalformula.arcontria.physics.boxes.PlayerHitbox;
import dev.eternalformula.arcontria.player.PlayerData;
import dev.eternalformula.arcontria.util.EFConstants;

/**
 * The Player Object.<br>
 * Some Notes:<br>
 * <t><b>DO NOT</b> add the player to the GameLevel entities array. 
 */
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
	
	private Animation<TextureRegion> deconUp;
	private Animation<TextureRegion> deconLeft;
	private Animation<TextureRegion> deconRight;
	private Animation<TextureRegion> deconDown;

	private String name;
	private UUID uuid;
	
	private boolean isAttacking;
	private boolean isMining;
	
	private float attackingTime;
	private float miningTime;
	
	private Sound sound;
	private Sound meleeSound;
	
	private float soundTimer; // footstep noise should be every 1/8 of a second.
	
	private PlayerAttackBox attackBox;
	
	private PlayerData playerData;
	private PlayerInventory inventory;
	
	/*
	 * Determines if the player is currently behind any MapObject
	 * (controls rendering order).
	 */
	private boolean isBehindMapObject;
	
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
		this.sound = Gdx.audio.newSound(Gdx.files.internal("sfx/footsteps/wood.ogg"));
		this.meleeSound = Gdx.audio.newSound(Gdx.files.internal("sfx/weapons/swing-air-woosh.wav"));
		
		this.width = 1f;
		this.height = 2f;
		
		this.isBehindMapObject = false;
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
		
		/*
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
		*/
		region = atlas.findRegion("deconstructor-up");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		deconUp = new Animation<TextureRegion>(0.0625f, frames);
		frames.clear();
		
		region = atlas.findRegion("deconstructor-down");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		deconDown = new Animation<TextureRegion>(0.0625f, frames);
		frames.clear();
		
		region = atlas.findRegion("deconstructor-left");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 32, 0, 32, 32));
		}
		deconLeft = new Animation<TextureRegion>(0.0625f, frames);
		frames.clear();
		
		region = atlas.findRegion("deconstructor-right");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 32, 0, 32, 32));
		}
		deconRight = new Animation<TextureRegion>(0.0625f, frames);
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
		//this.body = B2DUtil.createEntityCollider(level.getWorld(), this, BodyType.DynamicBody, PhysicsCategory.PLAYER_COLLIDER);
		
		this.hitbox = new PlayerHitbox(level, this);
		this.colliderBox = new PlayerColliderBox(level, this);
		this.attackBox = new PlayerAttackBox(level, this);
		
		this.inventory = PlayerInventory.createInventoryForPlayer(this);
		
		System.out.println("Item: " + inventory.getItem(0).toDebugString());
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		handleInput(delta);
	}

	@Override
	public void handleInput(float delta) {
		isMoving = false;
		
		if (isAttacking) {
			attackingTime += delta;
			attackBox.getBody().setActive(true);

			Vector2 pos = new Vector2(hitbox.getBody().getPosition());
			if (direction == 1) {
				// up
				pos.y += 0.5;
			}
			else if (direction == 2) {
				// left
				pos.x -= 0.65;
			}
			else if (direction == 3) {
				// right
				pos.x += 0.65;
			}
			else if (direction == 4) {
				// down
				pos.y -= 0.5;
			}

			attackBox.getBody().setTransform(pos, 0);
			
			if (currentAnimation.isAnimationFinished(attackingTime)) {
				isAttacking = false;
				attackingTime = 0f;
				attackBox.getBody().setActive(false);
			}
		}
		else if (isMining) {
			
			miningTime += delta;
			if (currentAnimation.isAnimationFinished(miningTime)) {
				isMining = false;
				miningTime = 0f;
			}
		}
		
		float horizontalForce = 0;
		float verticalForce = 0;
		
		if (!isAttacking && !isMining) {
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
		}
		
		if (Math.abs(horizontalForce) > 0 || Math.abs(verticalForce) > 0) {

			// Note that the distance moved from this method is roughly equal to (speed * Gdx.graphics.getDeltaTime())
			move(horizontalForce, verticalForce);
		}
		else {
			hitbox.setLinearVelocity(0f, 0f);
			colliderBox.setLinearVelocity(0f, 0f);
		}
		
		
		
		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			
			isMining = true;
			spawnDeconstructorOrb();
			/*isAttacking = true;
			setWeaponAnimation();
			meleeSound.play(0.3f);
			*/
		}
		
		if (isMoving) {
			soundTimer += delta;
			if (soundTimer >= 0.26f) {
				soundTimer -= 0.26f;
				sound.play(0.75f);
			}
		}
		currentAnimation = getAnimation();
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
		else if (isMining) {
			switch(direction) {
			case 1:
				return deconUp;
			case 2:
				return deconLeft;
			case 3:
				return deconRight;
			case 4:
				return deconDown;
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
	
	private void spawnDeconstructorOrb() {
		Vector2 loc = null;
		Vector2 vel = null;
		
		if (direction == 1) {
			loc = new Vector2(location.x + width / 2f, location.y + height);
			vel = new Vector2(0f, 7.5f);
		}
		else if (direction == 2) {
			loc = new Vector2(location.x, location.y + height / 2f);
			vel = new Vector2(-7.5f, 0f);
		}
		else if (direction == 3) {
			loc = new Vector2(location.x + width, location.y + height / 2f);
			vel = new Vector2(7.5f, 0f);
		}
		else if (direction == 4) {
			loc = new Vector2(location.x + width / 2f, location.y);
			vel = new Vector2(0f, -7.5f);
		}
		
		DeconProjectile deconProj = new DeconProjectile(level, loc);
		deconProj.applyVelocity(vel);
		level.addEntity(deconProj);
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
	
		if (isAttacking) {
			TextureRegion texRegion = weaponAnimation.getKeyFrame(attackingTime, true);
			float width = texRegion.getRegionWidth() / EFConstants.PPM;
			float height = texRegion.getRegionHeight() / EFConstants.PPM;
			float xOffset = (texRegion.getRegionWidth() / (EFConstants.PPM * 2f)) - 0.5f; // the 0.5f is half of the player's width
			batch.draw(texRegion, location.x - xOffset, location.y, width, height);
		}
		else if (isMining) {
			elapsedTime += delta;
			TextureRegion texRegion = currentAnimation.getKeyFrame(miningTime, true);
			float w = texRegion.getRegionWidth() / EFConstants.PPM;
			float h = texRegion.getRegionHeight() / EFConstants.PPM;
			
			float xOffset = 0;
			if (currentAnimation.equals(deconLeft) || currentAnimation.equals(deconRight)) {
				xOffset = w / 4f;
			}
			batch.draw(texRegion, location.x - xOffset, location.y, w, h);
		}
		else {
			super.draw(batch, delta);
		}
		
	}
	
	public void dispose() {
		sound.dispose();
		atlas.dispose();
	}

	public boolean shouldRenderBeforeEntities() {
		return isBehindMapObject;
	}
	
	public void setBehindMapObject(boolean isBehindMapObject) {
		this.isBehindMapObject = isBehindMapObject;
	}
	
	@Override
	public void destroyBodies(World world) {
	}
	
	public PlayerInventory getInventory() {
		return inventory;
	}
	
	public PlayerData getPlayerData() {
		return playerData;
	}
	
	public void setPlayerData(PlayerData playerData) {
		this.playerData = playerData;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
