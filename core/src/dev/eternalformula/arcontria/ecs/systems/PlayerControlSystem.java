package dev.eternalformula.arcontria.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.ecs.components.MotionComponent;
import dev.eternalformula.arcontria.ecs.components.PlayerComponent;
import dev.eternalformula.arcontria.ecs.components.PositionComponent;

public class PlayerControlSystem extends IteratingSystem {

	public PlayerControlSystem() {
		super(Family.all(PlayerComponent.class, PositionComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		// Gets the position component
		MotionComponent motionComp = entity.getComponent(MotionComponent.class);
		motionComp.velocity = new Vector2(0f, 0f);
		
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			motionComp.velocity.y = motionComp.speed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			motionComp.velocity.x = -motionComp.speed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			motionComp.velocity.y = -motionComp.speed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			motionComp.velocity.x = motionComp.speed;
		}
	}
}
