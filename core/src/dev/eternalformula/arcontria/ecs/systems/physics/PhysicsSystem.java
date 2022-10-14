package dev.eternalformula.arcontria.ecs.systems.physics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

import dev.eternalformula.arcontria.ecs.components.MotionComponent;
import dev.eternalformula.arcontria.ecs.components.PositionComponent;
import dev.eternalformula.arcontria.ecs.components.physics.ColliderComponent;
import dev.eternalformula.arcontria.scenes.GameScene;

public class PhysicsSystem extends EntitySystem {
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		for (Entity e : GameScene.ENGINE.getEntities()) {
			
			if (e.getComponent(ColliderComponent.class) != null) {
				
				// Only moveable entities need to have their collider components updated
				if (e.getComponent(MotionComponent.class) != null) {
					PositionComponent posComp = e.getComponent(PositionComponent.class);
					ColliderComponent collComp = e.getComponent(ColliderComponent.class);
					
					collComp.b2dBody.setTransform(posComp.position, 0f);
					
				}
			}
		}
	}

}
