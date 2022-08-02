package dev.eternalformula.arcontria.scenes.charcreator.tabs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.eternalformula.arcontria.scenes.charcreator.CharacterCreatorScene;
import dev.eternalformula.arcontria.ui.UIContainer;
import dev.eternalformula.arcontria.ui.UIElement;
import dev.eternalformula.arcontria.ui.actions.ButtonClickAction;
import dev.eternalformula.arcontria.ui.elements.EFButton;

public abstract class CharacterCreatorTab extends UIContainer {
	
	protected CharacterCreatorScene scene;
	
	protected int tabId;
	
	protected int selection;
	
	protected EFButton tabIcon;
	
	public CharacterCreatorTab(CharacterCreatorScene scene) {
		this.scene = scene;
		load();
	}
	
	public int getSelection() {
		return selection;
	}
	
	public abstract void load();
	
	public void update(float delta) {
		tabIcon.update(delta);
		
		if (scene.getCurrentTab() == tabId) {
			for (UIElement e : children) {
				e.update(delta);
			}
		}
	}
	
	@Override
	public void draw(SpriteBatch uiBatch, float delta) {
		tabIcon.draw(uiBatch, delta);
		
		if (scene.getCurrentTab() == tabId) {
			drawChildren(uiBatch, delta);
		}
	}
	
	public abstract void dispose();
	
	public void setCurrentTab(boolean isCurrentTab) {
		tabIcon.setClicked(isCurrentTab);
	}
	
	@Override
	public void onKeyTyped(char key) {
		if (scene.getCurrentTab() == tabId) {
			super.onKeyTyped(key);
		}
	}
	
	@Override
	public void onMouseClicked(int x, int y, int button) {
		tabIcon.onMouseClicked(x, y, button);
		
		if (scene.getCurrentTab() == tabId) {
			for (UIElement e : children) {
				e.onMouseClicked(x, y, button);
			}
		}
	}
	
	@Override
	public void onMouseDrag(int x, int y) {
		
		if (scene.getCurrentTab() == tabId) {
			for (UIElement e : children) {
				e.onMouseDrag(x, y);
			}
		}
	}
	
	public void setIconClickAction(ButtonClickAction action) {
		tabIcon.setClickAction(action);
	}
	
	public int getTabId() {
		return tabId;
	}
}
