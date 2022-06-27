package dev.eternalformula.arcontria.objects.achievements;

public class MCACondition {

	private MultiConditionAchievement achievement;	
	private Object currentValue;
	private Object targetCondition;
	private boolean isCompleted;

	/**
	 * A MCACondition is a condition that belongs to a specific<br>
	 * MultiConditionAchievement. It consists of four fields:<br>
	 * @param achievement The achievement which the condition belongs to.
	 * @param currentValue The current status/value of the condition.
	 * @param targetCondition The value that must be reached in order<br>
	 * <t>for the condition to be completed.<br>
	 * @param isCompleted Whether the condition is completed<br>
	 * <t>(currentValue is equal to the targetCondition)
	 * 
	 * @author EternalFormula
	 */

	MCACondition(MultiConditionAchievement achievement, 
			Object currentValue, Object targetCondition, boolean isCompleted){
		this.currentValue = currentValue;
		this.targetCondition = targetCondition;
		this.isCompleted = isCompleted;
	}

	public Object getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Object currentValue) {
		this.currentValue = currentValue;

		if (currentValue.equals(targetCondition)) {
			achievement.checkIfCompleted();
		}
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
}
