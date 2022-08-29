package dev.eternalformula.arcontria.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.RayHandler;
import dev.eternalformula.arcontria.physics.WorldContactListener;

public abstract class Scene {
	
	protected SceneManager manager;
	
	// Woooo
	protected RayHandler rayHandler;
	protected World world;
	
	protected float batchAlpha;
	
	public Scene(SceneManager manager) {
		this.manager = manager;
		this.world = new World(new Vector2(0f, 0f), false);
		world.setContactListener(new WorldContactListener());
		
		this.rayHandler = new RayHandler(world);
		
		loadAssets();
		load();
	}
	
	/**
	 * Handles any AssetManager loading.
	 */
	
	protected abstract void loadAssets();
	
	public abstract void load();
	
	public void draw(SpriteBatch batch, float delta) {
	}
	
	public void drawUI(SpriteBatch batch, float delta) {
	}
	
	public void update(float delta) {
	}
	
	public abstract void resize(int width, int height);
	
	public abstract void onKeyTyped(char key);
	
	public abstract void onMouseClicked(int x, int y, int button);
	
	public abstract void onMouseReleased(int x, int y, int button);
	
	public abstract void onMouseHovered(int x, int y);
	
	public abstract void onMouseDrag(int x, int y);
	
	public void dispose() {
		world.dispose();
		rayHandler.dispose();
	}
	
	public float getBatchAlpha() {
		return batchAlpha;
	}
	
	public void setBatchAlpha(float batchAlpha) {
		this.batchAlpha = batchAlpha;
	}
	
	public void setAmbientLight(float ambientLight) {
		rayHandler.setAmbientLight(ambientLight);
	}
	
	public World getWorld() {
		return world;
	}
	
	public RayHandler getRayHandler() {
		return rayHandler;
	}
}
