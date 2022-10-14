package dev.eternalformula.arcontria.ui.charcreator;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.level.maps.EFMapRenderer;
import dev.eternalformula.arcontria.level.maps.EFTiledMap;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.Strings;

/**
 * A moving tilemap background for the game menus.
 * @author EternalFormula
 */

public class MenuMapBackground {
	
	private EFTiledMap tiledMap;
	private EFMapRenderer mapRend;
	
	private Vector2 targetPos;
	private Vector2 currentPos;
	
	private float minCamX;
	private float maxCamX;
	private float minCamY;
	private float maxCamY;
	
	private static final float MOVE_SPEED = 0.5f;
	
	public MenuMapBackground(String mapFile) {
		tiledMap = Assets.get(mapFile, EFTiledMap.class);
		this.mapRend = new EFMapRenderer();
		mapRend.setTiledMap(tiledMap);
		
		// Camera Bounds
		MapProperties mapProps = tiledMap.getMap().getProperties();
		minCamX = 10f;
		minCamY = 5.625f;
		
		maxCamX = mapProps.get("width", Integer.class) - minCamX;
		maxCamY = mapProps.get("height", Integer.class) - minCamY;
		
		this.currentPos = getRandomMapCoords();
		this.targetPos = getRandomMapCoords();
	}
	
	public void update(float delta) {
		tiledMap.update(delta);
		
		if (targetPos.x == currentPos.x && targetPos.y == currentPos.y) {
			targetPos = getRandomMapCoords();
			return;
		}
		
		/*
		 * Reused code from PathUtil#moveToTarget.
		 */
		
		float distanceX = targetPos.x - currentPos.x;
		float distanceY = targetPos.y - currentPos.y;
		
		float directionX = Math.signum(distanceX);
		float horizontalVelocity = directionX * MOVE_SPEED;
		
		float directionY = Math.signum(distanceY);
		float verticalVelocity = directionY * MOVE_SPEED;
		
		distanceX = Math.abs(distanceX);
		distanceY = Math.abs(distanceY);
		
		if (Math.abs(distanceY - distanceX) < 4 / 16f) {
			/*
			 * Distances are close enough to each other.
			 * The entity moves on both axes so it doesn't stutter.
			 */	
			
			currentPos.x += (horizontalVelocity / (float) Math.sqrt(2)) * delta;
			currentPos.y += (verticalVelocity / (float) Math.sqrt(2)) * delta;
		}
		else if (Math.abs(distanceX) >= Math.abs(distanceY)) {
			// The entity moves on the X axis.
			currentPos.x += horizontalVelocity * delta;
		}
		else {
			// The entity moves on the Y axis.
			currentPos.y += verticalVelocity * delta;
		}
		
		float absRemainX = Math.abs(targetPos.x - currentPos.x);
		float absRemainY = Math.abs(targetPos.y - currentPos.y);
		
		if (absRemainX > 0 && absRemainX < 0.0125f) {
			currentPos.x = targetPos.x;
		}
		
		if (absRemainY > 0 && absRemainY < 0.0125f) {
			currentPos.y = targetPos.y;
		}
	}
	
	public void draw(SpriteBatch batch, float delta) {
		mapRend.draw(batch, delta); 
	}
	
	private Vector2 getRandomMapCoords() {
		Vector2 coords = new Vector2();
		
		// Both start and end are inclusive :)
		float x = MathUtils.random(minCamX, maxCamX);
		float y = MathUtils.random(minCamY, maxCamY);
		coords.set(x, y);
		
		return coords;
	}
	
	public Vector2 getCurrentPos() {
		return currentPos;
	}
}
