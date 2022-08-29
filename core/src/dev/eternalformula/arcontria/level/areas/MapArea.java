package dev.eternalformula.arcontria.level.areas;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.level.GameLevel;
import dev.eternalformula.arcontria.level.maps.EFTiledMap;
import dev.eternalformula.arcontria.level.maps.EFMapRenderer;

public class MapArea {
	
	protected GameLevel level;
	protected EFMapRenderer mapRend;
	protected EFTiledMap map;
	
	protected Array<Entity> entities;
	
	public MapArea(GameLevel level, EFTiledMap map) {
		this.level = level;
		this.entities = new Array<Entity>();
		this.map = map;
		this.mapRend = new EFMapRenderer();
	}
	
	public void update(float delta) {
		
	}
	
	public void draw(SpriteBatch batch, float delta) {
		
	}

}
