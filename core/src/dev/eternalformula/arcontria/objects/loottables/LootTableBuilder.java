package dev.eternalformula.arcontria.objects.loottables;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import dev.eternalformula.arcontria.files.JsonUtil;
import dev.eternalformula.arcontria.objects.loottables.DynamicLootTable.ChanceType;
import dev.eternalformula.arcontria.util.EFDebug;

/**
 * A utility class that acts as a factory for LootTables.<br>
 * The LootTableBuilder acts as a means to quickly and easily create <br>
 * LootTables from .json files.
 * 
 * @author EternalFormula.
 */

public class LootTableBuilder {

	/**
	 * Loads a LootTable from the specified file.
	 * @param lootTableFile The path of the file (in the core/assets folder as the root).
	 * @return A LootTable constructed from the data in the specified file.
	 */
	
	@SuppressWarnings("unchecked")
	public static <T extends LootTable> T loadFromFile(String lootTableFile) {
		if (!lootTableFile.endsWith(".json")) {
			EFDebug.error("Invalid loot table file!: Given: \"" + lootTableFile + "\"");
			return null;
		}
		// Return Types: LootTable (Single), DynamicLootTable (Multi) 
		List<JsonNode> tables = JsonUtil.getLootTableNodes(lootTableFile);
		
		if (tables.size() > 1) {
			// DynamicLootTable
			System.out.println("Creating dynamic table");
			JsonNode root = JsonUtil.getRootNode(lootTableFile);
			ChanceType tableType = ChanceType.valueOf(root.get("chancetype").asText().toUpperCase());
			
			if (tableType == null) {
				return (T) buildDynamicTable(tables, ChanceType.INDEPENDENT);
			}
			
			return (T) buildDynamicTable(tables, tableType);
		}
		else {
			// BasicLootTable
			System.out.println("Creating basic table");
			return (T) buildBasicTable(tables.get(0));
		}
	}
	
	/**
	 * Constructs a DynamicLootTable, consisting of multiple loot tables combined into one.
	 * @param nodes The list of individual loot table nodes.
	 * @param type The ChanceType of the DynamicLootTable (independent/cumulative).
	 * @return A DynamicLootTable constructed from the list of loot table nodes.
	 */
	
	private static DynamicLootTable buildDynamicTable(List<JsonNode> nodes, ChanceType type) {
		return new DynamicLootTable(nodes, type);
	}
	
	/**
	 * Constructs a BasicLootTable.
	 * @param node The JSON node of the loot table
	 * @return A BasicLootTable constructed from the JSON node.
	 */
	
	private static BasicLootTable buildBasicTable(JsonNode node) {
		return new BasicLootTable(node);
	}

}