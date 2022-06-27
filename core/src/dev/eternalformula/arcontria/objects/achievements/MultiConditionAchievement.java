package dev.eternalformula.arcontria.objects.achievements;

import java.util.List;
import java.util.Map;

/**
 * A MultiConditionAchievement is an {@link Achievmenet} in which multiple<br>
 * conditions must be met. For reference, think of Minecraft's achievements<br>
 * like "Hot Tourist Destination", in which all 5 Nether Biomes must be explored.
 * 
 * @author EternalFormula
 */

public class MultiConditionAchievement extends Achievement {
	
	private Map<String, MCACondition> conditions;
	
	MultiConditionAchievement(String id, AchievementData data, boolean isCompleted) {
		super(id, data, isCompleted);
	}
	
	void setConditons(Map<String, MCACondition> conditions) {
		this.conditions = conditions;
	}
	
	public MCACondition getValue(String conditionName) {
		return conditions.get(conditionName);
	}
	
	public void setValue(String conditionName, MCACondition value) {
		conditions.put(conditionName, value);
	}
	
	/**
	 * Iterates through each condition to see if the achievement is completed.<br>
	 * Note that this should only be used after one condition is set to true<br>
	 * (to avoid overuse).
	 */
	
	public void checkIfCompleted() {
		
		int numCompleted = 0;
		int numConditions = conditions.size();
		
		for (Map.Entry<String, MCACondition> cond : conditions.entrySet()) {
			if (cond.getValue().isCompleted()) {
				numCompleted++;
			}
			else {
				isCompleted = false; // At least 1 incomplete
			}
		}
		
		if (numCompleted == numConditions) {
			isCompleted = true; // 100% completion rate -> true
		}
		
	}
}