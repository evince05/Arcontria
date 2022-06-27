package dev.eternalformula.arcontria.objects.shops;

import dev.eternalformula.arcontria.items.Item;

/**
 * A ShopItem is an item that can be sold in an NPC's shop.
 * @author EternalFormula
 */

public class ShopItem {
	
	private Item item;
	private float buyPrice;
	private String itemDescription;
	
	/**
	 * Constructs a ShopItem.
	 * <br><t>Manually sets description as the item's lore, though this
	 * <br><t>can be overriden using {@link ShopItem#setDescription(String)}.
	 * @param item The item for sale.
	 * @param buyPrice The price of the item.
	 */
	
	public ShopItem(Item item, float buyPrice) {
		this.item = item;
		this.buyPrice = buyPrice;
	}
	
	/**
	 * Constructs a ShopItem
	 * @param item The item for sale.
	 * @param buyPrice The price of the item.
	 * @param itemDescription The description of the item (lore).
	 */
	
	public ShopItem(Item item, float buyPrice, String itemDescription) {
		this.item = item;
		this.buyPrice = buyPrice;
		this.itemDescription = itemDescription;
	}
	
	public Item getItem() {
		return item;
	}
	
	public float getPrice() {
		return buyPrice;
	}
	
	public String getDescription() {
		return itemDescription;
	}
	
	public void setDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

}
