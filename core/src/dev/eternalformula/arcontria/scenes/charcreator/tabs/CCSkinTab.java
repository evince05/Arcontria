package dev.eternalformula.arcontria.scenes.charcreator.tabs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.eternalformula.arcontria.scenes.charcreator.CharacterCreatorScene;
import dev.eternalformula.arcontria.ui.actions.ButtonClickAction;
import dev.eternalformula.arcontria.ui.charcreator.CCColorPicker;
import dev.eternalformula.arcontria.ui.charcreator.CCItemSelector;
import dev.eternalformula.arcontria.ui.elements.EFButton;
import dev.eternalformula.arcontria.util.Assets;

public class CCSkinTab extends CharacterCreatorTab {

	private CCItemSelector itemSelector;
	private CCColorPicker colorPicker;
	
	public CCSkinTab(CharacterCreatorScene scene) {
		super(scene);
		
		this.tabId = 2;
	}

	@Override
	public void load() {
		TextureAtlas ccAtlas = Assets.get("ui/charcreator/charcreator.atlas", TextureAtlas.class);
		
		this.tabIcon = new EFButton(this, 132, 138);
		tabIcon.setSkin(new TextureRegion(ccAtlas.findRegion("tabicons"), 18, 0, 18, 18));
		tabIcon.setClickSkin(new TextureRegion(ccAtlas.findRegion("selectedtabicons"), 18, 0, 18, 18));
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
		
		// Color Picker
		this.colorPicker = new CCColorPicker(150, 34);
		
		this.itemSelector = new CCItemSelector(this, 50, 60);
		itemSelector.setItemColor(colorPicker.getColor());
		itemSelector.setItemName("Skin");
				
		// Child Management
		addChildren(itemSelector, colorPicker);
	}

	@Override
	public void draw(SpriteBatch batch, float delta) {
		super.draw(batch, delta);

	}

	@Override
	public void update(float delta) {
		super.update(delta);

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
