package dev.eternalformula.arcontria.cutscenes;

public class CutsceneDialogue {
	
	private String senderUUID;
	private String message;
	private String[] prompts;
	
	CutsceneDialogue(String senderUUID, String message, String[] prompts) {
		this.senderUUID = senderUUID;
		this.message = message;
		this.prompts = prompts;
	}
	
	CutsceneDialogue(String senderUUID, String message) {
		this.senderUUID = senderUUID;
		this.message = message;
	}
	
	/**
	 * Gets the UUID of the entity who sent the message.
	 */
	
	public String getSenderUUID() {
		return senderUUID;
	}
	
	/**
	 * Gets the message to be sent in the dialogue
	 */
	
	public String getMessage() {
		return message;
	}
	
	/**
	 * Gets the list of prompt IDs to be used 
	 */
	
	public String[] getPrompts() {
		return prompts;
	}
	
	/**
	 * Determines whether the CutsceneDialogue has any prompts.
	 */
	
	public boolean hasPrompts() {
		return prompts != null && prompts.length != 0;
	}
	

}
