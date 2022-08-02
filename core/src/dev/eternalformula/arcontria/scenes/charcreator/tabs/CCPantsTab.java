package dev.eternalformula.arcontria.scenes.charcreator.tabs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.eternalformula.arcontria.scenes.charcreator.CharacterCreatorScene;
import dev.eternalformula.arcontria.ui.actions.ButtonClickAction;
import dev.eternalformula.arcontria.ui.charcreator.CCColorPicker;
import dev.eternalformula.arcontria.ui.charcreator.CCStylePicker;
import dev.eternalformula.arcontria.ui.elements.EFButton;
import dev.eternalformula.arcontria.util.Assets;
import dev.eternalformula.arcontria.util.EFUtil;

public class CCPantsTab extends CharacterCreatorTab {

	private CCStylePicker stylePicker;
	private CCColorPicker colorPicker;
	
	public CCPantsTab(CharacterCreatorScene scene) {
		super(scene);
		
		this.tabId = 5;
	}

	@Override
	public void load() {
		TextureAtlas ccAtlas = Assets.get("ui/charcreator/charcreator.atlas", TextureAtlas.class);
		
		this.tabIcon = new EFButton(this, 192, 138);
		tabIcon.setSkin(new TextureRegion(ccAtlas.findRegion("tabicons"), 90, 0, 18, 18));
		tabIcon.setClickSkin(new TextureRegion(ccAtlas.findRegion("selectedtabicons"), 90, 0, 18, 18));
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
		
		TextureRegion pantsRegion = Assets.get("ui/charcreator/playerbuilder/playerbuilder.atlas", 
				TextureAtlas.class).findRegion("pantsitems");
		
		// Style Picker
		this.stylePicker = new CCStylePicker(scene, this, 64, 34);
		stylePicker.setItemTextPrefix("Pants");
		stylePicker.setPreviewTexture(pantsRegion, 16, 16);
		stylePicker.setMaxItems(1);
		
		// Color Picker
		this.colorPicker = new CCColorPicker(156, 34);
		colorPicker.setColor(EFUtil.getColorFromRGB(9, 125, 184));
				
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
		
		scene.getPlayerPreview().setPantsColor(colorPicker.getColor());
		scene.getPlayerPreview().pantsId = stylePicker.getCurrentId();
		stylePicker.setPreviewColor(colorPicker.getColor());
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
