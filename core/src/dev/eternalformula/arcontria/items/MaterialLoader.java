package dev.eternalformula.arcontria.items;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import dev.eternalformula.arcontria.files.JsonUtil;
import dev.eternalformula.arcontria.util.EFDebug;

public class MaterialLoader {
	
	public static Material loadMaterial(Material material, String itemDataFile) {
		
		long startTime = System.currentTimeMillis();
		JsonNode root = JsonUtil.getRootNode(itemDataFile);
		
		// Item Names
		String materialName = root.get("name").textValue();
		material.name = materialName;
		
		String displayName = root.get("displayName").textValue();
		material.displayName = displayName;
		
		List<String> loreArr = new ArrayList<String>();
		if (root.has("lore")) {
			JsonNode loreNode = root.get("lore");
			if (loreNode.isArray()) {
				
				for (JsonNode n : loreNode) {
					String loreStr = n.textValue();
					loreArr.add(loreStr);
				}
			}	
		}
		// idea: include recipe in .itd file?
		// idea 2: provide recipe array in .itd file (eg torches in MC -> can be crafted w coal or charcoal... 2 recipes)
		material.lore = loreArr;
		
		int maxStackSize = 999;
		if (root.has("maxStackSize")) {
			maxStackSize = root.get("maxStackSize").asInt();
		}
		material.maxStackSize = maxStackSize;
		
		int durability = -1;
		if (root.has("durability")) {
			durability = root.get("durability").asInt();
		}
		material.durability = durability;
		if (material.durability == -1) {
			material.hasDurability = false;
		}
		else {
			material.hasDurability = true;
		}
		
		boolean isConsumable = false;
		boolean isEquipable = false;
		
		if (root.has("consumable")) {
			isConsumable = root.get("consumable").asBoolean();
		}
		material.isConsumable = isConsumable;
		
		if (root.has("equipable")) {
			isEquipable = root.get("equipable").asBoolean();
		}
		material.isEquipable = isEquipable;

		long endTime = System.currentTimeMillis();
		System.out.println("Load Time (" + material.name + "): " + ((endTime - startTime) / 1000D) + "s");
		return material;
	}

}
