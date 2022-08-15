package dev.eternalformula.arcontria.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Scene {
	
	protected SceneManager manager;
	protected float batchAlpha;
	
	public Scene(SceneManager manager) {
		this.manager = manager;
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
	
	public abstract void dispose();
	
	public float getBatchAlpha() {
		return batchAlpha;
	}
	
	public void setBatchAlpha(float batchAlpha) {
		this.batchAlpha = batchAlpha;
	}
}
