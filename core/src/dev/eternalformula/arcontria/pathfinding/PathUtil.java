package dev.eternalformula.arcontria.pathfinding;

import dev.eternalformula.arcontria.entity.hostile.HostileEntity;
import dev.eternalformula.arcontria.util.EFDebug;

public class PathUtil {
	
	public static void moveToTarget(HostileEntity e, float delta) {
		
		PathNode node = e.getPath().getNode(1);
		
		float distanceX = node.getPosition().x - e.getLocation().x;
		float distanceY = node.getPosition().y - e.getLocation().y;
		
		if (Math.abs(distanceX) >= Math.abs(distanceY)) {
			EFDebug.debug("Moving on X axis");
			
			// Move on X axis first.
			float directionX = Math.signum(distanceX);
			
			float horizontalVelocity = directionX * e.getSpeed();
			EFDebug.debug("Horizontal Velocity: " + horizontalVelocity);
			if (directionX == 1) {
				e.setDirection(3);
			}
			else {
				e.setDirection(2);
			}
			e.move(horizontalVelocity, 0f);
			
			float newDistanceX = node.getPosition().x - e.getLocation().x;
			
			/*
			if (Math.abs(newDistanceX) < 0.0625f) {
				EFDebug.debug("Force set X");
				e.setLocation(node.getPosition().x, e.getLocation().y);
			}*/
		}
		else {
			EFDebug.debug("Moving on Y axis");
			// Move on Y axis
			float directionY = Math.signum(distanceY);
			
			float verticalVelocity = directionY * e.getSpeed();
			if (directionY == 1) {
				e.setDirection(1);
			}
			else {
				e.setDirection(4);
			}
			EFDebug.debug("Vertical Velocity: " + verticalVelocity);
			e.move(0f, verticalVelocity);
			
			float newDistanceY = node.getPosition().y - e.getLocation().y;
			
			/*
			if (Math.abs(newDistanceY) < 0.0625f) {
				EFDebug.debug("Force set Y");
				e.setLocation(e.getLocation().x, node.getPosition().y);
			}*/
		}
		
		if (e.getLocation().equals(node.getPosition())) {
			e.getPath().removeNode(1);
		}
	}

}
