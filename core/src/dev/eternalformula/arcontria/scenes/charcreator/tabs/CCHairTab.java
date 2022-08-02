package dev.eternalformula.arcontria.scenes.charcreator.tabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.eternalformula.arcontria.scenes.charcreator.CharacterCreatorScene;
import dev.eternalformula.arcontria.ui.actions.ButtonClickAction;
import dev.eternalformula.arcontria.ui.charcreator.CCColorPicker;
import dev.eternalformula.arcontria.ui.charcreator.CCStylePicker;
import dev.eternalformula.arcontria.ui.elements.EFButton;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.Strings;

public class CCHairTab extends CharacterCreatorTab {
	
	private CCStylePicker stylePicker;
	private CCColorPicker colorPicker;
	
	public CCHairTab(CharacterCreatorScene scene) {
		super(scene);
		
		this.tabId = 3;
	}

	@Override
	public void load() {
		TextureAtlas ccAtlas = Assets.get("ui/charcreator/charcreator.atlas", TextureAtlas.class);
		
		this.tabIcon = new EFButton(this, 152, 138);
		tabIcon.setSkin(new TextureRegion(ccAtlas.findRegion("tabicons"), 54, 0, 18, 18));
		tabIcon.setClickSkin(new TextureRegion(ccAtlas.findRegion("selectedtabicons"), 54, 0, 18, 18));
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
		
		TextureRegion hairRegion = Assets.get("ui/charcreator/playerbuilder/playerbuilder.atlas",
				TextureAtlas.class).findRegion("hair");
		
		// Style Picker
		this.stylePicker = new CCStylePicker(scene, this, 64, 34);
		stylePicker.setPreviewTexture(hairRegion, 16, 16);
		stylePicker.setItemTextPrefix("Hair");
		stylePicker.setMaxItems(3);
		
		// Color Picker
		this.colorPicker = new CCColorPicker(156, 34);
		colorPicker.setColor(new Color(64 / 255f, 43 / 255f, 20 / 255f, 1f));
				
		// Child Management
		addChildren(stylePicker, colorPicker);
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
		super.draw(batch, delta);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		
		scene.getPlayerPreview().setHairColor(colorPicker.getColor());
		scene.getPlayerPreview().hairId = stylePicker.getCurrentId();
		stylePicker.setPreviewColor(colorPicker.getColor());
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
