package dev.eternalformula.arcontria.pathfinding;

import com.badlogic.gdx.math.Vector2;

public class PathNode {
	
	private boolean walkable;
	private Vector2 worldPos;
	
	public int gCost;
	public int hCost;
	public int gridX;
	public int gridY;
	
	public PathNode parent;
	
	public PathNode(boolean walkable, Vector2 worldPos, int gridX, int gridY) {
		this.walkable = walkable;
		this.worldPos = worldPos;
		this.gridX = gridX;
		this.gridY = gridY;
	}
	
	public int fCost() {
		return gCost + hCost;
	}
	
	public Vector2 getPosition() {
		return worldPos;
	}
	
	public boolean isWalkable() {
		return walkable;
	}
	
	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}

}
