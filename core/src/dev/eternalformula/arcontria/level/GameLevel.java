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
import dev.eternalformula.arcontria.ArcontriaGame;
import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.entity.hostile.bosses.UndeadProspector;
import dev.eternalformula.arcontria.entity.misc.Dummy;
import dev.eternalformula.arcontria.entity.projectiles.ProspectorPickaxe;
import dev.eternalformula.arcontria.gfx.lighting.DaylightHandler;
import dev.eternalformula.arcontria.gfx.particles.ParticleHandler;
import dev.eternalformula.arcontria.inventory.InventoryHandler;
import dev.eternalformula.arcontria.level.areas.MineArea;
import dev.eternalformula.arcontria.level.maps.EFMapRenderer;
import dev.eternalformula.arcontria.level.maps.EFTiledMap;
import dev.eternalformula.arcontria.scenes.GameSession;
import dev.eternalformula.arcontria.util.Assets;
//import dev.eternalformula.arcontria.scenes.GameSession;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.Strings;

public class GameLevel {
	
	private GameSession session;
	
	protected List<Entity> entities;
	private List<Entity> entitiesToAdd;
	private List<Entity> entitiesToRemove;
	
	protected EFTiledMap map;
	protected EFMapRenderer mapRenderer;
	
	protected DaylightHandler daylightHandler;
	protected ParticleHandler particleHandler;
	
	protected Box2DDebugRenderer b2dr;
	
	protected InventoryHandler inventoryHandler;
	
	protected float timeDebugAccumulator;
	private Music music;
	
	private boolean debugEnabled;
	private Dummy dummy;
	private UndeadProspector prospector;
	
	private MineArea mineArea;
	
	
	GameLevel(GameSession session) {
		
		this.session = session;
		this.entities = new ArrayList<Entity>();
		this.entitiesToAdd = new ArrayList<Entity>();
		this.entitiesToRemove = new ArrayList<Entity>();
		this.daylightHandler = new DaylightHandler(this);
		this.particleHandler = new ParticleHandler(this);
		this.inventoryHandler = new InventoryHandler(this, null);
		
		this.b2dr = new Box2DDebugRenderer();
		b2dr.setDrawInactiveBodies(false);
		
		this.music = Gdx.audio.newMusic(Gdx.files.internal("music/alpha.mp3"));
		music.setVolume(0.35f);
		
		this.mapRenderer = new EFMapRenderer();
		//this.map = Assets.get("maps/data/dojo/dojo.tmx", EFTiledMap.class);
		this.map = Assets.get("maps/data/mines/mine-level-1.tmx", EFTiledMap.class);
		
		for (Entity e : map.getMapEntities()) {
			addEntity(e);
		}
		
		this.dummy = new Dummy(session.getGameScene().getWorld(), this);
		dummy.setLocation(new Vector2(7.5f, 2f));
		//entities.add(dummy);
		
		this.prospector = new UndeadProspector(this, new Vector2(7.5f, 9f));
		//entities.add(prospector);
		
		//session.getGameCamera().position.set(dummy.getLocation().x + 0.5f, dummy.getLocation().y + 6f, 0f);
		
		this.mineArea = new MineArea(this, map);
		
	}
	
	public static GameLevel load(GameSession session, String saveFolder) {
		GameLevel level = new GameLevel(session);
		
		return level;
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	
	public void addEntity(Entity entity) {
		entitiesToAdd.add(entity);
	}
	
	/**
	 * Removes an entity from the GameLevel.
	 * <br>{@link GameLevel#clearRemovedEntities()} must be called for
	 * <br>the entity to actually be removed.
	 * @param entity The entity to be removed.
	 */
	
	
	public void removeEntity(Entity entity) {
		entitiesToRemove.add(entity);
	}
	
	private void addEntities() {
		for (Entity e : entitiesToAdd) {
			entities.add(e);
		}
		entitiesToAdd.clear();
	}
	
	public void clearRemovedEntities() {
		for (Entity e : entitiesToRemove) {
			entities.remove(e);
			
			if (e instanceof ProspectorPickaxe) {
				((ProspectorPickaxe) e).destroyBody(ArcontriaGame.getCurrentScene().getWorld());
			}
		}
		
		entitiesToRemove.clear();
	}
	
	public EFTiledMap getMap() {
		return map;
	}
	
	public World getWorld() {
		return session.getGameScene().getWorld();
	}
	
	public RayHandler getRayHandler() {
		return session.getGameScene().getRayHandler();
	}
	
	public void setMap(EFTiledMap map) {
		this.map = map;
		mapRenderer.setTiledMap(map);
	}
	
	public Box2DDebugRenderer getDebugRenderer() {
		return b2dr;
	}
	
	public EFMapRenderer getMapRenderer() {
		return mapRenderer;
	}
	
	public DaylightHandler getDaylightHandler() {
		return daylightHandler;
	}
	
	public ParticleHandler getParticleHandler() {
		return particleHandler;
	}
	
	public void resize(int width, int height) {
	}
	
	public void dispose() {
		music.dispose();
		b2dr.dispose();
		particleHandler.dispose();
	}
	
	public void update(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			session.getGameCamera().position.y += 1/4f;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			session.getGameCamera().position.y -= 1/4f;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			session.getGameCamera().position.x -= 1/4f;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			session.getGameCamera().position.x += 1/4f;
		}	
		
		addEntities();
		
		//daylightHandler.update();
		particleHandler.update(delta);
		
		if (inventoryHandler.isInventoryOpen()) {
			inventoryHandler.update(delta);
		}
		
		mineArea.update(delta);
		
		//Vector2 pos = session.centerCamera(session.getPlayer());
		//session.getGameCamera().position.set(7.5f, 7f, 0f);
		
		
		//mapRenderer.update(delta);
		
		for (Entity e : entities) {
			e.update(delta);
			
			if (e instanceof ProspectorPickaxe){
			
				if (((ProspectorPickaxe) e).isFinished()) {
					System.out.println("Removing entity");
					removeEntity(e);
				}
			}
		}
		
		// Lights
		getRayHandler().update();
		/*
		timeDebugAccumulator += delta;
		if (timeDebugAccumulator >= 1f) {
			timeDebugAccumulator -= 1f;
			EFDebug.info("World Time: " + daylightHandler.getFormattedWorldTime()
				+ " (running " + Gdx.graphics.getFramesPerSecond() + "FPS)");
			EFDebug.debug("Physics Body Count: " + world.getBodyCount());
			EFDebug.info("Camera Pos: " + Strings.vec3(session.getGameCamera().position));
		}*/
		
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
		
		//System.out.println("Cam Pos: " + Strings.vec3(session.getGameCamera().position));
		clearRemovedEntities();
		
	}
	
	public void draw(SpriteBatch batch, float delta) {
		//daylightHandler.draw();a
		//mapRenderer.draw(batch, delta);
		
		/*
		Player player = session.getPlayer();
		
		if (player.shouldRenderBeforeEntities()) {
			player.draw(batch, delta);
		}
		
		// Draw MapObjects.
		//mapRenderer.drawMapObjects(batch, delta);
		
		if (!player.shouldRenderBeforeEntities()) {
			player.draw(batch, delta);
		}*/
		
		mapRenderer.setTiledMap(map);
		mapRenderer.draw(batch, delta);
		
		mineArea.draw(batch, delta);
		
		for (Entity e : entities) {
			e.draw(batch, delta);
		}
		
		batch.end();
		getRayHandler().render();
		batch.begin();
		
		particleHandler.draw(batch, delta);
		
		/*
		
		
		
		if (this.isDebugEnabled()) {
		}
		
		// TODO: entity drawing should go here
		*/
		if (EFDebug.debugBox2D) {
			batch.end();
			b2dr.render(ArcontriaGame.getCurrentScene().getWorld(), session.getGameCamera().combined);
			batch.begin();
		}
		
	}
	
	public void drawUi(SpriteBatch batch, float delta) {
		batch.begin();
		
		if (inventoryHandler.isInventoryOpen()) {
			inventoryHandler.draw(batch, delta);
		}
		batch.end();
	}
	
	public void onMouseClicked(int x, int y, int button) {
	}
	
	public boolean isDebugEnabled() {
		return debugEnabled;
	}
	
	public GameSession getSession() {
		return session;
	}
}
