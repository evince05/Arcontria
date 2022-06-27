package dev.eternalformula.arcontria.level;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.entity.hostile.Skeleton;
import dev.eternalformula.arcontria.entity.npc.NPC;
import dev.eternalformula.arcontria.entity.player.Player;
import dev.eternalformula.arcontria.level.maps.EFMapObject;
import dev.eternalformula.arcontria.level.maps.Map;
import dev.eternalformula.arcontria.level.maps.MapRenderer;
import dev.eternalformula.arcontria.level.maps.TemplateTmxMapLoader;
import dev.eternalformula.arcontria.util.EFDebug;

public class TestLevel extends GameLevel {
	
	private float viewportWidth;
	private float viewportHeight;
	
	private Sound waveSfx;
	
	public TestLevel(GameScene scene) {
		super(scene);
		
		//this.map = new Map(this, new TmxMapLoader(this).load("data/levels/maps/dojo/dojo.tmx"));
		TemplateTmxMapLoader mapLoader = new TemplateTmxMapLoader(this);
		
		TiledMap tiledMap = mapLoader.load("maps/data/forest/level_1-1.tmx");
		Array<EFMapObject> objects = mapLoader.getMapObjects();
		
		Array<org.locationtech.jts.geom.Polygon> polygons = mapLoader.getNavmeshPolygons();
		
		this.map = new Map(this, tiledMap, polygons, objects);
		
		this.mapRenderer = new MapRenderer(map);
		
		this.viewportWidth = scene.getViewport().getWorldWidth();
		this.viewportHeight = scene.getViewport().getWorldHeight();
		
		this.waveSfx = Gdx.audio.newSound(Gdx.files.internal("sfx/ambient/waves.wav"));
		waveSfx.loop();
		
		/*
		Dummy dummy = new Dummy(this);
		dummy.setLocation(7.5f, 7f);
		entities.add(dummy);
		
		Dummy dummy1 = new Dummy(this);
		dummy1.setLocation(3.5f, 5f);
		entities.add(dummy1);
		
		Dummy dummy2 = new Dummy(this);
		dummy2.setLocation(11.5f, 5f);
		entities.add(dummy2);
		*/
		
		
		player = Player.create(this, "Elliott", UUID.randomUUID());
		
		player.setLocation(new Vector2(7.5f, 10.5f)); // dojo: 7.5f, 1.5f
		player.setDirection(1);
		
		Skeleton skele = new Skeleton(this);
		entities.add(skele);
		
		//NPC npc = new NPC(this);
		//entities.add(npc);
		
		//Skeleton skeleton = new Skeleton(this);
		//entities.add(skeleton);
		
		setupCamera();
			
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		
		Vector2 cameraPos = new Vector2(scene.getViewport().getCamera().position.x,
				scene.getViewport().getCamera().position.y);
		
		Vector2 playerPos = new Vector2(player.getLocation().x,
				player.getLocation().y);
		
		//System.out.println("Player Pos: " + Strings.vec2(playerPos));
		
		
		//System.out.println(playerPos.x + 0.5f);
		if (playerPos.x >= viewportWidth / 2f - 0.5f && playerPos.x <= map.getWidth() - viewportWidth / 2f - 0.5f) {
			cameraPos.x = playerPos.x + 0.5f;
		}
		
		if (playerPos.y >= viewportHeight / 2f - 1f && playerPos.y <= map.getHeight() - viewportHeight / 2f - 1f) {
			cameraPos.y = playerPos.y + 1f;
		}
		
		//cameraPos.x = playerPos.x + 0.5f;
		//cameraPos.y = playerPos.y + 1f;
		
		//System.out.println("Camera Pos: " + Strings.vec2(cameraPos));
		
		scene.getViewport().getCamera().position.set(cameraPos, 0f);
		
		player.handleInput(delta);
		
		// Handle entity clearing.
		clearRemovedEntities();
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_9)) {
			daylightHandler.setWorldTime(2437.5f);
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8)) {
			daylightHandler.setWorldTime(812.5f);
		}
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
		super.draw(batch, delta);
	}

	@Override
	public void resize(int width, int height) {
		mapRenderer.resize(width, height);
	}

	@Override
	public void dispose() {
		mapRenderer.dispose();
	}
	
	private void setupCamera() {
		
		// Calculates and sets the proper location of the camera.
		Vector2 playerPos = player.getLocation();
		Vector2 cameraPos = new Vector2();
		
		if (map.getWidth() <= viewportWidth) {
			EFDebug.debug("Watch TestLevel.java:setupCamera... line cameraPosX = map.getWidth() / 2f");
			cameraPos.x = map.getWidth() / 2f;
		}
		else {
			float centerX = viewportWidth / 2f - 0.5f;
			if (playerPos.x < centerX) {
				cameraPos.x = viewportWidth / 2f;
			}
			else if (playerPos.x > map.getWidth() - centerX) {
				cameraPos.x = map.getWidth() - viewportWidth / 2f + 0.5f;
			}
			else {
				cameraPos.x = playerPos.x;
			}
		}
		
		if (map.getHeight() <= viewportHeight) {
			cameraPos.y = map.getHeight() / 2f + 0.5f;
		}
		else {
			float centerY = viewportHeight / 2f - 1f;
			
			if (playerPos.y < centerY) {
				cameraPos.y = viewportHeight / 2f;
			}
			else if (playerPos.y > map.getHeight() - centerY) {
				cameraPos.y = map.getHeight() - viewportHeight / 2f + 1f;
			}
			else {
				cameraPos.y = playerPos.y;
			}
		}
		
		scene.getViewport().getCamera().position.set(cameraPos, 0f);
	}
}
