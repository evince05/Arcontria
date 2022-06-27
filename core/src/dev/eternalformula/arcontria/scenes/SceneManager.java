package dev.eternalformula.arcontria.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SceneManager {
	
	private Scene currentScene;
	
	public SceneManager() {
	}
	
	public Scene getCurrentScene() {
		return currentScene;
	}
	
	public void setCurrentScene(Scene currentScene) {
		this.currentScene = currentScene;
	}
	
	public void update(float delta) {
		if (currentScene != null) {
			currentScene.update(delta);
		}
	}
	
	public void draw(SpriteBatch batch, float delta) {
		if (currentScene != null) {
			currentScene.draw(batch, delta);
		}
	}

}
