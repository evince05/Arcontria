package dev.eternalformula.arcontria.level.maps;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;

import dev.eternalformula.arcontria.level.maps.Map.MapComparator;

public class EFTiledMap {
	
	private TiledMap tiledMap;
	
	private Array<EFMapObject> mapObjects;
	
	public EFTiledMap(TiledMap tiledMap, Array<EFMapObject> mapObjects) {
		this.tiledMap = tiledMap;
		this.mapObjects = mapObjects;
		zSort();
	}
	
	public void load() {
		// maybe
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
	
	public void dispose() {
		
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
