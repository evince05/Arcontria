package dev.eternalformula.arcontria.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.eternalformula.arcontria.ecs.components.AnimationComponent;
import dev.eternalformula.arcontria.ecs.components.MotionComponent;
import dev.eternalformula.arcontria.ecs.components.StateComponent;
import dev.eternalformula.arcontria.ecs.components.TextureComponent;
import dev.eternalformula.arcontria.util.EntityUtil;

public class AnimationSystem extends IteratingSystem {

	private static final Family FAMILY = Family.all(AnimationComponent.class,
			TextureComponent.class, StateComponent.class).get();
	
	public AnimationSystem() {
		super(FAMILY);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		for (Entity e : this.getEngine().getEntitiesFor(FAMILY)) {
			
			// Gets the components.
			TextureComponent texComp = TextureComponent.Map.get(e);
			AnimationComponent animComp = AnimationComponent.Map.get(e);
			StateComponent stateComp = StateComponent.Map.get(e);
			
			int direction = 0;
			
			if (e.getComponent(MotionComponent.class) != null) {
				
				MotionComponent motComp = MotionComponent.Map.get(e);
				direction = motComp.getDirection();
			}
			
			String dirStr = EntityUtil.getDirection(direction);
			
			String animName = stateComp.getState().animPrefix;
			
			if (!dirStr.equalsIgnoreCase("none")) {
				animName += "-" + dirStr;
			}
			
			animComp.currentAnim = animComp.getAnimation(animName);
			stateComp.stateTime += deltaTime;
			
			// Sets the texture to the proper direction and the current state.
			boolean isLooping = stateComp.isLooping;
			float stateTime = stateComp.getStateTime();
			
			// Sets the texture component's region.
			TextureRegion newRegion = animComp.getCurrentAnimation()
					.getKeyFrame(stateTime, isLooping);
			
			texComp.setTextureRegion(newRegion);
			
		}
	}

}
