package dev.eternalformula.arcontria.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Scene {
	
	protected SceneManager manager;
	
	public Scene(SceneManager manager) {
		this.manager = manager;
		load();
	}
	
	public abstract void load();
	
	public abstract void draw(SpriteBatch batch, float delta);
	
	public abstract void drawUI(SpriteBatch batch, float delta);
	
	public abstract void update(float delta);
	
	public abstract void resize(int width, int height);
	
	public abstract void dispose();
}
