package dev.eternalformula.arcontria.ui.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dev.eternalformula.arcontria.cutscenes.Cutscene;
import dev.eternalformula.arcontria.cutscenes.CutscenePrompt;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.actions.ButtonClickAction;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFDebug;

/**
 * EFDialogueBox.
 * @author EternalFormula
 */

public class EFDialogueBox extends UIContainer {

	private TextureAtlas uiAtlas;
	
	private EFTypingLabel typingLabel;
	
	private EFButton[] promptBtns;
	private EFButton exitBtn;
	
	private Rectangle textBoxBounds;
	
	// Handles display of prompts & exit btn
	private boolean postTextActionEnabled;
	
	private boolean requestingExit;
	
	/**
	 * The amount of time to wait after the label<br>
	 * finishes before post text action should occur.
	 */
	
	private final float postTextDelayTime;
	private float transitionTimeElapsed;
	
	// Temp
	private TextureRegion senderPortrait;
	
	
	public EFDialogueBox(String text, int x, int y) {
		
		this.location = new Vector2(x, y);
		this.uiAtlas = Assets.get("ui/elements/uiatlas.atlas", TextureAtlas.class);
		
		// Skin
		setSkin(uiAtlas.findRegion("dialoguebox"));
		
		// Bounds (for click) (hardcoded sorry lol)
		this.textBoxBounds = new Rectangle(x + 100, y + 8, x + 236, y + 104);
		
		// Label
		this.typingLabel = new EFTypingLabel(this, text);
		typingLabel.setLocation(x + 100, y + 104);
		
		// Exit Button
		this.exitBtn = new EFButton(this, x + 229, y + 7);
		
		// Sender Portrait
		this.senderPortrait = new TextureRegion(new 
				Texture(Gdx.files.internal("ui/dialogue/demoportrait.png")));
		
		// Skins
		TextureRegion btnRegion = uiAtlas.findRegion("exitdialoguebtn");
		exitBtn.setSkin(new TextureRegion(btnRegion, 0, 0, 9, 9));
		exitBtn.setClickSkin(new TextureRegion(btnRegion, 9, 0, 9, 9));
		exitBtn.setActive(false);
		exitBtn.setVisible(false);
		exitBtn.setClickAction(new ButtonClickAction() {

			@Override
			public void onClick(int x, int y, int button) {
				requestingExit = true;
			}
		});
		
		addChildren(typingLabel, exitBtn);
		
		this.postTextDelayTime = 0.75f;
	}
	
	public void setFont(BitmapFont font, int lineWidth) {
		typingLabel.setupWrapping(font, lineWidth);
	}
	
	public boolean isFinishedDialogue() {
		return typingLabel.isFinished();
	}
	
	public boolean isRequestingExit() {
		return requestingExit;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (typingLabel.isFinished() && !postTextActionEnabled) {
			transitionTimeElapsed += delta;
			
			if (transitionTimeElapsed >= postTextDelayTime) {
				postTextActionEnabled = true;
				
				if (promptBtns != null) {
					// Display prompts
					for (EFButton btn : promptBtns) {
						btn.setActive(true);
						btn.setVisible(true);
					}
				}
				else {
					// Display exit buton
					exitBtn.setVisible(true);
					exitBtn.setActive(true);
				}				
			}
		}
	}
	
	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		super.draw(uiBatch, delta);
		
		// Draw the portrait
		uiBatch.draw(senderPortrait, location.x + 16, location.y + 32);
	}
	
	@Override
	public void onMouseClicked(int x, int y, int button) {
		super.onMouseClicked(x, y, button);
		
		if (textBoxBounds.contains(x, y) && !typingLabel.isFinished()) {
			// Requests the closing of the dialoguebox
			EFDebug.info("Requesting text skip!");
			
			typingLabel.skipToEnd();
		}
	}
	
	
	
	/**
	 * Sets the cutscene prompts of the dialogue box.
	 * @param cutscene The cutscene to which the dialogue box belongs.
	 * @param prompts An array of prompts that belong to the dialogue box.
	 */
	
	public void setCutscenePrompts(Cutscene cutscene, String[] prompts) {
		// Create prompt button array
		this.promptBtns = new EFButton[prompts.length];
		
		for (int i = 0; i < prompts.length; i++) {
			// Gets the prompt
			CutscenePrompt prompt = cutscene.getPrompt(prompts[i]);
			
			// Create the button.
			EFButton promptBtn = new EFButton(this, (int) location.x + 97, (int) location.y + 5 + 19 * i);
			
			// Skins
			TextureRegion promptReg = uiAtlas.findRegion("promptbox");
			promptBtn.setSkin(new TextureRegion(promptReg, 0, 0, 142, 18));
			promptBtn.setClickSkin(new TextureRegion(promptReg, 0, 18, 142, 18));
			
			// Text & Visibility
			promptBtn.setText(prompt.getMessage(), 4f, 0f);
			promptBtn.setActive(false);
			promptBtn.setVisible(false);
			
			// Set prompt scripts
			promptBtn.setClickAction(new ButtonClickAction() {

				@Override
				public void onClick(int x, int y, int button) {
					cutscene.setScript(prompt.getScript());
					EFDebug.info("Switching script to " + prompt.getScript());
				}
			});
			
			// Adds the child to the EFDialogueBox
			promptBtns[i] = promptBtn;
			addChild(promptBtn);
		}
	}
	
	/**
	 * Chains a new Cutscene Dialogue to the existing DialogueBox.<br>
	 * This method should only be used when the existing DialogueBox is finished.
	 * @param cutscene The cutscene that is active.
	 * @param text The new dialogue text.
	 * @param prompts Any existing dialogue prompts.
	 */
	
	public void chainNewDialogue(Cutscene cutscene, String text, String[] prompts) {
		typingLabel.resetWithNewText(text);
		
		if (prompts != null && prompts.length > 0) {
			setCutscenePrompts(cutscene, prompts);
		}
		
		requestingExit = false;
	}
}
