package dev.eternalformula.arcontria.pathfinding;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.entity.LivingEntity;
import dev.eternalformula.arcontria.level.maps.Map;
import dev.eternalformula.arcontria.util.Strings;

/**
 * The Path object consists of a target object/entity, the object/entity who is following the target,
 * and the list of nodes which the follower must traverse to reach the target.
 * @author EternalFormula
 */

public class Path {
	
	private Map map;
	private LivingEntity follower; // Follower is livingEntity (follower must move so they are living? lol)
	private Entity target; // Target can be inanimate or alive.
	
	private List<PathNode> nodes;
	
	public Path(Map map, LivingEntity follower, Entity target) {
		this.map = map;
		this.follower = follower;
		this.target = target;
		this.nodes = new ArrayList<PathNode>();
		calculatePath();
	}
	
	/**
	 * Calculates the proper path which the follower should traverse.
	 * <br>Also, this method stores the determined path in the nodes field.
	 */
	
	private void calculatePath() {
		Vector2 startPos = follower.getLocation();
		Vector2 endPos = target.getLocation();
		System.out.println("Calculating Path: " + Strings.vec2(startPos) + " -> " 
				+ endPos);
		
		PathNode startNode = map.getNode((int) startPos.x, (int) startPos.y);
		PathNode endNode = map.getNode((int) endPos.x, (int) endPos.y);
	}
	

}
