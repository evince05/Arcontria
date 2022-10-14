package dev.eternalformula.arcontria.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.ecs.components.ItemEntityComponent;
import dev.eternalformula.arcontria.ecs.components.MotionComponent;
import dev.eternalformula.arcontria.ecs.components.PathfindingComponent;
import dev.eternalformula.arcontria.ecs.components.PositionComponent;
import dev.eternalformula.arcontria.ecs.components.TextureComponent;
import dev.eternalformula.arcontria.ecs.templates.Player;
import dev.eternalformula.arcontria.pathfinding.paths.Path;
import dev.eternalformula.arcontria.scenes.GameScene;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.EFMath;
import dev.eternalformula.arcontria.util.Strings;

public class ItemEntitySystem extends IteratingSystem {

	private static Family FAMILY = Family.all(ItemEntityComponent.class, TextureComponent.class,
			PositionComponent.class).get();
	
	public ItemEntitySystem() {
		super(FAMILY);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PositionComponent posComp = entity.getComponent(PositionComponent.class);
		if (getDistanceToPlayer(posComp.position) <= Player.pickupItemRange) {
			
			// Item will begin moving toward the player
			if (entity.getComponent(PathfindingComponent.class) != null) {
				
				PathfindingComponent pathComp = entity.getComponent(PathfindingComponent.class);
				if (!pathComp.hasPath()) {
					// Gets the end pos (player pos)
					Player player = ((GameScene) ArcontriaGame.getCurrentScene()).getPlayer();
					Vector2 endPos = player.getComponent(PositionComponent.class).position;
					// Sets path
					Path path = new Path(posComp.position, endPos, Path.PATH_DIRECT);
					path.entitySpeed = 3f;
					path.acceleration = 6f;
					pathComp.setPath(path);
				}
				else {
					if (pathComp.getPath().isFinished()) {
						pathComp.setPath(null);
					}
				}
			}
		}
	}
	
	public float getDistanceToPlayer(Vector2 pos) {
		Player player = ((GameScene) ArcontriaGame.getCurrentScene()).getPlayer();
		Entity playerEntity = player.getPlayerEntity();
		PositionComponent posComp = playerEntity.getComponent(PositionComponent.class);
		
		return EFMath.getDistance(pos, posComp.position);
	}

}
