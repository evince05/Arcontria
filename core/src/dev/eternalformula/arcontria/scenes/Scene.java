package dev.eternalformula.arcontria.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Scene {
	
	protected SceneManager manager;
	protected float batchAlpha;
	
	private boolean isFading;
	private float targetAlpha;
	
	// 0 = fadeIn, 1 = fadeOut;
	private int fadeDirection;
	private float fadeSpeed;
	
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
		if (isFading) {
			batch.setColor(new Color(0f, 0f, 0f, batchAlpha));
		}
		
	}
	
	public void drawUI(SpriteBatch batch, float delta) {
		
		if (isFading) {
			batch.setColor(new Color(0f, 0f, 0f, batchAlpha));
		}
		
	}
	
	public void update(float delta) {
		if (isFading) {
			if (fadeDirection == 0) {
				// Fade in
				if (batchAlpha - fadeSpeed * delta > targetAlpha) {
					batchAlpha -= fadeSpeed * delta;
				}
				else {
					batchAlpha = targetAlpha;
					isFading = false;
				}
			}
			else if (fadeDirection == 1) {
				// Fade out
				if (batchAlpha + fadeSpeed * delta < targetAlpha) {
					batchAlpha += fadeSpeed * delta;
				}
				else {
					batchAlpha = targetAlpha;
					isFading = false;
				}
			}
		}
	}
	
	public abstract void resize(int width, int height);
	
	public abstract void onKeyTyped(char key);
	
	public abstract void onMouseClicked(int x, int y, int button);
	
	public abstract void onMouseReleased(int x, int y, int button);
	
	public abstract void onMouseDrag(int x, int y);
	
	public void fadeIn(float timeInSeconds) {
		isFading = true;
		batchAlpha = 1f;
		targetAlpha = 0f;
		fadeDirection = 0;
		fadeSpeed = 1f / timeInSeconds;
	}
	
	public void fadeOut(float timeInSeconds) {
		isFading = true;
		batchAlpha = 0f;
		targetAlpha = 1f;
		fadeDirection = 0;
		fadeSpeed = 1f / timeInSeconds;
	}
	
	public abstract void dispose();
}
