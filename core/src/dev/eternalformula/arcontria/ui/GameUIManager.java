package dev.eternalformula.arcontria.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.arcontria.input.InputInterface;
import dev.eternalformula.arcontria.ui.navmenus.RecipeWindow;
import dev.eternalformula.arcontria.util.EFDebug;

public class GameUIManager implements InputInterface {
	
	private static GameUIManager instance;
	
	private RecipeWindow recipeWindow;
	
	/**
	 * Gets the GameUIManager. <b>DO NOT</b> call this method
	 * until the game has been fully loaded (it will crash due to NPE).
	 * 
	 * @return A singleton instance of the GameUIManager.
	 */
	
	public static final GameUIManager getInstance() {
		return instance;
	}
	
	/**
	 * Creates the GameUIManager. This will only work once,<br>
	 * as it should only be used to initialize the singleton instance.
	 */
	
	public GameUIManager() {
		if (GameUIManager.getInstance() != null) {
			EFDebug.warn("The GameUIManager is already loaded!");
			return;
		}
		else {
			long s = System.currentTimeMillis();
			
			initUIElements();
			instance = this;
			
			long e = System.currentTimeMillis();
			long t = (e - s) / 1000L;
			EFDebug.info("GameUIManager loaded! (" + t + "s)");
		}
	}
	
	/**
	 * Initializes the UI Elements of the GameUIManager.
	 */
	
	private void initUIElements() {
		this.recipeWindow = new RecipeWindow((int) (320 / 2f - 252 / 2f),
				(int) (180 / 2f - 144 / 2f));
		
	}
	
	/**
	 * Opens the RecipeWindow
	 */
	public void toggleRecipeWindow() {
		EFDebug.info("Attempting to open RecipeWindow!");
		// if (canOpenRecipeWindow) [something like that lol]
		recipeWindow.toggle();
	}
	
	public void draw(SpriteBatch uiBatch, float delta) {
		if (recipeWindow.isActive()) {
			recipeWindow.draw(uiBatch, delta);
		}
	}
	
	public void update(float delta) {
		if (recipeWindow.isActive()) {
			recipeWindow.update(delta);
		}
	}
	
	@Override
	public void onMouseClicked(int x, int y, int button) {
		if (recipeWindow.isActive()) {
			recipeWindow.onMouseClicked(x, y, button);
		}
	}

	@Override
	public void onMouseReleased(int x, int y, int button) {
		if (recipeWindow.isActive()) {
			recipeWindow.onMouseReleased(x, y, button);
		}
		
	}

	@Override
	public void onMouseDrag(int x, int y) {
		if (recipeWindow.isActive()) {
			recipeWindow.onMouseDrag(x, y);
		}
	}
	
	@Override
	public void onMouseHovered(int x, int y) {
		if (recipeWindow.isActive()) {
			recipeWindow.onMouseHovered(x, y);
		}
	}
	
	@Override
	public void onMouseWheelScrolled(int amount) {
		if (recipeWindow.isActive()) {
			recipeWindow.onMouseWheelScrolled(amount);
		}
	}

	@Override
	public void onKeyTyped(char key) {
		if (recipeWindow.isActive()) {
			recipeWindow.onKeyTyped(key);
		}
	}
}
