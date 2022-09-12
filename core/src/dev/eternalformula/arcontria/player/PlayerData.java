package dev.eternalformula.arcontria.player;

import java.io.File;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.eternalformula.arcontria.ecs.templates.Player;
import dev.eternalformula.arcontria.files.FileUtil;
import dev.eternalformula.arcontria.files.JsonUtil;
import dev.eternalformula.arcontria.objects.achievements.AchievementHandler;
import dev.eternalformula.arcontria.util.EFDebug;

public class PlayerData {
	
	private String saveFile;
	
	private PlayerStats stats;
	private AchievementHandler achHandler;
	
	private JsonNode playerDataNode;
	
	private float balance;
	public float stepsTakenSession;
	public float distanceTravelledSession;
	public double timePlayedSeconds;
	
	PlayerData(String saveFile, JsonNode playerData, PlayerStats stats, AchievementHandler achHandler) {
		this.saveFile = saveFile;
		this.playerDataNode = playerData;
		this.stats = stats;
		this.achHandler = achHandler;
	}
	
	public static PlayerData loadFromFile(String file) {
		// Do stuff
		JsonNode playerData = JsonUtil.getRootNodeFromLocalFile(file).get("playerData");
		
		JsonNode statsNode = playerData.get("stats");
		PlayerStats stats = new PlayerStats(file, statsNode);
		
		AchievementHandler achHandler = AchievementHandler.load(FileUtil.SAVES_FOLDER_LOCATION + File.separator + "elliott" + File.separator + "achievements.json");
		
		PlayerData data = new PlayerData(file, playerData, stats, achHandler);
		data.setBalance(playerData.get("balance").floatValue());
		
		return data;
	}
	
	public void save(Player player) {
		ObjectMapper objMapper = new ObjectMapper();
		
		HealthComponent health = 
		ObjectNode rootNode = objMapper.createObjectNode();
		
		String name = player.getName();
		float health = player.getHealth();
		float maxHealth = player.getMaxHealth();
		
		// Sets the saved metadata
		ObjectNode playerDataNode = objMapper.createObjectNode();
		playerDataNode.put("name", name);
		playerDataNode.put("balance", balance);
		playerDataNode.put("health", health);
		playerDataNode.put("maxHealth", maxHealth);
		playerDataNode.put("locationX", player.getLocation().x);
		playerDataNode.put("locationY", player.getLocation().y);
		
		// Sets the saved stats
		JsonNode statsNode = stats.save();
		playerDataNode.set("stats", statsNode);
		
		rootNode.set("playerData", playerDataNode);
		
		try {
			objMapper.writerWithDefaultPrettyPrinter().writeValue(new File(saveFile), rootNode);
			EFDebug.debug("Successfully saved player stats to " + saveFile);
		}
		catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public float getBalance() {
		return balance;
	}
	
	public void setBalance(float balance) {
		this.balance = balance;
	}
	
	public float getTotalDistanceTravelled() {
		return distanceTravelledSession + (float) stats.getRuntimeValue("distance-travelled", float.class);
	}
	
	public boolean isAchievementCompleted(String achievementId) {
		return achHandler.getAchievement(achievementId).isCompleted();
	}
	
	public AchievementHandler getAchievementHandler() {
		return achHandler;
	}
	
	public JsonNode getPlayerDataNode() {
		return playerDataNode;
	}
	
	public Object getStat(String statName) {
		return stats.getValue(statName);
	}
	
	public void updateMovement(float velX, float velY) {
		
		float deltaDist = 0f;
		
		float absX = Math.abs(velX * Gdx.graphics.getDeltaTime());
		float absY = Math.abs(velY * Gdx.graphics.getDeltaTime());
		
		if (absX != 0 && absY != 0) {
			deltaDist = (float) Math.sqrt(Math.pow(absX, 2) + Math.pow(absY, 2));
		}
		else {
			// one of these will always be 0 in this case.
			deltaDist = absX + absY;
		}
		
		// Updates the session's travelled distance
		distanceTravelledSession += deltaDist;
		
		float totalDistanceTravelled = distanceTravelledSession + (float) 
				stats.getRuntimeValue("distance-travelled", float.class);
		
		if (!isAchievementCompleted("touch-grass") && totalDistanceTravelled >= 50f) {
			achHandler.getAchievement("touch-grass").setCompleted(true);
		}
	}
}