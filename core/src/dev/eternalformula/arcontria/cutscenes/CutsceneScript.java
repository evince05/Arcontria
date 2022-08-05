package dev.eternalformula.arcontria.cutscenes;

import com.badlogic.gdx.utils.Queue;

public class CutsceneScript {
	
	private Queue<String> commands;
	
	CutsceneScript(String[] commands) {
		
		// Init queue
		this.commands = new Queue<String>();
		
		// Populat queue
		for (int i = 0; i < commands.length; i++) {
			this.commands.addLast(commands[i]);
		}
	}
	
	public String getCurrentCommand() {
		return commands.first();
	}
	
	public int getRemainingCommands() {
		return commands.size;
	}
	
	/**
	 * Code to be run when the current command is completed. 
	 */
	
	public String moveToNextCommand() {
		commands.removeFirst();
		
		if (isFinished()) {
			return "";
		}
		return getCurrentCommand();
	}
	
	public boolean isFinished() {
		return commands.size == 0;
	}
	
	/**
	 * Cutscene Command Handler
	 * @author EternalFormula
	 */
	
	public static abstract class CutsceneCommand {
	
		protected Cutscene cutscene;
		protected boolean isFinished;
		
		public CutsceneCommand(Cutscene cutscene) {
			this.cutscene = cutscene;
		}
		
		public abstract void update(float delta);
		
		public boolean isFinished() {
			return isFinished;
		}
	}
}
