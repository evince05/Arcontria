package dev.eternalformula.arcontria.level.areas;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.level.maps.EFMapRenderer;
import dev.eternalformula.arcontria.level.maps.EFTiledMap;

public class MapArea {
	
	protected GameLevel level;
	protected EFMapRenderer mapRend;
	protected EFTiledMap map;
	
	protected List<Entity> entities;
	private List<Entity> entitiesToAdd;
	private List<Entity> entitiesToRemove;
	
	public MapArea(GameLevel level, EFTiledMap map) {
		this.level = level;
		this.entities = new ArrayList<Entity>();
		this.entitiesToAdd = new ArrayList<Entity>();
		this.entitiesToRemove = new ArrayList<Entity>();
		this.map = map;
		this.mapRend = new EFMapRenderer();
		
		// Entities from map
		for (Entity e : map.getMapEntities()) {
			entities.add(e);
		}
	}
	
	public void update(float delta) {
		entitiesToAdd.forEach(e -> {
			entities.add(e);
		});
		entitiesToAdd.clear();
		
		entities.forEach(e -> {
			e.update(delta);
		});
		
		entitiesToRemove.forEach(e -> {
			entities.remove(e);
			e.destroyBodies(level.getWorld());
		});
		entitiesToRemove.clear();
	}
	
	public void draw(SpriteBatch batch, float delta) {
		
		mapRend.setTiledMap(map);
		mapRend.draw(batch, delta);
		
		entities.forEach(e -> {
			e.draw(batch, delta);
		});
	}
	
	public void dispose() {
		mapRend.dispose();
		map.dispose();
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	
	public void addEntity(Entity e) {
		entitiesToAdd.add(e);
	}
	
	public void removeEntity(Entity e) {
		entitiesToRemove.add(e);
	}
	
	public EFTiledMap getMap() {
		return map;
	}
	
	public float getMapWidth() {
		return map.getWidth();
	}
	
	public float getMapHeight() {
		return map.getHeight();
	}
}
