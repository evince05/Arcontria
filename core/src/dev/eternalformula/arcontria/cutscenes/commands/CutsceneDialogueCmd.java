package dev.eternalformula.arcontria.cutscenes.commands;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.arcontria.cutscenes.Cutscene;
import dev.eternalformula.arcontria.cutscenes.CutsceneDialogue;
import dev.eternalformula.arcontria.cutscenes.CutsceneScript.CutsceneCommand;
import dev.eternalformula.arcontria.gfx.EGFXUtil;
import dev.eternalformula.arcontria.ui.elements.EFDialogueBox;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFDebug;

public class CutsceneDialogueCmd extends CutsceneCommand {
	
	private EFDialogueBox dialogueBox;
	
	private String[] dialogues;
	
	// The index of the current dialogue
	private int dialogueIndex;
	
	/**
	 * Creates a new CutsceneDialogueCmd.
	 * @param cutscene The active cutscene.
	 * @param dialogues An array of dialogues to be used.
	 */
	
	public CutsceneDialogueCmd(Cutscene cutscene, String[] dialogues) {
		super(cutscene);
		
		this.dialogues = dialogues;
		this.dialogueIndex = 0;
		
		// Creates the DialogueBox using the first element in the dialogues array.
		CutsceneDialogue dialogue = cutscene.getDialogue(dialogues[dialogueIndex]);
		this.dialogueBox = new EFDialogueBox(dialogue.getMessage(), 38, 10); // y=34 to center :)
		if (dialogue.hasPrompts()) {
			dialogueBox.setCutscenePrompts(cutscene, dialogue.getPrompts());
		}
		
		BitmapFont font = Assets.get("fonts/Habbo.fnt", BitmapFont.class);
		dialogueBox.setFont(font, 136);
	}

	@Override
	public void update(float delta) {
		dialogueBox.update(delta);
		
		if (dialogueBox.isRequestingExit()) {
			
			dialogueIndex++;
			
			if (dialogueIndex < dialogues.length) {
				// There is still another dialogue to come.	
				
				// Get the new dialogue
				CutsceneDialogue dialogue = cutscene.getDialogue(dialogues[dialogueIndex]);
				
				// Chains the dialogue to the existing DialogueBox.
				dialogueBox.chainNewDialogue(cutscene, dialogue.getMessage(), dialogue.getPrompts());
				
				EFDebug.info("Chaining dialogue");
			}
			else {
				isFinished = true;
				EGFXUtil.setSceneAlpha(0f);
			}
		}
	}
	
	public void draw(SpriteBatch batch, float delta) {
		dialogueBox.draw(batch, delta);
	}
	
	public void onMouseClicked(int x, int y, int button) {
		dialogueBox.onMouseClicked(x, y, button);
	}
	
	public void onMouseReleased(int x, int y, int button) {
		dialogueBox.onMouseReleased(x, y, button);
	}
	
	public void onMouseHovered(int x, int y) {
		dialogueBox.onMouseHovered(x, y);
	}
}
