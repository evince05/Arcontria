package dev.eternalformula.arcontria.scenes.charcreator.tabs;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.eternalformula.arcontria.scenes.charcreator.CharacterCreatorScene;
import dev.eternalformula.arcontria.ui.actions.ButtonClickAction;
import dev.eternalformula.arcontria.ui.charcreator.CCGenderPicker;
import dev.eternalformula.arcontria.ui.elements.EFButton;
import dev.eternalformula.arcontria.ui.elements.EFTextField;
import dev.eternalformula.arcontria.util.Assets;

public class CCInfoTab extends CharacterCreatorTab {
	
	private EFTextField nameField;
	private EFTextField nicknameField;
	
	private CCGenderPicker genderPicker;
	
	private BitmapFont font;
	
	public CCInfoTab(CharacterCreatorScene scene) {
		super(scene);
		this.tabId = 0;
	}
	
	@Override
	public void load() {
		
		// Loads texture region
		TextureAtlas ccAtlas = Assets.get("ui/charcreator/charcreator.atlas", TextureAtlas.class);
		TextureAtlas uiAtlas = Assets.get("ui/elements/uiatlas.atlas", TextureAtlas.class);
		this.font = Assets.get("fonts/Habbo.fnt", BitmapFont.class);
		
		this.tabIcon = new EFButton(this, 92, 138);
		tabIcon.setSkin(new TextureRegion(ccAtlas.findRegion("tabicons"), 0, 0, 18, 18));
		tabIcon.setClickSkin(new TextureRegion(ccAtlas.findRegion("selectedtabicons"), 0, 0, 18, 18));
		tabIcon.setButtonMode(EFButton.TOGGLE_MODE);
		tabIcon.setClickAction(new ButtonClickAction() {

			@Override
			public void onClick(int x, int y, int button) {
				if (scene.getCurrentTab() != tabId) {
					scene.setCurrentTab(tabId);
				}
				else {
					tabIcon.setClicked(true);
				}
			}
		});
		
		this.nameField = new EFTextField(this);
		nameField.setLocation(90, 100);
		nameField.setSkin(uiAtlas.findRegion("textfield"));
		
		this.nicknameField = new EFTextField(this);
		nicknameField.setLocation(90, 68);
		nicknameField.setSkin(uiAtlas.findRegion("textfield"));
		
		this.genderPicker = new CCGenderPicker(this, 90, 50);
		
		addChildren(nameField, nicknameField, genderPicker);
		
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
		super.draw(batch, delta);
		
		if (scene.getCurrentTab() == tabId) {
			font.draw(batch, "Name", nameField.getLocation().x - 30,
					nameField.getLocation().y + 22);
			
			font.draw(batch, "Nickname", nicknameField.getLocation().x - 48, 
					nicknameField.getLocation().y + 22);
			
			font.draw(batch, "Sex:", genderPicker.getLocation().x - 30,
					genderPicker.getLocation().y + 14);
		}
		
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		
	}

	@Override
	public void dispose() {	
	}
}
