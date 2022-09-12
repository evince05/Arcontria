package dev.eternalformula.arcontria.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

import dev.eternalformula.arcontria.ecs.components.HealthComponent;

public class HealthSystem extends EntitySystem {
	
	private static final Family FAMILY = Family.all(HealthComponent.class).get();
	
	public HealthSystem() {
		super();
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		for (Entity e : this.getEngine().getEntitiesFor(FAMILY)) {
			HealthComponent healthComp = HealthComponent.Map.get(e);
		}
	}
	 
	
}
