package dev.eternalformula.arcontria.objects.loottables;

import java.util.List;

import dev.eternalformula.arcontria.items.Item;

public abstract class LootTable {
	
	/*
	private Map<Integer, LootTableItem> items;
	private int[] itemIds;
	
	private List<Integer> weightedIds;
	
	protected LootTable(Map<Integer, LootTableItem> items) {
		this.items = items;
		this.itemIds = new int[items.size()];
		this.weightedIds = new ArrayList<Integer>();
		
		populateList();
	}
	
	private void populateList() {
		
		int i = 0;
		for (Map.Entry<Integer, LootTableItem> entry : items.entrySet()) {
			itemIds[i] = entry.getKey();
			i++;
		}
		
		for (int listedId = 0; listedId < itemIds.length; listedId++) {
			int weight = Math.round(items.get(listedId).getWeight() * 100 * itemIds.length);
			
			for (int j = 0; j < weight; j++) {
				weightedIds.add(listedId);
			}
		}
	}
	
	public Item chooseItem() {
		
		int id = ThreadLocalRandom.current().nextInt(0, itemIds.length);
		return items.get(itemIds[id]).getItem();
		
		
	}*/
	
	/**
	 * Selects items from the loot table.
	 * <br>If the LootTable is basic, this will return a list with only one item.
	 * <br>If the LootTable is dynamic, this will return a list with all the items that are pulled.
	 * @return A list containing a set of pulled items.
	 */
	
	public abstract List<Item> selectItems();
	
	/**
	 * LootTableItem class stores an item as well as its weight.
	 */
	
	public static class LootTableItem {
		
		private Item item;
		private float weight;
		
		public LootTableItem(Item item, float weight) {
			this.item = item;
			this.weight = weight;
		}
		
		public Item getItem() {
			return item;
		}
		
		public float getWeight() {
			return weight;
		}
	}

}