package dev.eternalformula.arcontria.entity.player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.eternalformula.arcontria.util.EFDebug;

/**
 * A class used to load, store and save the player's statistics.
 * @author EternalFormula
 */

public class PlayerStats {
	
	private String outputFilePath;
	
	private Map<String, Object> stats;
	private JsonNode statsNode;
	
	PlayerStats(String outputFilePath, JsonNode statsNode) {
		this.outputFilePath = outputFilePath;
		this.statsNode = statsNode;
		this.stats = new HashMap<String, Object>();
		loadStats(statsNode);
		
		float timePlayed = getRuntimeValue("time-played", float.class);
		System.out.println("Time Played: " + timePlayed);
		
		String favWeapon = getRuntimeValue("favorite-weapon", String.class);
		System.out.println("Fav Weapon: " + favWeapon);
		
		setValue("favorite-weapon", "Rusty Sword");
	}
	
	private void loadStats(JsonNode statsNode) {
		
		statsNode.fields().forEachRemaining(statCategory -> {
			statCategory.getValue().fields().forEachRemaining(stat -> {		
				stats.put(stat.getKey(), 
						castToType(stat.getValue().get("value").asText(),
								stat.getValue().get("type").asText()));
			});
		});
	}
	
	public Object getValue(String stat) {
		return stats.get(stat);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getRuntimeValue(String statName, Class<?> type) {
		return (T) stats.get(statName);
	}
	
	public Object castToType(String stat, String type) {
		if (type.equalsIgnoreCase("String")) {
			return stat;
		}
		else if (type.equalsIgnoreCase("Float")) {
			return Float.valueOf(stat);
		}
		else if (type.equalsIgnoreCase("Boolean")) {
			return Boolean.valueOf(stat);
		}
		else if (type.equalsIgnoreCase("Double")) {
			return Double.valueOf(stat);
		}
		else if (type.equalsIgnoreCase("Integer")) {
			return Integer.valueOf(stat);
		}
		else {
			return null;
		}
	}
	
	public void setValue(String stat, Object value) {
		stats.put(stat, value);
	}
	
	/**
	 * Updates and saves the player's stats.
	 */
	
	public JsonNode save() {
		
		ObjectMapper objMapper = new ObjectMapper();
		
		statsNode.fields().forEachRemaining(statCategory -> {
			statCategory.getValue().fields().forEachRemaining(stat -> {
				
				String updatedStat = "";
				
				if (stats.get(stat.getKey()) instanceof String) {
					updatedStat = "\"" + String.valueOf(stats.get(stat.getKey())) + "\"";
				}
				else {
					updatedStat = String.valueOf(stats.get(stat.getKey()));
				}
				try {
					JsonNode newNode = objMapper.readTree(updatedStat);
					((ObjectNode) stat.getValue()).set("value", newNode);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		});
	
		return statsNode;
	}

}
