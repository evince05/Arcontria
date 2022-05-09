package dev.eternalformula.arcontria.entity.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.entity.LivingEntity;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.pathfinding.NavigationPath;
import dev.eternalformula.arcontria.pathfinding.PathUtil;
import dev.eternalformula.arcontria.physics.boxes.EntityColliderBox;
import dev.eternalformula.arcontria.physics.boxes.EntityHitbox;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.EFMath;

public class NPC extends LivingEntity {

	private Animation<TextureRegion> walkUp;
	private Animation<TextureRegion> walkDown;
	private Animation<TextureRegion> walkLeft;
	private Animation<TextureRegion> walkRight;
	
	private NavigationPath path;
	
	public NPC(GameLevel level) {
		super(level);
		
		this.location = new Vector2(7f, 12f);
		
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/entities/player/player.atlas"));
		
		Array<TextureRegion> frames = new Array<>();
		TextureRegion region = atlas.findRegion("up");
		
		this.speed = 1f;
		
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		walkUp = new Animation<TextureRegion>(0.125f, frames);
 
		frames.clear();
		
		region = atlas.findRegion("down");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		walkDown = new Animation<TextureRegion>(0.125f, frames);
		frames.clear();
		
		region = atlas.findRegion("left");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		walkLeft = new Animation<TextureRegion>(0.125f, frames);
		frames.clear();
		
		region = atlas.findRegion("right");
		for (int i = 0; i < 4; i++) {
			frames.add(new TextureRegion(region, i * 16, 0, 16, 32));
		}
		walkRight = new Animation<TextureRegion>(0.125f, frames);
		frames.clear();
		
		this.width = 1f;
		this.height = 2f;
		
		this.hitbox = new EntityHitbox(level, this);
		this.colliderBox = new EntityColliderBox(level, this);
		
		Vector2 plrLoc = level.getPlayer().getLocation();
		Vector2 targetLocation = new Vector2(plrLoc.x + 0.5f, plrLoc.y);
		
		path = new NavigationPath(level.getMap(), colliderBox.getBody().getPosition(), targetLocation, width / 2f);
		
		this.currentAnimation = walkRight;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		path.recalibrate(location, level.getPlayer().getLocation());
		path.update();
		
		if (path != null && path.getPoints().size > 0) {
			Vector2 targetPos = path.getPoints().get(1);
			
			float angle = EFMath.getAngle(location, targetPos);
			direction = path.getAnimationDirection(this, angle);
			System.out.println(direction);
			
			switch (direction) {
			case 1:
				currentAnimation = walkUp;
				break;
			case 2:
				currentAnimation = walkLeft;
				break;
			case 3:
				currentAnimation = walkRight;
				break;
			case 4:
				currentAnimation = walkDown;
				break;
			default:
				currentAnimation = walkDown;
				break;
			}
			
			// The lastAnimationDirection is IMPORTANT!!
			lastAnimationDirection = direction;
			PathUtil.moveToTarget(delta, path, this, targetPos);
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float delta) {
		super.draw(batch, delta);
		
		if (EFDebug.mapDebugEnabled) {
			batch.end();
			path.draw();
			batch.begin();
		}
	}
}
