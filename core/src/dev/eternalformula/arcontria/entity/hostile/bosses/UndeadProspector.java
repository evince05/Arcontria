package dev.eternalformula.arcontria.entity.hostile.bosses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.entity.LivingEntity;
import dev.eternalformula.arcontria.entity.projectiles.ProspectorPickaxe;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.boxes.EntityHitbox;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFConstants;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.Strings;

public class UndeadProspector extends LivingEntity {

	private TextureAtlas atlas;
	
	private Animation<TextureRegion> idleDown;
	private Animation<TextureRegion> shootingDown;
	
	private boolean canShoot;
	private boolean isShooting;
	
	private float shootingCooldown;
	private float shootingTimer;
	
	private TextureRegion pickaxeReg;
	
	public UndeadProspector(GameLevel level, Vector2 location) {
		super(level);
	
		width = 1f;
		height = 2f;
		this.location = location;
		this.hitbox = new EntityHitbox(level.getWorld(), this);
		
		shootingCooldown = 3f;
		shootingTimer = 0f;
		
		// Atlas
		this.atlas = Assets.get("textures/entities/bosses/udp.atlas", TextureAtlas.class);
		
		this.idleDown = new Animation<TextureRegion>(1f, atlas.findRegion("udp_down"));
		
		TextureRegion reg = atlas.findRegion("udp_throw_down");
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for (int i = 0; i < 3; i++) {
			frames.add(new TextureRegion(reg, 32 * i, 0, 32, 32));
		}
		
		this.shootingDown = new Animation<TextureRegion>(0.08f, frames);
		this.pickaxeReg = new TextureRegion(new Texture(Gdx.files.internal("pickaxe.png")));
		
		currentAnimation = idleDown;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (canShoot) {
			currentAnimation = shootingDown;
			elapsedTime = 0f;
			canShoot = false;
			isShooting = true;
		}
		else {
			shootingTimer += delta;
			if (shootingTimer >= shootingCooldown) {
				canShoot = true;
				shootingTimer = 0f;
			}
		}
		
		if (isShooting && currentAnimation.equals(shootingDown)) {
			if (currentAnimation.getKeyFrameIndex(elapsedTime) == 2) {
				ProspectorPickaxe axe = new ProspectorPickaxe(level, new Vector2(location.x, location.y - 0.25f), 4);
				axe.setTex(pickaxeReg);
				level.addEntity(axe);
				
				
				currentAnimation = idleDown;
				isShooting = false;
				canShoot = false;
			}
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float delta) {
		elapsedTime += delta;
		TextureRegion reg = currentAnimation.getKeyFrame(elapsedTime, true);
		
		float w = reg.getRegionWidth() / EFConstants.PPM;
		float h = reg.getRegionHeight() / EFConstants.PPM;

		if (currentAnimation.equals(shootingDown)) {
			batch.draw(reg, location.x - 0.5f, location.y, w, h);
		}
		else {
			batch.draw(reg, location.x, location.y, w, h);
		}
	}
}
