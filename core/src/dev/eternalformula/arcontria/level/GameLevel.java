package dev.eternalformula.arcontria.level;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import box2dLight.RayHandler;
import dev.eternalformula.arcontria.entity.Entity;
import dev.eternalformula.arcontria.gfx.lighting.DaylightHandler;
import dev.eternalformula.arcontria.gfx.particles.Particle;
import dev.eternalformula.arcontria.gfx.particles.ParticleManager;
import dev.eternalformula.arcontria.level.maps.Map;
import dev.eternalformula.arcontria.level.maps.MapRenderer;
import dev.eternalformula.arcontria.util.EFConstants;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.Strings;

public abstract class GameLevel {
	
	protected GameScene scene;
	
	protected List<Entity> entities;
	private List<Entity> entitiesToRemove;
	
	protected Map map;
	protected MapRenderer mapRenderer;
	
	protected DaylightHandler daylightHandler;
	protected ParticleManager particleManager;
	
	protected World world;
	protected Box2DDebugRenderer b2dr;
	protected RayHandler rayHandler;
	
	protected float timeDebugAccumulator;
	private Music music;
	
	private boolean debugEnabled;
	
	public GameLevel(GameScene scene) {
		
		this.scene = scene;
		this.entities = new ArrayList<Entity>();
		this.entitiesToRemove = new ArrayList<Entity>();
		this.daylightHandler = new DaylightHandler(this);
		this.particleManager = new ParticleManager();
		
		// physics :)
		this.world = new World(new Vector2(0f, 0f), false);
		this.rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(1.0f);
		this.b2dr = new Box2DDebugRenderer();
		
		this.music = Gdx.audio.newMusic(Gdx.files.internal("music/alpha.mp3"));
		music.setVolume(0.35f);
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
	
	public void removeEntity(Entity entity) {
		entitiesToRemove.add(entity);
	}
	
	public void clearRemovedEntities() {
		for (Entity e : entitiesToRemove) {
			entities.remove(e);
		}
	}
	
	public void setMap(Map map) {
		this.map = map;
		mapRenderer.setMap(map);
	}
	
	public GameScene getScene() {
		return scene;
	}
	
	public World getWorld() {
		return world;
	}
	
	public MapRenderer getMapRenderer() {
		return mapRenderer;
	}
	
	public RayHandler getRayHandler() {
		return rayHandler;
	}
	
	public DaylightHandler getDaylightHandler() {
		return daylightHandler;
	}
	
	public abstract void resize(int width, int height);
	
	public void dispose() {
		music.dispose();
		world.dispose();
		b2dr.dispose();
		rayHandler.dispose();
	}
	
	public void update(float delta) {
		daylightHandler.update();
		particleManager.update(delta);
		world.step(1 / 60f, 6, 2);
		
		// Lights
		rayHandler.update();
		rayHandler.setCombinedMatrix((OrthographicCamera) scene.getViewport().getCamera());
		timeDebugAccumulator += delta;
		if (timeDebugAccumulator >= 1f) {
			timeDebugAccumulator -= 1f;
			System.out.println("[DEBUG] World Time: " + daylightHandler.getFormattedWorldTime()
				+ " (running " + Gdx.graphics.getFramesPerSecond() + "FPS)");
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
			
			if (!music.isPlaying()) {
				music.play();
			}
			else {
				music.pause();
			}
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
			debugEnabled = !debugEnabled;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
			EFDebug.debugBox2D = !EFDebug.debugBox2D;
		}
		
	}
	
	public void draw(SpriteBatch batch, float delta) {
		daylightHandler.draw();
		particleManager.draw(batch, delta);
		
		batch.begin();
		mapRenderer.draw(batch);
		
		if (this.isDebugEnabled()) {
			batch.end();
			map.draw(delta);
			batch.begin();
		}
		
		for (Entity e : entities) {
			e.draw(batch, delta);
		}
		batch.end();
		
		if (EFDebug.debugBox2D) {
			b2dr.render(world, scene.getViewport().getCamera().combined);
		}
		
		rayHandler.render();
	}
	
	public void spawnParticle(Particle p) {
		particleManager.spawnParticle(p);
	}
	
	public boolean isDebugEnabled() {
		return debugEnabled;
	}
}
