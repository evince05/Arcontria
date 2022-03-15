package dev.eternalformula.arcontria.objects.loottables;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.databind.JsonNode;

import dev.eternalformula.arcontria.items.Item;

public class DynamicLootTable extends LootTable {
	
	// Generic Fields
	private ChanceType type;
	private List<BasicLootTable> tables;
	
	// Cumulative-Table-Specific Fields
	private float[] dropChances;
	private List<Integer> weightedKeys;
	
	DynamicLootTable(List<JsonNode> nodes, ChanceType type) {
		// build dyn LT.
		
		this.type = type;
		this.tables = new ArrayList<BasicLootTable>();
		this.dropChances = new float[nodes.size()];
		
		int i = 0;
		for (JsonNode tableNode : nodes) {
			tables.add(new BasicLootTable(tableNode));
			dropChances[i] = tableNode.get("dropChance").floatValue();
			i++;
		}
		
		if (type == ChanceType.CUMULATIVE) {
			for (int j = 0; j < dropChances.length; j++) {
				int numKeys = Math.round(dropChances[j] * 100);
				for (int n = 0; n < numKeys; n++) {
					weightedKeys.add(j);
				}
			}
		}
	}

	@Override
	public List<Item> selectItems() {
		List<Item> itemArr = new ArrayList<Item>();
		
		if (type == ChanceType.CUMULATIVE) {
			// CUMULATIVE
			int index = ThreadLocalRandom.current().nextInt(0, weightedKeys.size());
			LootTable lt = tables.get(weightedKeys.get(index));
			itemArr.addAll(lt.selectItems());
		}
		else {
			// INDEPENDENT (remember that type will never be null)
			int index = 0;
			for (LootTable lt : tables) {
				float tablePullChance = dropChances[index];
				
				if (tablePullChance == 1.0) {
					itemArr.addAll(lt.selectItems());
				}
				else {
					float chance = ThreadLocalRandom.current().nextFloat();
					if (chance <= tablePullChance) {
						itemArr.addAll(lt.selectItems());
					}
				}
				index++;
			}
		}
		
		return itemArr;
	}
	
	/**
	 * A small enum describing different pull methods for DynamicLootTables.
	 * @author EternalFormula
	 */
	
	public static enum ChanceType {
		
		/**
		 * Each table's pull weight combines to 1.0.<br>
		 * As such, only one table will be selected from the list of tables.
		 */
		
		CUMULATIVE,
		
		/**
		 * Each table's pull weight is independent of one another.<br>
		 * As such, each table has its own independent weight of being selected<br>
		 * and multiple tables can be selected.
		 */
		
		INDEPENDENT;
	}

}
