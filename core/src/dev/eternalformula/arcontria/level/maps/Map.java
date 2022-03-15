package dev.eternalformula.arcontria.level.maps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.pathfinding.PathNode;

public class Map {
	
	private GameLevel level;
	private TiledMap map;
	
	private int mapWidth;
	private int mapHeight;
	
	private ShapeRenderer debugRenderer;
	
	/**
	 * A 2D Array of all path nodes (tiles).
	 */
	
	private PathNode[][] nodeGrid;
	
	public Map(GameLevel level, TiledMap map) {
		this.level = level;
		this.map = map;
		
		this.debugRenderer = new ShapeRenderer();
		
		this.mapWidth = map.getProperties().get("width", int.class);
		this.mapHeight = map.getProperties().get("height", int.class);
		Map.zSort(map);
		
		this.nodeGrid = new PathNode[(int) mapHeight][(int) mapWidth];
		for (int row = 0; row < mapHeight; row++) {
			for (int col = 0; col < mapWidth; col++) {
				nodeGrid[row][col] = new PathNode(true, new Vector2(col, row), col, row);
			}
		}
		
		MapUtil.parseTiledObjectLayer(level.getWorld(), map.getLayers().get("Collisions").getObjects());
		
		// Create Lights
	}
	
	public void draw(float delta) {
		debugRenderer.setProjectionMatrix(level.getScene().getViewport().getCamera().combined);
		debugRenderer.setAutoShapeType(true);
		debugRenderer.setColor(Color.RED);
		debugRenderer.begin(ShapeType.Line);
		
		Vector2 loc = level.getEntities().get(1).getLocation();
		debugRenderer.rect(loc.x, loc.y, 1 / 16f, 1 / 16f);
		for (int row = 0; row < mapHeight; row++) {
			for(int col = 0; col < mapWidth; col++) {
				if (!nodeGrid[row][col].isWalkable()) {
					debugRenderer.rect(col, row, 1f, 1f);
				}
			}
		}
		debugRenderer.end();
	}
	
	public static void zSort(TiledMap map) {
		
		for (MapLayer layer : map.getLayers()) {
			if (layer.getObjects().getCount() > 0) {
				
				Array<TextureMapObject> texObjs = layer.getObjects().getByType(TextureMapObject.class);
				Sort.instance().sort(texObjs, new MapComparator());
				
				List<MapObject> objectsToRemove = new ArrayList<>();
				for (int i = 0; i < texObjs.size; i++) {
					objectsToRemove.add(texObjs.get(i));
				}
				for (MapObject mapObj : objectsToRemove) {
					layer.getObjects().remove(mapObj);
				}
				
				for (int i = 0; i < texObjs.size; i++) {
					layer.getObjects().add(texObjs.get(i));
				}
			}
		}
	}
	
	/**
	 * Returns a list of immediate neighbors between the two nodes.
	 * <br><t>This should only be used when the two tiles are diagonally adjacent to one another.
	 * @param startNode The start node.
	 * @param endNode The end node.
	 * @return A list of immediate neighbors between the two nodes.
	 */
	
	public List<PathNode> getImmediateNeighbors(PathNode[][] nodeGrid, PathNode startNode, PathNode endNode) {
		int distanceX = endNode.gridX - startNode.gridX;
		int distanceY = endNode.gridY - startNode.gridY;
		
		if (Math.abs(distanceX) != 1 || Math.abs(distanceY) != 1) {
			return null;
		}
		
		List<PathNode> nodes = new ArrayList<>();
		
		if (distanceX == 1) {
			nodes.add(getNode(startNode.gridX + 1, startNode.gridY));
		}
		else if (distanceX == -1) {
			nodes.add(getNode(startNode.gridX - 1, startNode.gridY));
		}
		
		if (distanceY == 1) {
			nodes.add(getNode(startNode.gridX, startNode.gridY + 1));
		}
		else if (distanceY == -1) {
			nodes.add(getNode(startNode.gridX, startNode.gridY - 1));
		}
		
		return nodes;
	}
	
	/**
	 * Gets a list of all neighbors of the specified node.
	 * @param nodeGrid The NodeGrid to be searched
	 * @param node The node to be searched.
	 * @return A list of all neighbors of the node.
	 */
	
	public List<PathNode> getNeighbors(PathNode node) {
		List<PathNode> neighbors = new ArrayList<>();
		
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (x == 0 && y == 0) {
					continue;
				}
				
				int checkX = node.gridX + x;
				int checkY = node.gridY + y;
				
				if (checkX >= 0 && checkX < mapWidth
						&& checkY >= 0 && checkY < mapHeight) {
					neighbors.add(nodeGrid[checkY][checkX]);
				}
			}
		}
		return neighbors;
	}
	
	public List<PathNode> retracePath(PathNode startNode, PathNode endNode) {
		List<PathNode> path = new ArrayList<PathNode>();
		PathNode currentNode = endNode;
		
		while (!currentNode.equals(startNode)) {
			path.add(currentNode);
			currentNode = currentNode.parent;
		}
		Collections.reverse(path);
		return path;
	}
	
	public int getDistance(PathNode nodeA, PathNode nodeB) {
		int distanceX = Math.abs(nodeA.gridX - nodeB.gridX);
		int distanceY = Math.abs(nodeA.gridY - nodeB.gridY);
		
		/**
		 * Move cost: Diagonal -> 14
		 * Horizontal/Vertical -> 10
		 */
		if (distanceX > distanceY) {
			return 14 * distanceY + 10 * (distanceX - distanceY);
		}
		else {
			return 14 * distanceX + 10 * (distanceY - distanceX);
		}
	}
	
	public PathNode getNode(int x, int y) {
		return nodeGrid[y][x];
	}
	
	public PathNode[][] getNodeGrid() {
		return nodeGrid;
	}
	
	public GameLevel getLevel() {
		return level;
	}
	
	public TiledMap getMap() {
		return map;
	}
	
	public int getWidth() {
		return mapWidth;
	}
	
	public int getHeight() {
		return mapHeight;
	}

	/**
	 * Blocks the tile on the grid at [x, y].
	 * @param x The x grid location of the tile.
	 * @param y The y grid location of the tile.
	 */
	
	public void blockTile(int x, int y) {
		nodeGrid[y][x].setWalkable(false);
	}
	
	/**
	 * Determines if the specified tile is walkable.
	 * @param x The x grid location of the tile
	 * @param y The y grid location of the tile
	 * @return True if the tile is walkable, otherwise false.
	 */
	
	public boolean isTileWalkable(int x, int y) {
		return getNode(x, y).isWalkable();
	}
	
	public static class MapComparator implements Comparator<TextureMapObject> {

		@Override
		public int compare(TextureMapObject obj1, TextureMapObject obj2) {
			return Integer.compare(Math.round(obj2.getY()), Math.round(obj1.getY()));
		}
		
	}

}
