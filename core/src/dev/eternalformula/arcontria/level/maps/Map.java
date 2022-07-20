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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;

import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.pathfinding.Navmesh;
import dev.eternalformula.arcontria.pathfinding.PathNode;

public class Map {
	
	private GameLevel level;
	private TiledMap map;
	
	private int mapWidth;
	private int mapHeight;
	
	private ShapeRenderer debugRenderer;
	private Array<EFMapObject> mapObjects;
	private Navmesh navmesh;
	
	
	public Map(GameLevel level, TiledMap map, Array<org.locationtech.jts.geom.Polygon> navmeshPolygons,
			Array<EFMapObject> mapObjects) {
		this.level = level;
		this.map = map;
		
		this.debugRenderer = new ShapeRenderer();
		
		this.mapWidth = map.getProperties().get("width", int.class);
		this.mapHeight = map.getProperties().get("height", int.class);
		
		this.mapObjects = mapObjects;
		
		Map.zSort(map);
		
		navmesh = new Navmesh(map, navmeshPolygons);
		
		/*
		this.nodeGrid = new PathNode[(int) mapHeight][(int) mapWidth];
		for (int row = 0; row < mapHeight; row++) {
			for (int col = 0; col < mapWidth; col++) {
				nodeGrid[row][col] = new PathNode(true, new Vector2(col, row), col, row);
			}
		}*/
				
		if (map.getLayers().get("Collisions") != null) {
			//MapUtil.parseTiledObjectLayer(level.getWorld(), map.getLayers().get("Collisions").getObjects());
		}
		// Create Lights
	}
	
	/**
	 * Draws the underlying navmesh of the map.
	 * @param delta The time that has elapsed since the last frame.
	 */
	
	public void draw(float delta) {
		//debugRenderer.setProjectionMatrix(level.getScene().getViewport().getCamera().combined);
		debugRenderer.setAutoShapeType(true);
		debugRenderer.setColor(Color.RED);
		debugRenderer.begin(ShapeType.Line);
		
		navmesh.draw(debugRenderer);
		debugRenderer.end();
	}
	
	/**
	 * Sorts the MapObjects from .tmx files so they appear correctly.
	 * @param map The TiledMap to be sorted.
	 */
	
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
	
	public GameLevel getLevel() {
		return level;
	}
	
	public Navmesh getNavmesh() {
		return navmesh;
	}
	
	public TiledMap getMap() {
		return map;
	}
	
	public Array<EFMapObject> getMapObjects() {
		return mapObjects;
	}
	
	public int getWidth() {
		return mapWidth;
	}
	
	public int getHeight() {
		return mapHeight;
	}
	
	public ShapeRenderer getDebugRenderer() {
		return debugRenderer;
	}

	public static class MapComparator implements Comparator<TextureMapObject> {

		@Override
		public int compare(TextureMapObject obj1, TextureMapObject obj2) {
			return Integer.compare(Math.round(obj2.getY()), Math.round(obj1.getY()));
		}
		
	}

}
