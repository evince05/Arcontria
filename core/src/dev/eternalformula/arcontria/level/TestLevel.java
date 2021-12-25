package dev.eternalformula.arcontria.level;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.entity.hostile.Skeleton;
import dev.eternalformula.arcontria.entity.player.Player;
import dev.eternalformula.arcontria.gfx.particles.DamageTextParticle;
import dev.eternalformula.arcontria.level.maps.Map;
import dev.eternalformula.arcontria.level.maps.MapRenderer;
import dev.eternalformula.arcontria.util.EFConstants;
import dev.eternalformula.arcontria.util.Strings;

public class TestLevel extends GameLevel {
	
	private Player player;
	
	private float viewportWidth;
	private float viewportHeight;
	
	private DamageTextParticle dtp;
	
	public TestLevel(GameScene scene) {
		super(scene);
		player = Player.create("Elliott", UUID.randomUUID());
		entities.add(player);
		
		this.map = new Map(this, new TmxMapLoader().load("data/levels/maps/map.tmx"));
		this.mapRenderer = new MapRenderer(map);
		this.viewportWidth = scene.getViewport().getWorldWidth();
		this.viewportHeight = scene.getViewport().getWorldHeight();
	
		//entities.add(new Skeleton());
		
		this.dtp = new DamageTextParticle(new Vector2(20f, 15f), 100);
		
		player.setLocation(new Vector2(4, 17.375f));
		setupCamera();
			
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		
		dtp.update(delta);
		
		for (Entity e : entities) {
			e.update(delta);
		}
		
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
		
		map.getLevel().getScene().getViewport().getCamera().position.set(cameraPos, 0f);
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
		
		mapRenderer.draw(batch);
		
		if (this.isDebugEnabled()) {
			batch.end();
			map.draw(delta);
			batch.begin();
		}
		
		for (Entity e : entities) {
			e.draw(batch, delta);
		}
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
		
		float centerX = viewportWidth / 2f - 0.5f;
		float centerY = viewportHeight / 2f - 1f;
		
		if (playerPos.x < centerX) {
			cameraPos.x = viewportWidth / 2f;
		}
		else if (playerPos.x > map.getWidth() - centerX) {
			cameraPos.x = map.getWidth() - viewportWidth / 2f + 0.5f;
		}
		else {
			cameraPos.x = playerPos.x;
		}
		
		if (playerPos.y < centerY) {
			cameraPos.y = viewportHeight / 2f;
		}
		else if (playerPos.y > map.getHeight() - centerY) {
			cameraPos.y = map.getHeight() - viewportHeight / 2f + 1f;
		}
		else {
			cameraPos.y = playerPos.y;
		}
		
		System.out.println("Camera Pos: " + Strings.vec2(cameraPos.x, cameraPos.y));
		System.out.println(map.getHeight() - viewportHeight / 2f);
		scene.getViewport().getCamera().position.set(cameraPos, 0f);
	}
}
