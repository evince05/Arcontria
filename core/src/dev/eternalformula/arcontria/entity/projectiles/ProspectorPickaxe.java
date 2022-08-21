package dev.eternalformula.arcontria.entity.projectiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import dev.eternalformula.arcontria.gfx.EGFXUtil;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.physics.boxes.ProjectileBox;
import dev.eternalformula.arcontria.util.EFConstants;

public class ProspectorPickaxe extends Projectile {

	private Vector2 centerPos;
	private Vector2 pos;
	
	private int direction;

	private int xDirection;
	
	private float speedX;
	private float amtMovedX;
	
	private float totalMoveAmtX;
	
	private boolean usePositiveRoot;
	
	private boolean isFinished;
	
	private float leftXBound;
	private float rightXBound;
	
	private float rotation;
	private float rotationTime;
	
	private TextureRegion reg;
	
	public ProspectorPickaxe(GameLevel level, Vector2 origin, int direction) {
		super(level, 100f);
		this.location = origin;
		this.direction = direction;
		
		// Center point
		this.pos = new Vector2(0f, 0f);
		
		// Defines bounds and speed
		if (direction == 1 || direction == 4) {
			leftXBound = -0.5f;
			rightXBound = 0.5f;
			speedX = 2.2f;
			totalMoveAmtX = 2f;
		}
		else if (direction == 2 || direction == 3) {
			leftXBound = -3f;
			rightXBound = 3f;
			speedX = 13.3f;
			totalMoveAmtX = 12f;
		}
		
		if (direction == 1) {
			xDirection = 1;
			usePositiveRoot = false;
			this.centerPos = new Vector2(origin.x, origin.y + 3f);
		}
		else if (direction == 2) {
			xDirection = -1;
			pos.x = rightXBound;
			usePositiveRoot = true;
			this.centerPos = new Vector2(origin.x - 3f, origin.y);
		}
		else if (direction == 3) {
			xDirection = 1;
			pos.x = leftXBound;
			usePositiveRoot = false;
			this.centerPos = new Vector2(origin.x + 3f, origin.y);
		}
		else if (direction == 4) {
			xDirection = -1;
			usePositiveRoot = true;
			this.centerPos = new Vector2(origin.x, origin.y - 3f);
		}
		
		this.rotation = -90f;
		this.rotationTime = 0f;
		
		this.width = 1f;
		this.height = 1f;
		
		this.box = new ProjectileBox(level, this);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		// Gets the appropriate x coord
		
		if (!isFinished) {
			if (xDirection == 1) {
				// X moves right
				if (pos.x + speedX * delta < rightXBound) {
					pos.x += speedX * delta;
					amtMovedX += speedX * delta;
				}
				else {
					// Calculates the amount moved to the boundary.
					float moveAmt = rightXBound - pos.x;
					amtMovedX += moveAmt;
					
					// Rounds up to avoid adding extra x values.
					if (totalMoveAmtX - amtMovedX < 0.01) {
						amtMovedX = totalMoveAmtX;
					}
					
					// X has reached right bound.
					pos.x = rightXBound;
					usePositiveRoot = true;
					xDirection = -1;
				}
			}
			else if (xDirection == -1) {
				// X moves left
				if (pos.x - speedX * delta > leftXBound) {
					pos.x -= speedX * delta;
					amtMovedX += speedX * delta;
				}
				else {
					// Calculates the amount moved to the boundary
					float moveAmt = pos.x - leftXBound;
					amtMovedX += moveAmt;
					
					// Rounds up to avoid adding extra x values.
					if (totalMoveAmtX - amtMovedX < 0.01) {
						amtMovedX = totalMoveAmtX;
					}
					
					// X has reached the left bound.
					pos.x = leftXBound;
					usePositiveRoot = false;
					xDirection = 1;
				}
			}
			
			// Checks if the move is complete
			if (amtMovedX / totalMoveAmtX >= 1f) {
				isFinished = true;
				rotation = 0f;
				return;
			}
			
			// Sets the y position
			pos.y = getYValue(pos.x, usePositiveRoot);
			
			box.getBody().setTransform(centerPos.x + pos.x + 1/2f, centerPos.y + pos.y + 1/2f, 0f);
			
			// Calculates the rotation
			rotationTime += delta;
			rotation = rotationTime / 0.4f * 360f;
			
			if (rotationTime >= 0.4f) {
				rotationTime = 0f;
			}
		}
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
		float locX = centerPos.x + pos.x;
		float locY = centerPos.y + pos.y;
		
		float w = reg.getRegionWidth() / EFConstants.PPM;
		float h = reg.getRegionHeight() / EFConstants.PPM;
		batch.draw(reg, locX, locY, 8/16f, 8/16f, w, h, 1f, 1f, rotation);
	}
	
	public void setTex(TextureRegion tex) {
		this.reg = tex;
	}
	
	/**
	 * Gets the appropriate y-value for the projectile based on the x-value.
	 * @param x The x value in the relation.
	 * @param usePositiveRoot True if the positive root should be used,<br>
	 * false if the negative should be used.
	 * 
	 * @return The appropriate y value for the projectile.
	 */
	
	private float getYValue(float x, boolean usePositiveRoot) {
		int sign = usePositiveRoot == true ? 1 : -1;
		
		float y = 0f;
		if (direction == 1 || direction == 4) {
			// Up or Down
			y = sign * (float) Math.sqrt((-9 * (float) Math.pow(x, 2) + 1/4f * 9f) * 4f);
		}
		else if (direction == 2 || direction == 3) {
			// Left or Right
			y = sign * (float) Math.sqrt((-1/4f * (float) Math.pow(x, 2) + 9 * 1/4f) / 9f);
		}
		
		return y;
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public void destroyBody(World world) {
		world.destroyBody(box.getBody());
	}
}
