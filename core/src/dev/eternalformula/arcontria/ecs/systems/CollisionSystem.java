package dev.eternalformula.arcontria.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import dev.eternalformula.arcontria.ecs.components.CollisionComponent;
import dev.eternalformula.arcontria.ecs.components.PlayerComponent;
import dev.eternalformula.arcontria.ecs.components.TypeComponent;

public class CollisionSystem extends IteratingSystem {
	
	private ComponentMapper<CollisionComponent> cm;
	
	public CollisionSystem() {
		// Only need to really detect and handle player collisions
		super(Family.all(CollisionComponent.class, PlayerComponent.class).get());
		
		this.cm = ComponentMapper.getFor(CollisionComponent.class);
		
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		CollisionComponent cc = cm.get(entity);
		
		Entity collidedEntity = cc.collisionEntity;
		if (collidedEntity != null) {
			// Handle entity collision logic
			TypeComponent typeComp = collidedEntity.getComponent(TypeComponent.class);
			switch (typeComp.type) {
			//case TypeComponent.
			}
		}
	}

}
