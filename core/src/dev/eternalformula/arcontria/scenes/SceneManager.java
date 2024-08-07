package dev.eternalformula.arcontria.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dev.eternalformula.arcontria.gfx.EGFXUtil;
import dev.eternalformula.arcontria.input.InputHandler;
import dev.eternalformula.arcontria.sfx.SoundManager;
import dev.eternalformula.arcontria.util.EFDebug;
import dev.eternalformula.arcontria.util.Strings;

public class SceneManager {
	
	private ViewportHandler viewportHandler;
	private InputHandler inputHandler;
	
	private SpriteBatch gameBatch;
	private SpriteBatch uiBatch;
	
	private ShapeRenderer shapeRend;
	private EFDebug debug;
	
	private SoundManager soundMgr;
	
	private Scene currentScene;
	
	public SceneManager() {
		this.viewportHandler = new ViewportHandler(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.inputHandler = new InputHandler(this);
		
		this.gameBatch = new SpriteBatch();
		this.uiBatch = new SpriteBatch();
		this.shapeRend = new ShapeRenderer();
		this.debug = new EFDebug();
		this.soundMgr = new SoundManager();
		soundMgr.initSounds();
	}
	
	
	public Scene getCurrentScene() {
		return currentScene;
	}
	
	public void setCurrentScene(Scene currentScene) {
		this.currentScene = currentScene;
	}
	
	public void update(float delta) {
		debug.update(delta);
		viewportHandler.update(delta);
		inputHandler.update(delta);
		
		if (currentScene != null) {
			currentScene.update(delta);
		}
	}
	
	public void draw(float delta) {
		gameBatch.setProjectionMatrix(viewportHandler.getGameViewport().getCamera().combined);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		if (currentScene != null) {
			currentScene.draw(gameBatch, delta);
		}
	}
	
	public void drawUI(float delta) {
		uiBatch.setProjectionMatrix(viewportHandler.getUIViewport().getCamera().combined);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		
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
	
	public SpriteBatch getGameBatch() {
		return gameBatch;
	}
	
	public ViewportHandler getViewportHandler() {
		return viewportHandler;
	}
	
	public InputHandler getInputHandler() {
		return inputHandler;
	}
	
	public ShapeRenderer getShapeRenderer() {
		return shapeRend;
	}
	
	public void dispose() {
		if (currentScene != null) {
			currentScene.dispose();
		}
	}
	
	public void resize(int width, int height) {
		viewportHandler.resize(width, height);
		
		if (currentScene != null) {
			currentScene.resize(width, height);
		}
		
	}
	
	public static class ViewportHandler {
		
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
			uiViewport.setUnitsPerPixel(1 / 2f);
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
			uiViewport.setUnitsPerPixel(1f / EGFXUtil.RENDER_SCALE);
			
			System.out.println("[Resize] New Render Scale: " + EGFXUtil.RENDER_SCALE);
			//System.out.println("[Resize] New UI UPP: " + uiViewport.getUnitsPerPixel());
			float uiWidth = uiViewport.getWorldWidth();
			float uiHeight = uiViewport.getWorldHeight();
			
			System.out.println("[Resize] New UI W/H: " + Strings.vec2(uiWidth, uiHeight));
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
		
		public Vector2 getUIDimensions() {
			return new Vector2(uiViewport.getWorldWidth(), uiViewport.getWorldHeight());
		}
	}

}
