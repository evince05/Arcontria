package dev.eternalformula.arcontria.ecs.components;

import com.badlogic.ashley.core.Component;

import dev.eternalformula.arcontria.items.Item;

/**
 * ItemEntityComponents are components used to describe ItemEntities,
 * which are items that are sitting on the ground in the world.
 * 
 * @author EternalFormula
 */

public class ItemEntityComponent implements Component {
	
	private Item item;
	
	public ItemEntityComponent() {
		this.item = null;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}

}
