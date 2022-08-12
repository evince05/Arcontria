package dev.eternalformula.arcontria.cutscenes.commands;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.arcontria.cutscenes.Cutscene;
import dev.eternalformula.arcontria.cutscenes.CutsceneDialogue;
import dev.eternalformula.arcontria.cutscenes.CutsceneScript.CutsceneCommand;
import dev.eternalformula.arcontria.ui.elements.EFDialogueBox;
import dev.eternalformula.arcontria.util.Assets;

public class CutsceneDialogueCmd extends CutsceneCommand {
	
	private EFDialogueBox dialogueBox;
	
	public CutsceneDialogueCmd(Cutscene cutscene, CutsceneDialogue dialogue) {
		super(cutscene);
		
		// Creates the DialogueBox
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
			isFinished = true;
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

}
