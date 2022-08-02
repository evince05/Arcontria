package dev.eternalformula.arcontria.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Scene {
	
	protected SceneManager manager;
	
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
	
	public abstract void draw(SpriteBatch batch, float delta);
	
	public abstract void drawUI(SpriteBatch batch, float delta);
	
	public abstract void update(float delta);
	
	public abstract void resize(int width, int height);
	
	public abstract void onKeyTyped(char key);
	
	public abstract void onMouseClicked(int x, int y, int button);
	
	public abstract void onMouseReleased(int x, int y, int button);
	
	public abstract void onMouseDrag(int x, int y);
	
	public abstract void dispose();
}
