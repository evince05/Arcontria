package dev.eternalformula.arcontria.cutscenes.commands;

import dev.eternalformula.arcontria.cutscenes.Cutscene;
import dev.eternalformula.arcontria.cutscenes.CutsceneScript.CutsceneCommand;

public class SimpleCutsceneCmd extends CutsceneCommand {
	
	private SimpleCutsceneAction action;
	
	public SimpleCutsceneCmd(Cutscene cutscene, SimpleCutsceneAction action) {
		super(cutscene);
		
		this.action = action;
	}

	/**
	 * Theoretically, this should only be called once.
	 */
	
	@Override
	public void update(float delta) {
		action.run();
		isFinished = true;
	}
	
	public static interface SimpleCutsceneAction {
		public void run();
	}

}
