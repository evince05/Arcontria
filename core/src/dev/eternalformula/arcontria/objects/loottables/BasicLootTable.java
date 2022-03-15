package dev.eternalformula.arcontria.objects.loottables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import dev.eternalformula.arcontria.items.Item;
import dev.eternalformula.arcontria.items.Material;

public class BasicLootTable extends LootTable {
	
	private Map<Integer, LootTableItem> items;
	private List<Integer> itemKeys;
	
	/**
	 * Creates a BasicLootTable
	 * @param node The JsonNode of the item.
	 */
	
	BasicLootTable(JsonNode node) {
		
		ArrayNode itemArr = (ArrayNode) node.get("items");
		Iterator<JsonNode> itr = itemArr.iterator();
		
		this.items = new HashMap<Integer, LootTableItem>();
		this.itemKeys = new ArrayList<Integer>();
		
		// Add null value to items
		items.put(-1, null);
		
		int j = 0;
		while (itr.hasNext()) {
			JsonNode item = itr.next();
			
			// Create item
			String material = item.get("material").asText().toUpperCase();
			if (Material.valueOf(material) == null) {
				System.out.println("[BasicLootTable] Invalid material name! Input: \"" + material + "\"");
				continue;
			}
			
			Item i = new Item(Material.valueOf(material), item.get("amount").intValue());
			LootTableItem lti = new LootTableItem(i, item.get("weight").floatValue());
			items.put(j, lti);
			
			int weight = Math.round(lti.getWeight() * 100);
			for (int c = 0; c < weight; c++) {
				itemKeys.add(j);
			}
			j++;
		}
		
		// Filling remaining values with null objects.
		if (itemKeys.size() < 100) {
			int remainder = 100 - itemKeys.size();
			for (int i = 0; i < remainder; i++) {
				itemKeys.add(-1);
			}
		}
		
	}

	@Override
	public List<Item> selectItems() {
		List<Item> itemArray = new ArrayList<Item>();
		int itemKey = ThreadLocalRandom.current().nextInt(0, itemKeys.size());
		
		int mapKey = itemKeys.get(itemKey);
		if (items.get(mapKey) != null) {
			itemArray.add(items.get(mapKey).getItem());
		}
		return itemArray;
	}

}
