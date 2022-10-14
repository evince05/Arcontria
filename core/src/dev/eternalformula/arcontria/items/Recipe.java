package dev.eternalformula.arcontria.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.inventory.Inventory;
import dev.eternalformula.arcontria.util.Strings;

public class Recipe {
	
	private static final Map<String, Recipe> RECIPES = new HashMap<String, Recipe>();
	
	public static final int LOCATION_ANY = 0;
	public static final int LOCATION_FORGE = 1;
	
	static void initRecipe(Recipe recipe) {
		RECIPES.put(recipe.getSearchName(), recipe);
	}
	
	public static final Recipe find(String recipeName) {
		return RECIPES.get(recipeName);
	}
	
	/**
	 * Gets all recipes containing the given string.
	 * @param str The string to search
	 * @return All recipes containing the specified string.
	 */
	
	public static final Array<Recipe> getAllContaining(String str) {
		Array<Recipe> recipes = new Array<Recipe>();
		for (Map.Entry<String, Recipe> entry : RECIPES.entrySet()) {
			if (Strings.containsIgnoreCase(entry.getKey(), str)) {
				recipes.add(entry.getValue());
			}
		}
		return recipes;
	}
	
	private final String searchName;
	
	private final List<Item> ingredients;
	private final Item result;
	private final int location;
	
	Recipe(String searchName, List<Item> ingredients, Item result) {
		this.searchName = searchName;
		this.ingredients = ingredients;
		this.result = result;
		this.location = Recipe.LOCATION_ANY;
	}
	
	Recipe(String searchName, List<Item> ingredients, Item result, int location) {
		this.searchName = searchName;
		this.ingredients = ingredients;
		this.result = result;
		this.location = location;
	}
	
	public String getSearchName() {
		return searchName;
	}
	
	public boolean areAllIngredientsPresent(Inventory inventory) {
		int presentItems = 0;
		
		for (Item ingredient : ingredients) {
			for (int i = 0; i < inventory.getSize(); i++) {
				
				// Gets the itemstack in the inventory.
				Item item = inventory.getItem(i);
				if (item.getMaterial() == ingredient.getMaterial() &&
						item.getAmount() >= ingredient.getAmount()) {
					// Can move onto the next ingredient
					presentItems++;
					break;
				}
			}
		}
		return presentItems == ingredients.size();
	}
	
	public List<Item> getIngredients() {
		return ingredients;
	}
	
	public Item getResult() {
		return result;
	}
	
	public int getRecipeLocation() {
		return location;
	}
}
