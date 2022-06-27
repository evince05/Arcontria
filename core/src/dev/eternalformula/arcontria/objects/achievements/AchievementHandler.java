package dev.eternalformula.arcontria.objects.achievements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import dev.eternalformula.arcontria.entity.player.Player;
import dev.eternalformula.arcontria.files.JsonUtil;
import dev.eternalformula.arcontria.objects.achievements.Achievement.AchievementData;
import dev.eternalformula.arcontria.util.EFDebug;

/**
 * Loads, updates, registers, and saves achievements.
 * @author EternalFormula.
 */

public class AchievementHandler {
	
	private Map<String, Achievement> achievements;
	
	/**
	 * Loads and returns an AchievementHandler created from the<br>
	 * "achievements.json" folder in the specified save directory.
	 * @param saveDir The path of the save folder.
	 * @return An AchievementHandler for the current save.
	 */
	
	public static AchievementHandler load(String saveDir) {
		JsonNode achievementsNode = JsonUtil.getRootNodeFromLocalFile(saveDir).get("achievements");
		return new AchievementHandler(achievementsNode);
	}
	
	private AchievementHandler(JsonNode achievementsNode) {
		this.achievements = new HashMap<String, Achievement>();
		loadAchievements(achievementsNode);
	}
	
	public Map<String, Achievement> getAchievements() {
		return achievements;
	}
	
	private void loadAchievements(JsonNode achievementNode) {
		achievementNode.fields().forEachRemaining(ach -> {
			String type = ach.getValue().get("type").asText();
			
			Achievement achievement = null;
			if (type.equals("single")) {
				achievement = loadSingleCondAchievement(ach.getValue());
			}
			else if (type.equals("multi")) {
				achievement = loadMultiCondAchievement(ach.getValue());
			}
			
			if (achievement != null) {
				achievements.put(achievement.getId(), achievement);
			}
			else {
				EFDebug.warn("Error loading an achievment!");
			}
			
			EFDebug.info("Loaded " + achievements.size() + " achievements!");
		});
	}
	
	public void onCompletion(Player player, Achievement achievement) {
		EFDebug.info(player.getName() + " has completed the achievement \"" + 
				achievement.getData().getDisplayName() + "\"!");
	}
	
	/**
	 * Loads a SingleConditionAchievement from the specified JsonNode.
	 * @param achNode The achievement node to be parsed.
	 * @return A SingleConditionAchievement parsed from the JsonNode, otherwise null.
	 */
	
	private SingleConditionAchievement loadSingleCondAchievement(JsonNode achNode) {
		
		// Creates the achievement metadata.
		String displayName = achNode.get("displayName").asText();
		String displayDesc = achNode.get("displayDesc").asText();
		AchievementData data = new AchievementData(displayName, displayDesc);
		
		// Loads the achievement data
		String id = achNode.get("id").asText();
		boolean isCompleted = achNode.get("isCompleted").asBoolean();
		
		Object currentValue = achNode.get("currentValue");
		Object targetValue = achNode.get("targetCondition");
		
		System.out.println("Current: " + currentValue + ", Target: " + targetValue);
	
		return new SingleConditionAchievement(id, data, currentValue,
				targetValue, isCompleted);
	}
	
	/**
	 * Loads a MultiConditionAchievement from the specified JsonNode.
	 * @param achNode The achievement node to be parsed.
	 * @return A MultiConditionAchievement parsed from the JsonNode, otherwise null.
	 */
	
	private MultiConditionAchievement loadMultiCondAchievement(JsonNode achNode) {
		
		// Creates the achievement metadata.
		String displayName = achNode.get("displayName").asText();
		String displayDesc = achNode.get("displayDesc").asText();
		AchievementData data = new AchievementData(displayName, displayDesc);
		
		// Loads the achievement data.
		String id = achNode.get("id").asText();
		boolean isCompleted = achNode.get("isCompleted").asBoolean();
		
		// Creates an empty MultiConditionAchievement
		MultiConditionAchievement mca = new MultiConditionAchievement(id, data, isCompleted);
		
		// Loads the map of conditions
		Map<String, MCACondition> conditions = new HashMap<String, MCACondition>();
		achNode.get("conditions").fields().forEachRemaining(cond -> {
			
			String condId = cond.getKey();
			JsonNode condition = cond.getValue();
			Object currentValue = condition.get("currentValue");
			Object targetCondition = condition.get("targetCondition");
			
			boolean isCondCompleted = condition.get("isCompleted").asBoolean();
			
			MCACondition mcaCond = new MCACondition(mca, currentValue, targetCondition,
					isCondCompleted);
			
			conditions.put(condId, mcaCond);
		});
		
		// Sets the conditions of the achievement.
		mca.setConditons(conditions);
		
		return mca;
	}
	
	public Achievement getAchievement(String id) {
		return achievements.get(id);
	}
}
