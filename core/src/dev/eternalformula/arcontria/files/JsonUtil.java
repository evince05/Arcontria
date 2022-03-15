package dev.eternalformula.arcontria.files;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	
	public static List<JsonNode> getLootTableNodes(String lootTableFile) {
		
		List<JsonNode> tableNodes = new ArrayList<JsonNode>();
		
		ObjectMapper objMapper = new ObjectMapper();
		InputStream jsonStream = Gdx.files.internal(lootTableFile).read();
		
		try {
			JsonNode rootNode = objMapper.readTree(jsonStream);	
			
			if (rootNode.has("tables")) {
				// Dynamic Loot Table
				Iterator<JsonNode> tableItr = rootNode.get("tables").iterator();
				
				while (tableItr.hasNext()) {
					JsonNode jNode = tableItr.next();					

					Iterator<JsonNode> tableDataItr = jNode.iterator();
					
					while (tableDataItr.hasNext()) {
						JsonNode tableNode = tableDataItr.next();
						tableNodes.add(tableNode);
					}
				}
				return tableNodes;
			}
			else if (rootNode.has("table")) {
				// Single Loot Table
				tableNodes.add(rootNode.get("table"));
				return tableNodes;
			}
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Error parsing loottable. Returning null!");
		return null;
	}
	
	public static JsonNode getRootNode(String jsonFile) {
		ObjectMapper objMapper = new ObjectMapper();
		InputStream jsonStream = Gdx.files.internal(jsonFile).read();
		try {
			return objMapper.readTree(jsonStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
