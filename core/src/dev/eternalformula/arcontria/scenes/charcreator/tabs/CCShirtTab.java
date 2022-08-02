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
import dev.eternalformula.arcontria.util.EFUtil;

public class CCShirtTab extends CharacterCreatorTab {

	private CCStylePicker stylePicker;
	private CCColorPicker colorPicker;
	
	public CCShirtTab(CharacterCreatorScene scene) {
		super(scene);
		
		this.tabId = 4;
	}

	@Override
	public void load() {
		TextureAtlas ccAtlas = Assets.get("ui/charcreator/charcreator.atlas", TextureAtlas.class);
		
		this.tabIcon = new EFButton(this, 172, 138);
		tabIcon.setSkin(new TextureRegion(ccAtlas.findRegion("tabicons"), 72, 0, 18, 18));
		tabIcon.setClickSkin(new TextureRegion(ccAtlas.findRegion("selectedtabicons"), 72, 0, 18, 18));
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
		
		TextureRegion shirtRegion = Assets.get("ui/charcreator/playerbuilder/playerbuilder.atlas",
				TextureAtlas.class).findRegion("shirt");
		
		// Style Picker
		this.stylePicker = new CCStylePicker(scene, this, 64, 34);
		stylePicker.setPreviewTexture(shirtRegion, 16, 12);
		stylePicker.setItemTextPrefix("Shirt");
		stylePicker.setMaxItems(1);
		
		// Color Picker
		this.colorPicker = new CCColorPicker(156, 34);
		colorPicker.setColor(Color.BLUE);
				
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
		
		scene.getPlayerPreview().setShirtColor(colorPicker.getColor());
		scene.getPlayerPreview().shirtId = stylePicker.getCurrentId();
		stylePicker.setPreviewColor(colorPicker.getColor());

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
