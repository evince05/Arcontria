package dev.eternalformula.arcontria.cutscenes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.util.EFDebug;

/**
 * A less-meaty version of the LivingEntity, used for cutscenes.
 * @author EternalFormula
 */

public class CutsceneEntity {

	private Animation<TextureRegion> currentAnim;
	private Vector2 location;
	
	private String name;
	private UUID uuid;
	
	private Map<String, Animation<TextureRegion>> animations;
	private float elapsedTime;
	
	
	CutsceneEntity(String name, UUID uuid) {
		this.name = name;
		this.uuid = uuid;
		this.animations = new HashMap<String, Animation<TextureRegion>>();
		this.elapsedTime = 0f;
	}
	
	public void setLocation(Vector2 pos) {
		this.location = pos;
	}
	
	public void setLocation(float x, float y) {
		this.location = new Vector2(x, y);
	}
	
	public void moveTo(Vector2 pos) {
		
	}
	
	public void update(float delta) {
		if (currentAnim.isAnimationFinished(elapsedTime)) {
			elapsedTime = 0f;
		}
	}
	
	public void draw(SpriteBatch batch, float delta) {
		elapsedTime += delta;
		batch.draw(currentAnim.getKeyFrame(elapsedTime), location.x, location.y);
	}
	
	public void setAnimation(String animName) {
		if (animations.get(animName) != null) {
			this.currentAnim = animations.get(animName);
		}
		else {
			EFDebug.error("Could not set CutsceneEntity animation! Reason: Null");
		}
	}

}
