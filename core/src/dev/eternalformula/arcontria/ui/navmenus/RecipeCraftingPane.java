package dev.eternalformula.arcontria.ui.navmenus;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.items.Item;
import dev.eternalformula.arcontria.items.Recipe;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFDebug;

public class RecipeCraftingPane extends UIContainer {
	
	private RecipeWindow window;
	
	private Array<ItemDisplayBox> ingredientBoxes;
	private ItemDisplayBox resultBox;
	
	public RecipeCraftingPane(RecipeWindow window) {
		this.window = window;
		
		this.location = new Vector2(window.getLocation().x + 94,
				window.getLocation().y + 5);
		
		this.ingredientBoxes = new Array<ItemDisplayBox>();
		this.resultBox = new ItemDisplayBox(this, 
				(int) location.x + 68, (int) location.y + 24);
		
		addChild(resultBox);
	}
	
	/**
	 * Sets the recipe and updates the pane accordingly.
	 * @param recipe The recipe to be displayed.
	 */
	
	void setRecipe(Recipe recipe) {
		EFDebug.info("Attempting to set recipe: " + recipe.getSearchName());
		// Sets the item of the result box
		resultBox.setItem(recipe.getResult());
		
		// Sets the ingredient boxes
		
		int row = 0;
		int col = 0;
		
		// num ingredients
		int ni = recipe.getIngredients().size();
		int width = -4 * (int) Math.pow(ni, 2) + 44 * ni - 22;
		int spacingX = 22 - 4 * ni + 18;
	
		int startX = (int) location.x + 77 - width / 2;
		
		EFDebug.info("Cumulative Width of Boxes: " + width);
		ingredientBoxes.clear();
		
		for (Item ingredient : recipe.getIngredients()) {
			// Sets the proper x and y location of the item display box
			int x = startX + spacingX * col;
			int y = (int) location.y + 92;
			
			// Creates the item display box
			ItemDisplayBox displayBox = new ItemDisplayBox(this, x, y);
			displayBox.setItem(ingredient);
			
			ingredientBoxes.add(displayBox);
			col++;
		}
	}
	
	@Override
	protected void draw(SpriteBatch uiBatch, float delta) {
		super.draw(uiBatch, delta);
		
		BitmapFont rcpFont = Assets.get("fonts/pixelfj-8.fnt", BitmapFont.class);
		rcpFont.draw(uiBatch, "Ingredients:", location.x + 2, location.y + 130);
		ingredientBoxes.forEach(e -> {
			e.draw(uiBatch, delta);
		});
	}
	
	@Override
	public void onMouseHovered(int x, int y) {
		super.onMouseHovered(x, y);
		
		ingredientBoxes.forEach(e -> {
			e.onMouseHovered(x, y);
		});
		
		resultBox.onMouseHovered(x, y);
	}

}
