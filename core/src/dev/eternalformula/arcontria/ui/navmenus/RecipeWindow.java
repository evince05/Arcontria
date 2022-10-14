package dev.eternalformula.arcontria.ui.navmenus;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import dev.eternalformula.arcontria.items.Recipe;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFDebug;

/**
 * The RecipeWindow is a UI container that facilitates the crafting experience.
 * @author EternalFormula
 */

public class RecipeWindow extends UIContainer {
	
	private RecipeSelectionPane rcpSelPane;
	private RecipeCraftingPane rcpCraftPane;
	
	private boolean isOpen;
	
	public RecipeWindow(int x, int y) {
		super();
		setLocation(x, y);
		setSkin(Assets.get("ui/elements/uiatlas.atlas", TextureAtlas.class).findRegion("recipewindow"));
		
		this.rcpSelPane = new RecipeSelectionPane(this);
		this.rcpCraftPane = new RecipeCraftingPane(this);
		addChildren(rcpSelPane, rcpCraftPane);
	}
	
	void onRecipeSelect(Recipe recipe) {
		// Do stuff
		EFDebug.info("[RecipeWindow] Attempted to open recipe " + recipe.getSearchName());
		recipe.getIngredients().forEach(i -> {
			EFDebug.info("ingredient: " + i.toDebugString());
		});
		rcpCraftPane.setRecipe(recipe);
	}
	
	public void toggle() {
		isOpen = !isOpen;
		
		setActive(isOpen);
		setInteractive(isOpen);
		setVisible(isOpen);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
	}
	
	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		super.draw(uiBatch, delta);
	}
	
	@Override
	protected void drawChildren(SpriteBatch uiBatch, float delta) {
		// TODO Auto-generated method stub
		super.drawChildren(uiBatch, delta);
	}
	
	@Override
	public void onMouseClicked(int x, int y, int button) {
		super.onMouseClicked(x, y, button);
	}
	
}
