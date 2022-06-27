package dev.eternalformula.arcontria.objects.achievements;

import dev.eternalformula.arcontria.entity.player.Player;
import dev.eternalformula.arcontria.util.EFDebug;

public abstract class Achievement {
	
	protected String id;
	
	/**
	 * The text that is displayed with the achievemnt.
	 */
	
	protected AchievementData data;
	
	protected String displayDesc;
	protected boolean isCompleted;
	
	Achievement(String id, AchievementData data, boolean isCompleted) {
		this.id = id;
		this.data = data;
		this.isCompleted = isCompleted;
	}
	
	public String getId() {
		return id;
	}
	
	public boolean isCompleted() {
		return isCompleted;
	}
	
	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
		onCompletion();
	}
	
	public AchievementData getData() {
		return data;
	}
	
	public void onCompletion() {
		System.out.println("Elliott has completed the achievement \"" + data.getDisplayName() + "\"!");
	}
	
	public static class AchievementData {
		
		private String displayName;
		private String displayDesc;
		
		AchievementData(String displayName, String displayDesc) {
			this.displayName = displayName;
			this.displayDesc = displayDesc;
		}
		
		public String getDisplayName() {
			return displayName;
		}
		
		public String getDisplayDesc() {
			return displayDesc;
		}
	}

}
