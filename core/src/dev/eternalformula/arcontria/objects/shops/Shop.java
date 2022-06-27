package dev.eternalformula.arcontria.objects.shops;

import java.util.ArrayList;
import java.util.List;

import dev.eternalformula.arcontria.entity.player.Player;

/**
 * The Shop class is used to buy items from NPCs.
 * @author EternalFormula
 */

public class Shop {
	
	protected List<ShopItem> items;
	
	/**
	 * Creates an "empty" shop.<br>
	 * Items can be added using {@link Shop#setItems(List)}
	 */
	
	public Shop() {
		this.items = new ArrayList<ShopItem>();
	}
	
	/**
	 * Creates a shop with a predetermined list of items for sale. 
	 * @param items The list of items to be sold in the shop.
	 */
	
	public Shop(List<ShopItem> items) {
		this.items = items;
	}
	
	public boolean buyItem(Player buyer, ShopItem item) {
		
		float playerBal = buyer.getPlayerData().getBalance();
		
		if (playerBal >= item.getPrice()) {
			// something like "buyer.getInventory().addItem(item)"
			// make sure to check for full inventory (if inv is full, display "INVENTORY FULL" popup)
			
			return true;
		}
		else {
			// Nothing happens. Player is broke (and probably sad).
			return false;
		}
	}
	
	public boolean buyItem(Player buyer, int index) {
		return buyItem(buyer, items.get(index));
	}
	
	public List<ShopItem> getItems() {
		return items;
	}
	
	public void setItems(List<ShopItem> items) {
		this.items = items;
	}
}
