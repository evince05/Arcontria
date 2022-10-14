package dev.eternalformula.arcontria.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import dev.eternalformula.arcontria.ecs.components.PathfindingComponent;

public class PathfindingSystem extends IteratingSystem {

	public static Family FAMILY = Family.all(PathfindingComponent.class).get();
	
	public PathfindingSystem() {
		super(FAMILY);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PathfindingComponent pathComp = entity.getComponent(PathfindingComponent.class);
		if (pathComp.hasPath()) {
			pathComp.getPath().update(deltaTime);
		}
	}

}
