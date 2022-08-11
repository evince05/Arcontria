package dev.eternalformula.arcontria.ui.elements;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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
	private EFButton exitBtn;
	
	private Rectangle textBoxBounds;
	
	private boolean clickToExitEnabled;
	private boolean requestingExit;
	
	/**
	 * The amount of time to wait after the label<br>
	 * finishes before the exit button should be displayed.
	 */
	
	private final float exitBtnDelayTime;
	private float transitionTimeElapsed;
	
	
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
		
		this.exitBtnDelayTime = 0.75f;
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
		
		if (typingLabel.isFinished() && !exitBtn.isVisible()) {
			transitionTimeElapsed += delta;
			
			if (transitionTimeElapsed >= exitBtnDelayTime) {
				exitBtn.setVisible(true);
				exitBtn.setActive(true);
			}
		}
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
}
