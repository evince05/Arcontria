package dev.eternalformula.arcontria.cutscenes;

public class CutscenePrompt {
	
	private String promptMsg;
	private String script;
	
	CutscenePrompt(String promptMsg, String script) {
		this.promptMsg = promptMsg;
		this.script = script;
	}
	
	/**
	 * Gets the prompt message.
	 */
	
	public String getMessage() {
		return promptMsg;
	}
	
	/**
	 * Gets the name of the CutsceneScript that should run if this prompt is selected. 
	 */
	
	public String getScript() {
		return script;
	}

}
