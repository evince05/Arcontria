package dev.eternalformula.arcontria.ecs.components.quests;

import com.badlogic.ashley.core.Component;

public class QuestComponent implements Component {
	
	/**
	 * The name (id) of the quest.
	 * eg. "lumberjack".
	 */
	
	public String name;
	
	/**
	 * The display name of the quest in the Quest UI.
	 */
	
	public String displayName;
	
	/**
	 * The text of the quest.
	 */
	
	public String promptText;
	
	/**
	 * The current value of the quest.<br>
	 * eg. in the case where the quest would be "collect 50 logs",<br>
	 * this value would hold the number of logs in the player's inventory.
	 */
	
	public Object currentValue;
	
	/**
	 * The completed value of the quest.<br>
	 * eg. in the case where the quest would be "collect 50 logs",<br>
	 * this value would 50, as it is how many logs need to be collected<br>
	 * in order to complete the quest.
	 */
	
	public Object completedValue;
	
	/**
	 * The action that should occur once the quest has been completed.
	 */
	
	public CompletedQuestAction action;
	
	public boolean hasActionOccurred;
	
	/**
	 * Determines whether the quest is active.<br>
	 * This field is especially useful as it controls<br>
	 * which quests should actively be updated. A possible<br>
	 * application of this field is to separate early-game quests<br>
	 * and late-game ones.
	 */
	
	public boolean isQuestActive;
	
	public static interface CompletedQuestAction {
		
		/**
		 * Executes some command on the completion of a quest.
		 */
		
		public void execute();
	}

}
