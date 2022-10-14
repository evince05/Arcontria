package dev.eternalformula.arcontria.pathfinding.paths;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.Strings;

public class Path {
	
	public static final int PATH_DIRECT = 0;
	public static final int PATH_A_STAR = 1;

	private Vector2 startPos;
	private Vector2 endPos;
	private final int pathType;
	
	public Vector2 currentPos;
	
	/**
	 * Speed may differ in x and y in some cases
	 */
	
	public Vector2 speed;
	public float totalTime;
	
	public float entitySpeed = 1f;
	public float acceleration = 0f;
	
	private float amtMovedX;
	private boolean finished;
	
	public Path(Vector2 startPos, Vector2 endPos, int pathType) {
		this.startPos = startPos;
		this.endPos = endPos;
		
		this.pathType = pathType;
		
		this.currentPos = startPos;
	}
	
	public void update(float delta) {
		if (!finished) {
			switch (pathType) {
			case PATH_DIRECT:
				updateDirect(delta);
			case PATH_A_STAR:
				updateAStar(delta);
			default:
				return;
			}
		}
		
	}
	
	/**
	 * Handles the updating of the "direct" path system (fast, unobstructed pathfinding).
	 * @param delta Delta time
	 */
	
	private void updateDirect(float delta) {
		
		Interpolation inpol = Interpolation.circleIn;
		System.out.println(Strings.vec2(startPos.y, endPos.y));
		
		float distX = endPos.x - startPos.x;
		float distY = endPos.y - startPos.y;
		
		if (Math.abs(currentPos.y - endPos.y) > 0.125f) {
			currentPos.y += inpol.apply(startPos.y, endPos.y, delta / entitySpeed)
					* delta * Math.signum(distY);
		}
		else {
			if (currentPos.y != endPos.y) {
				currentPos.y = endPos.y;
			}
		}
		
		if (Math.abs(currentPos.x - endPos.x) > 0.125f) {
			currentPos.x += inpol.apply(startPos.x, endPos.x, delta / entitySpeed)
					* delta * Math.signum(distX);
		}
		else {
			if (currentPos.x != endPos.x) {
				currentPos.x = endPos.x;
			}
		}
		
		if (currentPos.x == endPos.x && currentPos.y == endPos.y) {
			finished = true;
		}
		
		
		
		/*
		float distanceX = endPos.x - startPos.x;
		float distanceY = endPos.y - startPos.y;
		
		EFDebug.info("" + Math.signum(distanceX));
		
		float dirX = Math.signum(distanceX);
		float moveAmtX = 0f;
		
		if (dirX == 1) {
			// Move right on X
			if (currentPos.x + entitySpeed * delta <= endPos.x) {
				moveAmtX = entitySpeed * delta;
				currentPos.x += moveAmtX;
			}
			else {
				// Move right to target X
				moveAmtX = endPos.x - currentPos.x;
				currentPos.x = endPos.x;
			}
		}
		else {
			// Move left on X
			if (currentPos.x - entitySpeed * delta >= endPos.x) {
				moveAmtX = -entitySpeed * delta;
				currentPos.x -= entitySpeed * delta;
			}
			else {
				// Move left to target Y
				moveAmtX = endPos.x - startPos.x;
				currentPos.x = endPos.x;
			}
		}
		
		// Adjusts y value according to the slope
		EFDebug.info("mX=" + Math.signum(moveAmtX) + ", slope=" + slope);
		if (((Float) slope).isNaN()) {
			slope = 1000f * Math.signum(distanceY);
		}
		currentPos.y += Math.signum(moveAmtX) * Math.min(slope, 50f) * delta;
		
		entitySpeed += acceleration * delta;
		/*
		float dirX = Math.signum(distanceX);
		float velX = dirX * entitySpeed.x;
		
		float dirY = Math.signum(distanceY);
		float velY = dirY * entitySpeed.y;
		
		float absDistX = Math.abs(distanceX);
		float absDistY = Math.abs(distanceY);
		
		if (Math.abs(absDistY - absDistX) < 4 / 16f) {
			// Move on both axes
			
			currentPos.x += (velX / (float) Math.sqrt(2) * delta);
			currentPos.y += (velY / (float) Math.sqrt(2) * delta);
		}
		else if (absDistX >= absDistY) {
			// Moves on the x axis
			currentPos.x += velX * delta;
		}
		else {
			// Moves on the y axis
			currentPos.y += velY * delta;
		}
		
		float absRemX = Math.abs(endPos.x - currentPos.x);
		float absRemY = Math.abs(endPos.y - currentPos.y);
		
		if (absRemX > 0 && absRemX < 0.0125f) {
			currentPos.x = endPos.x;
		}
		
		if (absRemY > 0 && absRemY < 0.0125f) {
			currentPos.y = endPos.y;
		}
		
		/*
		System.out.println("Start=" + Strings.vec2(startPos) + ", End=" + Strings.vec2(endPos) + "m=" + slope);
		
		float xDir = Math.signum(endPos.x - startPos.x);
		
		if (xDir == 1) {
			// Right
			if (currentPos.x + entitySpeed <= endPos.x) {
				currentPos.x += entitySpeed * delta;
			}
			else {
				currentPos.x = endPos.x;
			}
		}
		else {
			// Left
			if (currentPos.x - entitySpeed >= endPos.x) {
				currentPos.x -= entitySpeed * delta;
			}
			else {
				currentPos.x = endPos.x;
			}	
		}
		// Calculate y-value
		currentPos.y += slope * delta;
		
		System.out.println("Item Pos: " + Strings.vec2(currentPos));
		*/
	}
	
	/**
	 * Handles the updating of the "A star" path system (optimized pathfinding through a grid<br>
	 * which may contain obstacles).
	 * @param delta Delta time
	 */
	private void updateAStar(float delta) {
		// TODO: add logic
	}
	
	public boolean isFinished() {
		return finished;
	}
}
