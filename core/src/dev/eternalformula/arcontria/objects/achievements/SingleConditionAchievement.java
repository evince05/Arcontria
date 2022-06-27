package dev.eternalformula.arcontria.objects.achievements;

/**
 * A SingleConditionAchievment is an achievement in which only one condition<br>
 * must be met to complete the achievement.
 * @author EternalFormula
 *
 */
public class SingleConditionAchievement extends Achievement {

	private Object currentValue;
	private Object targetCondition;
	
	SingleConditionAchievement(String id, AchievementData data,
			Object currentValue, Object targetCondition, boolean isCompleted) {
		super(id, data, isCompleted);
		
		this.currentValue = currentValue;
		this.targetCondition = targetCondition;
	}
	
	public Object getCurrentValue() {
		return currentValue;
	}
	
	public void setCurrentValue(Object currentValue) {
		this.currentValue = currentValue;
		if (currentValue.equals(targetCondition)) {
			isCompleted = true;
		}
	}
}
