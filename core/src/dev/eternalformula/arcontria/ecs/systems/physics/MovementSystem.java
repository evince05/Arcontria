package dev.eternalformula.arcontria.ecs.systems.physics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import dev.eternalformula.arcontria.ecs.components.MotionComponent;
import dev.eternalformula.arcontria.ecs.components.PositionComponent;
import dev.eternalformula.arcontria.ecs.components.StateComponent;
import dev.eternalformula.arcontria.ecs.components.StateComponent.State;
import dev.eternalformula.arcontria.ecs.components.physics.ColliderComponent;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.Strings;

public class MovementSystem extends IteratingSystem {

	public MovementSystem() {
		super(Family.all(MotionComponent.class, PositionComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		MotionComponent motionComp = MotionComponent.Map.get(entity);
		PositionComponent posComp = PositionComponent.Map.get(entity);
		StateComponent stateComp = StateComponent.Map.get(entity);
		
		int direction = motionComp.direction;
		
		if (motionComp.velocity.x != 0) {
			direction = Math.signum(motionComp.velocity.x) == 1 ? 3 : 2;
			motionComp.velocity.x += Math.signum(motionComp.velocity.x) *
					motionComp.acceleration.x * deltaTime;
			motionComp.isMoving = true;
		}
		
		if (motionComp.velocity.y != 0) {
			direction = Math.signum(motionComp.velocity.y) == 1 ? 1 : 4;
			motionComp.velocity.y += Math.signum(motionComp.velocity.y) *
					motionComp.acceleration.y * deltaTime;
			motionComp.isMoving = true;
		}
		
		if (motionComp.velocity.x == 0 && motionComp.velocity.y == 0) {
			motionComp.isMoving = false;
		}
		
		if (entity.getComponent(ColliderComponent.class) != null) {
			ColliderComponent collComp = entity.getComponent(ColliderComponent.class);
			collComp.b2dBody.setLinearVelocity(motionComp.velocity);
			posComp.position = collComp.b2dBody.getTransform().getPosition();
		}
		// Other component clauses go here
		else {
			posComp.position.x += motionComp.velocity.x * deltaTime;
			posComp.position.y += motionComp.velocity.y * deltaTime;
		}
		
		if (stateComp != null) {
			State lastState = stateComp.getState();
			if (motionComp.isMoving && lastState != State.MOVING) {
				stateComp.setState(State.MOVING, true);
			}
			else if (!motionComp.isMoving && lastState == State.MOVING) {
				stateComp.setState(State.IDLE);
			}
		}
		motionComp.setDirection(direction);
		
		
		
		
	}
	
	

}
