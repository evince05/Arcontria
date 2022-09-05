package dev.eternalformula.arcontria.inventory;

import dev.eternalformula.arcontria.entity.player.Player;
import dev.eternalformula.arcontria.items.Item;

public class PlayerInventory extends Inventory {

	private Player player;
	
	private Item bootsItem;
	private Item chestItem;
	private Item helmetItem;
	
	/**
	 * Creates a new PlayerInventory.
	 * @param player The player whom the inventory belongs to
	 * @param name The name of the inventory.
	 */
	
	PlayerInventory(Player player, String name) {
		super(name, 9, 3);
		
		this.player = player;
		
		// Adds 3 slots (boots, chest, helmet)
		invSize += 3;
	}
	
	public static PlayerInventory createInventoryForPlayer(Player player) {
		return new PlayerInventory(player, player.getName() + "'s Inventory");
	}
	
	@Override
	public void setItem(int slot, Item item) {
		int baseSize = width * height;
		
		if (slot < baseSize) {
			super.setItem(slot, item);
			return;
		}
		else {
			if (slot == baseSize) {
				bootsItem = item;
				return;
			}
			else if (slot == baseSize + 1) {
				chestItem = item;
				return;
			}
			else if (slot == baseSize + 2) {
				helmetItem = item;
				return;
			}
		}
	}
	
	@Override
	public Item getItem(int slot) {
		int baseSize = width * height;
		
		if (slot < baseSize) {
			return super.getItem(slot);
		}
		else {
			if (slot == baseSize) {
				return bootsItem;
			}
			else if (slot == baseSize + 1) {
				return chestItem;
			}
			else if (slot == baseSize + 2) {
				return helmetItem;
			}
		}
		return null;
	}
	
	public Player getPlayer() {
		return player;
	}

}
