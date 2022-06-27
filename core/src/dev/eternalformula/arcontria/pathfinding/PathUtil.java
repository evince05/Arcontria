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
		
		float distanceX = targetPos.x - entity.getLocation().x;
		float distanceY = targetPos.y - entity.getLocation().y;
		
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
		}
		else if (Math.abs(distanceX) >= Math.abs(distanceY)) {
			// The entity moves on the X axis.
			entity.move(horizontalVelocity, 0f);
		}
		else {
			// The entity moves on the Y axis.
			entity.move(0f, verticalVelocity);
		}
	}
}
