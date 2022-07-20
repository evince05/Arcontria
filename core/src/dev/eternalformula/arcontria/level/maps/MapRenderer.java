package dev.eternalformula.arcontria.level.maps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.util.EFConstants;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.Strings;

public class MapRenderer {
	
	private Map map;
	private OrthogonalTiledMapRendererBleeding mapRenderer;
	
	public MapRenderer(Map map) {
		this.map = map;
		this.mapRenderer = new OrthogonalTiledMapRendererBleeding(map.getMap(), 1 / EFConstants.PPM);
	}
	
	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
		this.mapRenderer.setMap(map.getMap());
	}
	
	public void setColor(Color tintColor) {
		this.mapRenderer.getBatch().setColor(tintColor);
	}
	
	/**
	 * Updates the MapObjects within the map.
	 * @param delta
	 */
	public void update(float delta) {
		for (EFMapObject mapObj : map.getMapObjects()) {
			mapObj.update(delta);
		}
	}
	
	/**
	 * Draws the map (tiles only). See {@link MapRenderer#drawMapObjects(SpriteBatch, float)}<br>
	 * for drawing MapObjects.
	 * @param batch The SpriteBatch to be used.
	 */
	
	public void draw(SpriteBatch batch, float delta) {
		//mapRenderer.setView((OrthographicCamera) map.getLevel().getScene().getViewport().getCamera());
		mapRenderer.render();
	}
	
	public void drawMapObjects(SpriteBatch batch, float delta) {
		for (EFMapObject mapObj : map.getMapObjects()) {
			mapObj.draw(batch, delta);
		}
	}
	
	public void resize(int width, int height) {
	}
	
	public void dispose() {
	}
	
	public class EFMapRenderer extends OrthogonalTiledMapRenderer {

		public EFMapRenderer(TiledMap map) {
			super(map, 1 / EFConstants.PPM);
			System.out.println("Implement Z-Sort algorithm for TiledMapObjects");
		}
		
		@Override
		public void renderObject(MapObject object) {
			
			if (object instanceof TextureMapObject) {
				TextureMapObject texObj = (TextureMapObject) object;
				batch.draw(texObj.getTextureRegion(),
						(float) Math.floor(texObj.getX() / EFConstants.PPM), 
						(float) Math.floor(texObj.getY() / EFConstants.PPM), 
						texObj.getOriginX(), texObj.getOriginY(), 
						texObj.getTextureRegion().getRegionWidth() / EFConstants.PPM, 
						texObj.getTextureRegion().getRegionHeight() / EFConstants.PPM, 
						texObj.getScaleX(), texObj.getScaleY(), texObj.getRotation());
				
			}
		} 
		
	}

}
