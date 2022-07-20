package dev.eternalformula.arcontria.level;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.RayHandler;
import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.entity.player.Player;
import dev.eternalformula.arcontria.entity.player.PlayerData;
import dev.eternalformula.arcontria.gfx.lighting.DaylightHandler;
import dev.eternalformula.arcontria.gfx.particles.ParticleHandler;
import dev.eternalformula.arcontria.inventory.InventoryHandler;
import dev.eternalformula.arcontria.level.maps.Map;
import dev.eternalformula.arcontria.level.maps.MapRenderer;
import dev.eternalformula.arcontria.physics.WorldContactListener;
//import dev.eternalformula.arcontria.scenes.GameSession;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.Strings;

public class GameLevel {
	
	//private GameSession session;
	
	protected List<Entity> entities;
	private List<Entity> entitiesToRemove;
	
	protected Map map;
	protected MapRenderer mapRenderer;
	
	protected DaylightHandler daylightHandler;
	protected ParticleHandler particleHandler;
	
	protected World world;
	protected Box2DDebugRenderer b2dr;
	protected RayHandler rayHandler;
	
	protected InventoryHandler inventoryHandler;
	
	protected float timeDebugAccumulator;
	private Music music;
	
	private boolean debugEnabled;
}
	
	/*
	GameLevel(GameSession session) {
		
		this.session = session;
		this.entities = new ArrayList<Entity>();
		this.entitiesToRemove = new ArrayList<Entity>();
		this.daylightHandler = new DaylightHandler(this);
		this.particleHandler = new ParticleHandler(this);
		this.inventoryHandler = new InventoryHandler(this, null);
		
		// physics :)
		this.world = new World(new Vector2(0f, 0f), false);
		world.setContactListener(new WorldContactListener());
		
		this.rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(1.0f);
		this.b2dr = new Box2DDebugRenderer();
		b2dr.setDrawInactiveBodies(false);
		
		this.music = Gdx.audio.newMusic(Gdx.files.internal("music/alpha.mp3"));
		music.setVolume(0.35f);
		
		
	}
	
	public static GameLevel load(GameSession session, String saveFolder) {
		GameLevel level = new GameLevel(session);
		
		return level;
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	/**
	 * Removes an entity from the GameLevel.
	 * <br>{@link GameLevel#clearRemovedEntities()} must be called for
	 * <br>the entity to actually be removed.
	 * @param entity The entity to be removed.
	 */
	
	/*
	public void removeEntity(Entity entity) {
		entitiesToRemove.add(entity);
	}
	
	public void clearRemovedEntities() {
		for (Entity e : entitiesToRemove) {
			entities.remove(e);
		}
	}
	
	public Map getMap() {
		return map;
	}
	
	public void setMap(Map map) {
		this.map = map;
		mapRenderer.setMap(map);
	}
	
	public World getWorld() {
		return world;
	}
	
	public Box2DDebugRenderer getDebugRenderer() {
		return b2dr;
	}
	
	public MapRenderer getMapRenderer() {
		return mapRenderer;
	}
	
	public DaylightHandler getDaylightHandler() {
		return daylightHandler;
	}
	
	public ParticleHandler getParticleHandler() {
		return particleHandler;
	}
	
	public RayHandler getRayHandler() {
		return rayHandler;
	}
	
	public void resize(int width, int height) {
		mapRenderer.resize(width, height);
	}
	
	public void dispose() {
		music.dispose();
		world.dispose();
		b2dr.dispose();
		rayHandler.dispose();
		particleHandler.dispose();
	}
	
	public void setupCamera() {
		
		// Calculates and sets the proper location of the camera.
		Vector2 playerPos = session.getPlayer().getLocation();
		Vector2 cameraPos = new Vector2();
		
		float viewportWidth = session.getViewportWidth();
		float viewportHeight = session.getViewportHeight();
		
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
		
		session.getGameCamera().position.set(cameraPos, 0f);
	}
	
	public void update(float delta) {
		//daylightHandler.update();
		particleHandler.update(delta);
		world.step(1 / 60f, 6, 2);
		
		if (inventoryHandler.isInventoryOpen()) {
			inventoryHandler.update(delta);
		}
		
		Vector2 pos = session.centerCamera(session.getPlayer());
		session.getGameCamera().position.set(pos, 0f);
		
		//mapRenderer.update(delta);
		
		for (Entity e : entities) {
			e.update(delta);
		}
		
		// Lights
		rayHandler.update();
		rayHandler.setCombinedMatrix(session.getGameCamera());
		timeDebugAccumulator += delta;
		if (timeDebugAccumulator >= 1f) {
			timeDebugAccumulator -= 1f;
			EFDebug.info("World Time: " + daylightHandler.getFormattedWorldTime()
				+ " (running " + Gdx.graphics.getFramesPerSecond() + "FPS)");
			EFDebug.debug("Physics Body Count: " + world.getBodyCount());
			EFDebug.info("Camera Pos: " + Strings.vec3(session.getGameCamera().position));
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
			debugEnabled = !debugEnabled;
			EFDebug.debugEnabled = !EFDebug.debugEnabled;
			EFDebug.mapDebugEnabled = !EFDebug.mapDebugEnabled;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
			EFDebug.debugBox2D = !EFDebug.debugBox2D;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			inventoryHandler.toggle();
		}
		
	}
	
	public void draw(SpriteBatch batch, float delta) {
		//daylightHandler.draw();
		
		batch.begin();
		//mapRenderer.draw(batch, delta);
		
		Player player = session.getPlayer();
		
		if (player.shouldRenderBeforeEntities()) {
			player.draw(batch, delta);
		}
		
		// Draw MapObjects.
		//mapRenderer.drawMapObjects(batch, delta);
		
		if (!player.shouldRenderBeforeEntities()) {
			player.draw(batch, delta);
		}
		
		for (Entity e : entities) {
			e.draw(batch, delta);
		}
		
		
		
		particleHandler.draw(batch, delta);
		
		if (this.isDebugEnabled()) {
			batch.end();
			//map.draw(delta);
			batch.begin();
		}
		
		// TODO: entity drawing should go here
		
		batch.end();
		
		if (EFDebug.debugBox2D) {
			b2dr.render(world, session.getGameCamera().combined);
		}
		
		rayHandler.render();
	}
	
	public void drawUi(SpriteBatch batch, float delta) {
		batch.begin();
		
		if (inventoryHandler.isInventoryOpen()) {
			inventoryHandler.draw(batch, delta);
		}
		batch.end();
	}
	
	public boolean isDebugEnabled() {
		return debugEnabled;
	}
	
	public GameSession getSession() {
		return session;
	}
}*/
