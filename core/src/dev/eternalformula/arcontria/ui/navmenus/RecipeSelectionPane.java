package dev.eternalformula.arcontria.ui.navmenus;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.items.Recipe;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.elements.EFSlider;
import dev.eternalformula.arcontria.ui.elements.EFTextField;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFDebug;

/**
 * The RecipeSelectionPane allows the user to choose what recipe should appear in the<br>
 * {@link RecipeWindow}. It can fit 24 recipes at once.
 * 
 * @author EternalFormula
 */

public class RecipeSelectionPane extends UIContainer {
	
	private RecipeWindow window;
	
	private Array<RecipeSelectionButton> recipeBtns;
	
	private EFTextField searchBox;
	private EFSlider slider;
	
	private static final int FIRST_RECIPEBTN_X = 2;
	private static final int FIRST_RECIPEBTN_Y = 100;
	
	/**
	 * Creates a new RecipeSelectionPane.
	 * @param window The RecipeWindow.
	 */
	
	RecipeSelectionPane(RecipeWindow window) {
		this.window = window;
		
		this.location = new Vector2(window.getLocation().x + 4,
				window.getLocation().y + 4);

		this.recipeBtns = new Array<RecipeSelectionButton>();
		
		// Instantiates the searchBox and slider
		
		this.searchBox = new EFTextField(this);
		searchBox.setLocation(location.x + 2, location.y + 120);
		searchBox.setSkin(Assets.get("ui/elements/uiatlas.atlas", TextureAtlas.class)
				.findRegion("recipesearchbox"));
		
		this.slider = new EFSlider(this, (int) location.x + 81, (int) location.y + 2,
				EFSlider.SLIDER_VERTICAL); // arb x and y

		addChildren(slider, searchBox);
		populateRecipeList();
	}
	
	/**
	 * Handles the updating of the recipe selector search function.
	 */
	
	@Override
	public void onKeyTyped(char key) {
		super.onKeyTyped(key);
		
		populateRecipeList();
	}
	
	public void populateRecipeList() {
		long s = System.currentTimeMillis();
		String query = searchBox.getText();
		Array<Recipe> recipes = new Array<Recipe>();
		recipes.addAll(Recipe.getAllContaining(query));
		
		int row = 0;
		int col = 0;
		
		// Updates the recipe buttons array
		recipeBtns.clear();
		for (Recipe recipe : recipes) {
			int x = (int) location.x + FIRST_RECIPEBTN_X + (2 + 18) * col;
			int y = (int) location.y + FIRST_RECIPEBTN_Y - (2 + 18) * row;
			
			RecipeSelectionButton btn = new RecipeSelectionButton(this, x, y);
			btn.setMaterial(recipe.getResult().getMaterial());
			recipeBtns.add(btn);
			
			col++;
			if (col > 3) {
				row++;
				col = 0;
			}
		}
		long t = (System.currentTimeMillis() - s) / 1000L;
		EFDebug.info("Recipes in Selection Pane: " + recipeBtns.size + " (" + t + "s)");
	}
	
	@Override
	public void onMouseDrag(int x, int y) {
		super.onMouseDrag(x, y);
	}
	
	@Override
	protected void draw(SpriteBatch uiBatch, float delta) {
		super.draw(uiBatch, delta);
		
		recipeBtns.forEach(recipeBtn -> {
			recipeBtn.draw(uiBatch, delta);
		});
	}
	
	@Override
	public void onMouseClicked(int x, int y, int button) {
		super.onMouseClicked(x, y, button);
		
		recipeBtns.forEach(recipeBtn -> {
			recipeBtn.onMouseClicked(x, y, button);
		});
	}
	
	@Override
	public void onMouseReleased(int x, int y, int button) {
		super.onMouseReleased(x, y, button);
		
		recipeBtns.forEach(recipeBtn -> {
			recipeBtn.onMouseReleased(x, y, button);
		});
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		recipeBtns.forEach(recipeBtn -> {
			recipeBtn.update(delta);
		});
	}
	
	public void onRecipeSelect(Recipe recipe) {
		window.onRecipeSelect(recipe);
	}
	
}
