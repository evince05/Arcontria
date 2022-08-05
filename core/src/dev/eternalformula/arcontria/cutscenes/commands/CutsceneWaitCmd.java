package dev.eternalformula.arcontria.cutscenes.commands;

import dev.eternalformula.arcontria.cutscenes.Cutscene;
import dev.eternalformula.arcontria.cutscenes.CutsceneScript.CutsceneCommand;

public class CutsceneWaitCmd extends CutsceneCommand {

	private float elapsedTime;
	private float waitTime;
	
	public CutsceneWaitCmd(Cutscene cutscene, float waitTime) {
		super(cutscene);
		
		this.waitTime = waitTime;
		this.elapsedTime = 0f;
	}

	@Override
	public void update(float delta) {
		elapsedTime += delta;
		if (elapsedTime >= waitTime) {
			isFinished = true;
		}
	}
}
