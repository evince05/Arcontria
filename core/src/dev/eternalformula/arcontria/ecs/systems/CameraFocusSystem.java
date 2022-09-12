package dev.eternalformula.arcontria.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;

import dev.eternalformula.arcontria.ecs.components.CameraFocusComponent;
import dev.eternalformula.arcontria.ecs.components.PositionComponent;
import dev.eternalformula.arcontria.util.EFDebug;

public class CameraFocusSystem extends EntitySystem {
	
	private static Family FAMILY = Family.all(PositionComponent.class,
			CameraFocusComponent.class).get();
	
	private OrthographicCamera camera;
	
	private boolean hasThrownMultiFocusWarning;
	
	public CameraFocusSystem(OrthographicCamera camera) {
		super();
		
		this.camera = camera;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		ImmutableArray<Entity> entities = this.getEngine().getEntitiesFor(FAMILY);
		
		// Throws a warning if 2+ entities are being focused on.
		if (entities.size() > 1 && !hasThrownMultiFocusWarning) {
			hasThrownMultiFocusWarning = true;
			EFDebug.warn("CameraFocusSystem cannot focus on more than one entity!");
		}
		
		if (entities.get(0) != null) {
			// Gets the focus entity
			Entity focusEntity = entities.get(0);
			
			PositionComponent posComp = focusEntity.getComponent(PositionComponent.class);
			camera.position.set(posComp.getX(), posComp.getY(), 0f);
		}
	}

}
