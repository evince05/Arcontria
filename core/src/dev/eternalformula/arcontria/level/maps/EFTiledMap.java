package dev.eternalformula.arcontria.level.maps;

import java.util.Comparator;

import org.locationtech.jts.geom.Polygon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;

import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.pathfinding.Navmesh;

public class EFTiledMap {

	private int width;
	private int height;
	
	private TiledMap tiledMap;
	
	private Array<EFMapObject> mapObjects;
	private Array<Entity> mapEntities;
	
	private Navmesh navmesh;
	
	public EFTiledMap(TiledMap tiledMap, Array<EFMapObject> mapObjects, Array<Polygon> polygons,
			Array<Entity> mapEntities) {
		this.tiledMap = tiledMap;
		this.mapObjects = mapObjects;
		this.mapEntities = mapEntities;
		
		this.width = tiledMap.getProperties().get("width", Integer.class);
		this.height = tiledMap.getProperties().get("height", Integer.class);
		
		this.navmesh = new Navmesh(this, polygons);
		zSort();
	}
	
	public void load() {
		// maybe
	}
	
	public <T> T getProperty(String property, Class<T> clazz) {
		return tiledMap.getProperties().get(property, clazz);
	}
	
	public void update(float delta) {
		for (EFMapObject mapObj : mapObjects) {
			mapObj.update(delta);
		}
	}
	
	public void drawMapObjects(SpriteBatch batch, float delta) {
		for (EFMapObject mapObj : mapObjects) {
			mapObj.draw(batch, delta);
		}
	}
	
	public TiledMap getMap() {
		return tiledMap;
	}
	
	public Array<Entity> getMapEntities() {
		return mapEntities;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void dispose() {
		tiledMap.dispose();
	}
	
	private void zSort() {
		Sort.instance().sort(mapObjects, new EFMapComparator());
	}
	
	public static class EFMapComparator implements Comparator<EFMapObject> {

		@Override
		public int compare(EFMapObject obj1, EFMapObject obj2) {
			return Integer.compare(Math.round(obj2.getY()), Math.round(obj1.getY()));
		}
		
	}
}
