package dev.eternalformula.arcontria.entity.hostile;

import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.entity.LivingEntity;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.pathfinding.Path;
import dev.eternalformula.arcontria.pathfinding.PathNode;
import dev.eternalformula.arcontria.pathfinding.PathUtil;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.Strings;

public abstract class HostileEntity extends LivingEntity {
	
	protected LivingEntity target;
	
	protected Path pathToTarget;
	protected Vector2 lastTargetPosition;
	
	/**
	 * This boolean determines if the HostileEntity should chase the<br>
	 * player to execute a melee attack. If false, a custom chasing<br>
	 * algorithm must be implemented in the child's update method.
	 */
	
	protected boolean meleeAttacker;
	
	// idea: entitybuilder class?
	public HostileEntity(GameLevel level, LivingEntity target, Vector2 location) {
		super(level);
		this.target = target;
		
		// HostileEntity must have location to avoid a NPE in path init.
		this.location = location;
		
		this.meleeAttacker = true;
		
	}
	
	protected void calculatePath() {
		this.pathToTarget = new Path(level.getMap(), this, target);
		this.lastTargetPosition = new Vector2(target.getLocation());
		
		EFDebug.debug("Path Size: " + pathToTarget.getLength());
		level.getPathRenderer().addPath(pathToTarget);
		
		for (PathNode pathNode : pathToTarget.getNodes()) {
			EFDebug.debug("Node: " + Strings.vec2(pathNode.getPosition()));
		}
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		// Watch this algorithm for efficiency and CPU usage...
	
		// Path is only recalculated if the target has recently moved.
		if (lastTargetPosition.x != target.getLocation().x ||
				lastTargetPosition.y != target.getLocation().y) {
			pathToTarget.calculatePath();
		}
		
		// Update the target's last position
		this.lastTargetPosition = new Vector2(target.getLocation());
		
		if (pathToTarget.getLength() > 1) {
			if (meleeAttacker) {
				PathUtil.moveToTarget(this, delta);
			}
		}	
	}
	
	/**
	private void moveToPlayer(float delta) {
		
		if (pathToTarget.getLength() > 1) {
			
			float horizontalForce = 0f;
			float verticalForce = 0f;
			
			PathNode nextNode = pathToTarget.getNode(1);
			
			float distanceX = nextNode.gridX - location.x;
			float distanceY = nextNode.gridY - location.y;
			
			//System.out.println("Distances: " + Strings.vec2(distanceX, distanceY));
			
			
			 * Determines the direction in which the entity should first move.
			 * In the case that the two distances are equal, the entity will move
			 * first horizontally.
			 
			
			if (Math.abs(distanceY) != 0) { // old: math.abs(distanceY) > math.abs(distanceX)
				float directionY = Math.signum(distanceY);
				verticalForce = directionY * speed;
				
				if (directionY == 1) {
					this.direction = 1;
				}
				else {
					this.direction = 4;
				}
			}
			else {
				float directionX = Math.signum(distanceX);
				horizontalForce = directionX * speed;
				
				if (directionX == 1) {
					this.direction = 3;
				}
				else {
					this.direction = 2;
				}
			}
			
			if (Math.abs(horizontalForce) > 0 || Math.abs(verticalForce) > 0) {
				isMoving = true;
				
				System.out.println("Forces: " + Strings.vec2(horizontalForce, verticalForce));
				move(delta, horizontalForce, verticalForce);
			}
			else {
				//body.setLinearVelocity(0f, 0f);
			}
		}
	}*/
	
	public LivingEntity getTarget() {
		return target;
	}
	
	public Path getPath() {
		return pathToTarget;
	}
	
	public boolean isMeleeAttacker() {
		return meleeAttacker;
	}
	
	public void setMeleeAttacker(boolean meleeAttacker) {
		this.meleeAttacker = meleeAttacker;
	}
}
