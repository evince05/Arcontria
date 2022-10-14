package dev.eternalformula.arcontria.items;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Queue;
import com.fasterxml.jackson.databind.JsonNode;

import dev.eternalformula.arcontria.files.JsonUtil;
import dev.eternalformula.arcontria.util.EFDebug;

public class RecipeLoader {
	
	private static final RecipeLoader INSTANCE = new RecipeLoader();
	
	private Queue<JsonNode> recipesToInit;
	
	private boolean areRecipesLoaded;
	
	private RecipeLoader() {
		areRecipesLoaded = false;
	}
	
	public static RecipeLoader getInstance() {
		return INSTANCE;
	}
	
	public void addRecipeToQueue(JsonNode recipeNode) {
		recipesToInit.addLast(recipeNode);
	}
	
	public boolean areRecipesLoaded() {
		return areRecipesLoaded;
	}
	
	/**
	 * Loads each .rcp file in the data/recipes directory.
	 */
	
	public void loadRecipes() {
		if (!areRecipesLoaded) {

			long startTime = System.currentTimeMillis();
			int count = 0;
			
			// Iterates through each given recipe in the file, and then adds it to the registry.
			FileHandle assetsFile = Gdx.files.internal("data/recipes/recipes.txt");
			String assetsStr = assetsFile.readString();

			String[] filesArray = assetsStr.split("\\r?\\n");
			
			for(String file : filesArray) {
				
				long s = System.currentTimeMillis();
				// Recipe file

				JsonNode rootNode = JsonUtil.getRootNode(file);
				JsonNode recipesNode = rootNode.get("recipes");

				for (JsonNode recipeNode : recipesNode) {

					String searchName = recipeNode.get("searchName").textValue();
					List<Item> ingredients = new ArrayList<Item>();

					// Gets each ingredient of the recipe and adds it to the "ingredients" array.
					for (JsonNode ingredient : recipeNode.get("ingredients")) {
						Material m = Material.valueOf(ingredient.get("material")
								.textValue().toUpperCase());
						int amount = ingredient.get("amount").intValue();
						ingredients.add(new Item(m, amount));
					}

					// Gets the resulting item of the recipe
					JsonNode resultNode = recipeNode.get("result");
					Material m = Material.valueOf(resultNode.get("material").textValue().toUpperCase());
					int amount = resultNode.get("amount").intValue();
					Item result = new Item(m, amount);

					/*
					 * Determines the recipe location. If no location is specified in the .itd
					 * file, it is assumed that the recipe can be used anywhere.
					 */

					int location = Recipe.LOCATION_ANY;
					if (recipeNode.has("location")) {
						String recipeLocation = recipeNode.get("location").textValue();
						if (recipeLocation.equalsIgnoreCase("forge")) {
							location = Recipe.LOCATION_FORGE;
						}
					}
					Recipe recipe = new Recipe(searchName, ingredients, result, location);

					// Adds the recipe to the recipe registry.
					Recipe.initRecipe(recipe);
					count++;
					
					long e = System.currentTimeMillis();
					
					System.out.println("Elapsed Time for {" + m.getName() + "}: " + (e - s) / 1000D);
				}
			}

			long endTime = System.currentTimeMillis();
			String elapsedTime = (endTime - startTime) / 1000D + "s";
			
			EFDebug.info("Loaded " + count + " recipes! (" + elapsedTime + " elapsed)");
			areRecipesLoaded = true;
		}
		else {
			EFDebug.warn("Recipes are already loaded."
					+ " Disregarding call to RecipeLoader#loadRecipes()");
		}
		
	}

}
