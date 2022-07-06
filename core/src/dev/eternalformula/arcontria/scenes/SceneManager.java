package dev.eternalformula.arcontria.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dev.eternalformula.arcontria.gfx.EGFXUtil;

public class SceneManager {
	
	private ViewportHandler viewportHandler;
	
	private SpriteBatch gameBatch;
	private SpriteBatch uiBatch;
	
	private Scene currentScene;
	
	public SceneManager() {
		this.viewportHandler = new ViewportHandler(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.gameBatch = new SpriteBatch();
		this.uiBatch = new SpriteBatch();
	}
	
	public Scene getCurrentScene() {
		return currentScene;
	}
	
	public void setCurrentScene(Scene currentScene) {
		this.currentScene = currentScene;
	}
	
	public void update(float delta) {
		viewportHandler.update(delta);
		
		if (currentScene != null) {
			currentScene.update(delta);
		}
	}
	
	public void draw(float delta) {
		gameBatch.setProjectionMatrix(viewportHandler.getGameViewport().getCamera().combined);
		
		if (currentScene != null) {
			currentScene.draw(gameBatch, delta);
		}
	}
	
	public void drawUI(float delta) {
		uiBatch.setProjectionMatrix(viewportHandler.getUIViewport().getCamera().combined);
		
		if (currentScene != null) {
			currentScene.drawUI(uiBatch, delta);
		}
	}
	
	public OrthographicCamera getGameCamera() {
		return (OrthographicCamera) viewportHandler.getGameViewport().getCamera();
	}
	
	public OrthographicCamera getUICamera() {
		return (OrthographicCamera) viewportHandler.getUIViewport().getCamera();
	}
	
	public ViewportHandler getViewportHandler() {
		return viewportHandler;
	}
	
	public void dispose() {
		currentScene.dispose();
	}
	
	public void resize(int width, int height) {
		viewportHandler.resize(width, height);
		
		currentScene.resize(width, height);
	}
	
	static class ViewportHandler {
		
		private ScreenViewport viewport;
		private ScreenViewport uiViewport;
		
		private int width;
		private int height;
		
		ViewportHandler(int width, int height) {
			
			this.width = width;
			this.height = height;
			
			// Game Viewport
			this.viewport = new ScreenViewport();
			viewport.setUnitsPerPixel(EGFXUtil.DEFAULT_UPP);
			viewport.update(width, height);
			
			// UI Viewport
			this.uiViewport = new ScreenViewport();
			uiViewport.setUnitsPerPixel(1f);
			uiViewport.update(width, height);
			
			uiViewport.getCamera().position.set(uiViewport.getWorldWidth() / 2f, 
					uiViewport.getWorldHeight() / 2f, 0f);
		}
		
		public void update(float delta) {
			viewport.update(width, height);
			uiViewport.update(width, height);
		}
		
		public void resize(int width, int height) {
			this.width = width;
			this.height = height;
			
			// Viewport
			float upp = (width / EGFXUtil.DEFAULT_WIDTH);
			EGFXUtil.RENDER_SCALE = 2 * upp;
			viewport.setUnitsPerPixel(EGFXUtil.DEFAULT_UPP / upp);
			
			// UIViewport
			uiViewport.setUnitsPerPixel(1f);
			
			System.out.println("[Resize] New Render Scale: " + EGFXUtil.RENDER_SCALE);
		}
		
		public ScreenViewport getGameViewport() {
			return viewport;
		}
		
		public ScreenViewport getUIViewport() {
			return uiViewport;
		}
		
		public float getWorldWidth() {
			return viewport.getWorldWidth();
		}
		
		public float getWorldHeight() {
			return viewport.getWorldHeight();
		}
	}

}
