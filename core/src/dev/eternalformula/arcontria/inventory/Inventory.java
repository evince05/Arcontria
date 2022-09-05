package dev.eternalformula.arcontria.inventory;

import com.badlogic.gdx.utils.Array;

import dev.eternalformula.arcontria.items.Item;

public abstract class Inventory {
	
	protected Item[][] items;
	
	protected String name;
	
	protected final int width;
	protected final int height;
	
	protected int invSize;
	
	Inventory(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
		
		this.items = new Item[height][width];
	}
	
	public Item getItem(int slot) {
		int row = slot / width;
		int col = slot % width;
		
		return items[row][col];
	}
	
	/**
	 * Sets the item at the specified slot
	 * @param item The item to set
	 * @param slot The slot where the item should go. 
	 */
	
	public void setItem(int slot, Item item) {
		int row = slot / width;
		int col = slot % width;
		
		items[row][col] = item;
	}
	
	/**
	 * Adds the specified item to the inventory at the first available slot
	 * @param item The item to be added
	 * @return True if the item was added, otherwise false
	 */
	
	public boolean addItem(Item item) {
		for (int index = 0; index < invSize; index++) {
			if (getItem(index) == null) {
				setItem(index, item);
				return true;
			}
		}
		
		// No empty slots were found, so the item could not be added.
		return false;
	}
	
	/**
	 * Adds a collection of items to the inventory.
	 * @param items The items to be added
	 * @return The items that could not be added to the inventory.
	 */
	
	public Array<Item> addItems(Item... items) {
		Array<Item> leftoverItems = new Array<Item>();
		
		// Iterates through each item.
		for (Item i : items) {
			boolean isAdded = false;
			for (int index = 0; index < invSize; index++) {
				
				// If the item at the index is null, the specified item gets added.
				if (getItem(index) == null) {
					setItem(index, i);
					isAdded = true;
					break;
				}
			}
			
			// If the item could not be added, the item gets put in the leftoverItems arr.
			if (!isAdded) {
				leftoverItems.add(i);
			}
		}
		
		return leftoverItems;
	}
	
	/**
	 * Removes the item at the specified slot.
	 * @param slot The slot which should have its item removed.
	 */
	
	public void removeItem(int slot) {
		int row = slot / width;
		int col = slot % width;
		
		items[row][col] = null;
	}
	
	/**
	 * Gets the total size of the inventory.
	 */
	
	public int getSize() {
		return invSize;
	}
	
	/**
	 * Gets the width of each row in the inventory.
	 */
	
	public int getRowWidth() {
		return width;
	}
	
	/**
	 * Gets the number of rows in the inventory.
	 */
	
	public int getNumRows() {
		return height;
	}
	
	/**
	 * Gets the name of the inventory.
	 */
	
	public String getName() {
		return name;
	}
	

}
