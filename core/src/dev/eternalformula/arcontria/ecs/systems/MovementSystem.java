package dev.eternalformula.arcontria.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import dev.eternalformula.arcontria.ecs.components.MotionComponent;
import dev.eternalformula.arcontria.ecs.components.PositionComponent;
import dev.eternalformula.arcontria.ecs.components.StateComponent;
import dev.eternalformula.arcontria.ecs.components.StateComponent.State;

public class MovementSystem extends IteratingSystem {

	public MovementSystem() {
		super(Family.all(MotionComponent.class, PositionComponent.class,
				StateComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		MotionComponent motionComp = MotionComponent.Map.get(entity);
		PositionComponent posComp = PositionComponent.Map.get(entity);
		StateComponent stateComp = StateComponent.Map.get(entity);
		
		State lastState = stateComp.getState();
		int direction = motionComp.direction;
		
		if (motionComp.velocity.x != 0) {
			posComp.position.x += motionComp.velocity.x * deltaTime;
			direction = Math.signum(motionComp.velocity.x) == 1 ? 3 : 2;
			motionComp.isMoving = true;
		}
		
		if (motionComp.velocity.y != 0) {
			posComp.position.y += motionComp.velocity.y * deltaTime;
			direction = Math.signum(motionComp.velocity.y) == 1 ? 1 : 4;
			motionComp.isMoving = true;
		}
		
		if (motionComp.velocity.x == 0 && motionComp.velocity.y == 0) {
			motionComp.isMoving = false;
		}
		
		motionComp.setDirection(direction);
		if (motionComp.isMoving && lastState != State.MOVING) {
			stateComp.setState(State.MOVING, true);
		}
		else if (!motionComp.isMoving && lastState == State.MOVING) {
			stateComp.setState(State.IDLE);
		}
		
		
	}
	
	

}
