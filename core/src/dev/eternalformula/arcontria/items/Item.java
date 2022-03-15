package dev.eternalformula.arcontria.items;

public class Item {

	private Material material;
	private int amount;
	
	public Item(Material material, int amount) {
		this.material = material;
		this.amount = amount;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public String toDebugString() {
		return "[ITEM {Material: " + material.getName() + ", Amount: " + amount + "}]";
	}
}
