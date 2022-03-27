package dev.eternalformula.arcontria.pathfinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.entity.LivingEntity;
import dev.eternalformula.arcontria.level.maps.Map;

/**
 * The Path object consists of a target object/entity, the object/entity who is following the target,
 * and the list of nodes which the follower must traverse to reach the target.
 * @author EternalFormula
 */

public class Path {
	
	private static final String TAG = Path.class.getName();
	
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
	
	public void calculatePath() {
		Vector2 startPos = follower.getLocation();
		Vector2 endPos = target.getLocation();
		
		PathNode startNode = map.getNode((int) startPos.x, (int) startPos.y);
		PathNode endNode = map.getNode((int) endPos.x, (int) endPos.y);
		
		List<PathNode> openNodes = new ArrayList<>();
		openNodes.add(startNode);
		
		// HashSet is a list with no duplicate members.
		HashSet<PathNode> closedNodes = new HashSet<>();
		
		while (openNodes.size() > 0) {
			PathNode currentNode = openNodes.get(0);
			for (int i = 1; i < openNodes.size(); i++) {
				
				if (openNodes.get(i).fCost() < currentNode.fCost() || 
						openNodes.get(i).fCost() == currentNode.fCost()) {
					
					if (openNodes.get(i).hCost < currentNode.hCost) {
						currentNode = openNodes.get(i);
					}
				}
			}
			
			openNodes.remove(currentNode);
			closedNodes.add(currentNode);
			
			if (currentNode.equals(endNode)) {
				this.nodes = map.retracePath(startNode, endNode);
				return;
			}
			
			for (PathNode neighbor : map.getNeighbors(currentNode)) {
				// add blocked congruent tiles 
				List<PathNode> immediateNeighbors = map.getImmediateNeighbors(map.getNodeGrid(), currentNode, neighbor);
				if (!neighbor.isWalkable() || closedNodes.contains(neighbor) || 
						(immediateNeighbors != null && (!immediateNeighbors.get(0).isWalkable() 
						|| !immediateNeighbors.get(1).isWalkable()))) {
					continue;
				}
				
				int newMovementCost = currentNode.gCost + map.getDistance(currentNode, neighbor);
				if (newMovementCost < neighbor.gCost || !openNodes.contains(neighbor)) {
					neighbor.gCost = newMovementCost;
					neighbor.hCost = map.getDistance(neighbor, endNode);
					neighbor.parent = currentNode;
					
					if (!openNodes.contains(neighbor)) {
						openNodes.add(neighbor);
					}
				}
			}
		}
	}
	
	/**
	 * Gets the number of tiles in the path.
	 */
	
	public int getLength() {
		return nodes.size();
	}
	
	public List<PathNode> getNodes() {
		return nodes;
	}
	
	/**
	 * Gets the node at the selected index.
	 * @param index The index of the desired node.
	 * @return The node at the selected index, <b>null</b> if there are less
	 * <br>nodes than the specified index.
	 */
	
	public PathNode getNode(int index) {
		return index < nodes.size() ? nodes.get(index) : null;
	}
	
	public void removeNode(int index) {
		nodes.remove(index);
	}
	
}
