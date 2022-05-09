package dev.eternalformula.arcontria.pathfinding;

import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.entity.LivingEntity;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.EFMath;
import dev.eternalformula.arcontria.util.Strings;

public class PathUtil {
	
	/**
	 * Note: Allows for simultaneous X and Y movement.
	 */
	public static void moveToTarget(float delta, NavigationPath path, LivingEntity entity, Vector2 targetPos) {
		
		EFDebug.debug("--- New Frame ---");
		
		float distanceX = targetPos.x - entity.getLocation().x;
		float distanceY = targetPos.y - entity.getLocation().y;
		
		EFDebug.debug("dst=" + Strings.vec2(distanceX, distanceY));
		
		float directionX = Math.signum(distanceX);
		float horizontalVelocity = directionX * entity.getSpeed();
		
		float directionY = Math.signum(distanceY);
		float verticalVelocity = directionY * entity.getSpeed();
		
		if (Math.abs(distanceY - distanceX) < 1 / 16f) {
			/*
			 * Distances are close enough to each other.
			 * The entity moves on both axes so it doesn't stutter.
			 */
			
			horizontalVelocity = directionX * (float) Math.sqrt((double) entity.getSpeed());
			verticalVelocity = directionY * (float) Math.sqrt((double) entity.getSpeed());
			
			entity.move(horizontalVelocity, verticalVelocity);
			EFDebug.debug("Moving on both axes, [[velX,velY]: " + 
					Strings.vec2(horizontalVelocity, verticalVelocity) + "], v= "
					+ entity.getSpeed() + ", [[dX,dY]: "
					+ Strings.vec2(directionX, directionY) + "]");
		}
		else if (Math.abs(distanceX) >= Math.abs(distanceY)) {
			// The entity moves on the X axis.
			entity.move(horizontalVelocity, 0f);
			EFDebug.debug("Moving on x axis");
		}
		else {
			// The entity moves on the Y axis.
			entity.move(0f, verticalVelocity);
			EFDebug.debug("Moving on y axis");
		}
	}
	
	/**
	 * Safe (one axis at a time) movement only. 
	 * See {@link PathUtil#moveToTarget(float, LivingEntity, Vector2)} for regular.
	 * @param endPos The final position of the path.
	 */
	
	public static void safeMoveToTarget(float delta, LivingEntity entity, Vector2 targetPos, Vector2 endPos) {
		
		float distanceX = targetPos.x - entity.getLocation().x;
		float distanceY = targetPos.y - entity.getLocation().y;
		
		float deltaDistance = Math.abs(distanceY - distanceX);
		
		int direction = 0;
		
		float angle = EFMath.getAngle(entity.getLocation(), targetPos);

		float directionX = Math.signum(distanceX);
		float horizontalVelocity = directionX * entity.getSpeed();
			
		float directionY = Math.signum(distanceY);
		float verticalVelocity = directionY * entity.getSpeed();
		
		if (deltaDistance >= 1.25f || deltaDistance >= 1.75f) {
			if (Math.abs(distanceX) >= Math.abs(distanceY)) {
				entity.move(horizontalVelocity, 0f);
			}
			else {
				entity.move(0f, verticalVelocity);
			}
		}
		else {
			entity.move(horizontalVelocity, verticalVelocity);
		}
		
		if (angle >= 45 && angle <= 135) {
			// Direction up
			entity.setDirection(1);
			direction = 1;
		}
		else if (angle > 135 && angle < 225) {
			// Direction left
			entity.setDirection(2);
			direction = 2; 
		}
		else if (angle < 45 || angle > 315) {
			// Direction right
			entity.setDirection(3);
			direction = 3;
		}
		else if (angle >= 225 && angle <= 315) {
			// Should always be down. angle >= 225 && angle <= 315
			entity.setDirection(4);
			direction = 4;
		}
		
		EFDebug.info("DD: " + deltaDistance + ", Angle: " + angle + ", Direction: " + direction);
	}

}
