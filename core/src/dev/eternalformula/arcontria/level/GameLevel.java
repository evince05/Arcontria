package dev.eternalformula.arcontria.level;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import dev.eternalformula.arcontria.entity.player.Player;
import dev.eternalformula.arcontria.entity.projectiles.ProspectorPickaxe;
import dev.eternalformula.arcontria.gfx.lighting.DaylightHandler;
import dev.eternalformula.arcontria.gfx.particles.ParticleHandler;
import dev.eternalformula.arcontria.inventory.InventoryHandler;
import dev.eternalformula.arcontria.level.areas.MapArea;
import dev.eternalformula.arcontria.level.areas.MineArea;
import dev.eternalformula.arcontria.level.maps.EFMapRenderer;
import dev.eternalformula.arcontria.level.maps.EFTiledMap;
import dev.eternalformula.arcontria.scenes.GameSession;
import dev.eternalformula.arcontria.ui.hud.PlayerHUD;
import dev.eternalformula.arcontria.util.Assets;
//import dev.eternalformula.arcontria.scenes.GameSession;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.Strings;

public class GameLevel {
	
	private GameSession session;
	
	private Player player;
	private PlayerHUD playerHud;
	
	protected DaylightHandler daylightHandler;
	protected ParticleHandler particleHandler;
	
	protected Box2DDebugRenderer b2dr;
	
	protected InventoryHandler inventoryHandler;
	
	protected float timeDebugAccumulator;
	private Music music;
	
	private boolean debugEnabled;
	
	private MapArea mapArea;
	
	
	GameLevel(GameSession session) {
		
		this.session = session;
		this.daylightHandler = new DaylightHandler(this);
		this.particleHandler = new ParticleHandler(this);
		this.inventoryHandler = new InventoryHandler(this, null);
		
		this.b2dr = new Box2DDebugRenderer();
		b2dr.setDrawInactiveBodies(false);
		
		this.music = Gdx.audio.newMusic(Gdx.files.internal("music/alpha.mp3"));
		music.setVolume(0.35f);
		
		// load map from session entry point
		this.mapArea = new MineArea(this, Assets.get("maps/data/mines/mine-level-1.tmx", EFTiledMap.class));
		
		this.player = Player.create(this, "Elliott", UUID.randomUUID());
		player.setLocation(10.5f, 12f);
		session.getGameCamera().position.set(player.getLocation().x + 0.5f, player.getLocation().y + 1f, 0f);		
		
		playerHud = new PlayerHUD(player);
		playerHud.getClock().setDaylightHandler(daylightHandler);
		
	}
	
	public static GameLevel load(GameSession session, String saveFolder) {
		GameLevel level = new GameLevel(session);
		
		return level;
	}
	
	public World getWorld() {
		return session.getGameScene().getWorld();
	}
	
	public RayHandler getRayHandler() {
		return session.getGameScene().getRayHandler();
	}
	
	public MapArea getMapArea() {
		return mapArea;
	}
	
	public void setMapArea(MapArea mapArea) {
		this.mapArea = mapArea;
	}
	
	public Box2DDebugRenderer getDebugRenderer() {
		return b2dr;
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
		
		mapArea.update(delta);
		player.update(delta);
		
		session.getGameCamera().position.set(session.centerCamera(player), 0f);
		
		daylightHandler.update();
		particleHandler.update(delta);
		
		if (inventoryHandler.isInventoryOpen()) {
			inventoryHandler.update(delta);
		}
		
		playerHud.update(delta);
		
		// Lights
		getRayHandler().update();
		
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
		
		mapArea.draw(batch, delta);
		player.draw(batch, delta);
		
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
	
	public void drawUI(SpriteBatch batch, float delta) {
		if (inventoryHandler.isInventoryOpen()) {
			inventoryHandler.draw(batch, delta);
		}
		
		playerHud.draw(batch, delta);
	}
	
	public void onMouseClicked(int x, int y, int button) {
		playerHud.onMouseClicked(x, y, button);
	}
	
	public void onMouseReleased(int x, int y, int button) {
		playerHud.onMouseReleased(x, y, button);
	}
	
	public void onMouseHovered(int x, int y) {
		playerHud.onMouseHovered(x, y);
	}
	
	public void onMouseWheelScrolled(int amount) {
		playerHud.onMouseWheelScrolled(amount);
	}
	
	public boolean isDebugEnabled() {
		return debugEnabled;
	}
	
	public GameSession getSession() {
		return session;
	}
	
	public void addEntity(Entity e) {
		mapArea.addEntity(e);
	}
	
	public void removeEntity(Entity e) {
		mapArea.removeEntity(e);
	}
}
