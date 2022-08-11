package dev.eternalformula.arcontria.cutscenes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.pathfinding.NavigationPath;
import dev.eternalformula.arcontria.util.EFConstants;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.Strings;

/**
 * A less-meaty version of the LivingEntity, used for cutscenes.
 * @author EternalFormula
 */

public class CutsceneEntity {

	private static final float DEFAULT_SPEED = 1.75f;
	
	private Animation<TextureRegion> currentAnim;
	private Vector2 location;
	
	private NavigationPath path;
	private float speed;
	
	private String name;
	private UUID uuid;
	
	private Map<String, Animation<TextureRegion>> animations;
	private float elapsedTime;
	
	private boolean isCurrentAnimLooping;
	
	
	CutsceneEntity(String name, UUID uuid) {
		this.name = name;
		this.uuid = uuid;
		this.animations = new HashMap<String, Animation<TextureRegion>>();
		this.elapsedTime = 0f;
		this.speed = DEFAULT_SPEED;
		
		this.isCurrentAnimLooping = true;
		
		if (name.equalsIgnoreCase("%player%")) {
			this.name = "player";
			loadAnimations();
		}
		
	}
	
	private void loadAnimations() {
		String path = "textures/entities/" + name + "/" + name + ".atlas";
		
		// Generic animation prepping
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(path));
		Array<TextureRegion> frames = new Array<>();
		
		// Blink
		for (int i = 0; i < atlas.getRegions().size; i++) {
			
			TextureRegion reg = atlas.getRegions().get(i);
			String regName = atlas.getRegions().get(i).name;
			
			float numFrames = reg.getRegionWidth() / 16f;
			for (int j = 0; j < numFrames; j++) {
				frames.add(new TextureRegion(reg, j * 16, 0, 16, reg.getRegionHeight()));
			}
			animations.put(regName, new Animation<TextureRegion>(0.125f, frames));
			frames.clear();
			
			
			
		}
		
		System.out.println("Loaded " + animations.size() + " animations!");
		
		
		
	}
	
	public void setLocation(Vector2 pos) {
		this.location = pos;
	}
	
	public void setLocation(float x, float y) {
		this.location = new Vector2(x, y);
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public void moveTo(Vector2 pos) {
		
	}
	
	public void update(float delta) {
		if (currentAnim.isAnimationFinished(elapsedTime)) {
			elapsedTime = 0f;
		}
		
		/*
		 * This code isn't needed as the target shouldn't be moving.
		 * Keeping it here in case I need it again.
		 * 
		path.recalibrate(location, target.getLocation());
		path.update();
		*/
		
		if (path.getCurrentPath().getCount() > 0) {
			Vector2 targetPos = path.getCurrentPath().get(0).getPortal().getMidpoint();
			moveToTarget(delta, path, targetPos);
		}
	}
	
	public void draw(SpriteBatch batch, float delta) {
		elapsedTime += delta;
		if (currentAnim != null) {
			
			// Gets the textureregion
			TextureRegion reg = currentAnim.getKeyFrame(elapsedTime, isCurrentAnimLooping);
			float w = reg.getRegionWidth() / EFConstants.PPM;
			float h = reg.getRegionHeight() / EFConstants.PPM;
			
			batch.draw(reg, location.x, location.y, w, h);
		}
		
	}
	
	public void setAnimation(String animName, boolean looping) {
		if (animations.get(animName) != null) {
			this.currentAnim = animations.get(animName);
			this.isCurrentAnimLooping = looping;
			
			
			EFDebug.info("Set " + name + "'s anim to " + animName);
		}
		else {
			EFDebug.error("Could not set CutsceneEntity animation! Reason: Null");
		}
	}
	
	private void moveToTarget(float delta, NavigationPath path,  Vector2 targetPos) {
		
		float distanceX = targetPos.x - location.x;
		float distanceY = targetPos.y - location.y;
		
		float directionX = Math.signum(distanceX);
		float horizontalVelocity = directionX * speed;
		
		float directionY = Math.signum(distanceY);
		float verticalVelocity = directionY * speed;
		
		if (Math.abs(distanceY - distanceX) < 1 / 16f) {
			/*
			 * Distances are close enough to each other.
			 * The entity moves on both axes so it doesn't stutter.
			 */
			
			horizontalVelocity = directionX * (float) Math.sqrt(2) * speed;
			verticalVelocity = directionY * (float) Math.sqrt(2) * speed;
			
			move(horizontalVelocity, verticalVelocity);
		}
		else if (Math.abs(distanceX) >= Math.abs(distanceY)) {
			// The entity moves on the X axis.
			move(horizontalVelocity, 0f);
		}
		else {
			// The entity moves on the Y axis.
			move(0f, verticalVelocity);
		}
	}
	
	public void move(float velX, float velY) {
		location.x += velX;
		location.y += velY;
	}

}
